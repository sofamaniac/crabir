package com.sofamaniac.crabir.domain.repository.thread

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.votable.CommentType
import kotlin.math.max

data class Forest(
    val root: Fullname,
    private val last: Fullname,
    private val nodes: Map<Fullname, CommentType>,
    private val order: Map<Fullname, Fullname>,
    val depth: Map<Fullname, Int>,
) : Iterable<CommentType> {
    companion object {
        fun new(root: Fullname): Forest {
            return Forest(root, last = root, emptyMap(), emptyMap(), emptyMap())
        }

        fun from(l: List<Thing>, root: Fullname, depth: Int = 0): Forest {
            return l.fold(new(root)) { acc, thing ->
                flatten(thing, acc, depth)
            }
        }

        fun from(thing: CommentType): Forest = new(thing.name).copy(
            nodes = mapOf(thing.name to thing),
            last = thing.name
        )
    }

    fun add(thing: CommentType, depth: Int): Forest = copy(
        nodes = nodes + (thing.name to thing),
        last = thing.name,
        order = order + (last to thing.name),
        depth = this.depth + (thing.name to max(depth, thing.depth))
    )

    fun isEmpty(): Boolean = nodes.isEmpty()
    fun isNotEmpty(): Boolean = nodes.isNotEmpty()
    fun first(): CommentType? {
        val next = order[root]
        return nodes[next]
    }

    fun insert(base: Fullname, comment: Forest, keepBase: Boolean = true): Forest {
        if (comment.isEmpty()) {
            return this
        }

        val comments = nodes.toMutableMap()
        comments.putAll(comment.nodes)

        val endNext = order[base]

        val newOrder = order.toMutableMap()
        newOrder.putAll(comment.order)

        if (endNext != null) {
            newOrder[comment.last] = endNext
        }

        val first = comment.first()!!
        if (keepBase) {
            newOrder[base] = first.name
        } else {
            val previous = newOrder.entries.find { it.value == base }?.key
            newOrder[previous!!] = first.name
            comments.remove(base)
            newOrder.remove(base)
        }
        val last = if (base == last) comment.last else last
        return copy(last = last, nodes = comments, order = newOrder, depth = depth + comment.depth)
    }

    override fun iterator(): Iterator<CommentType> {
        val iterator = object : Iterator<CommentType> {
            var current: Fullname? = order[root]

            // In a perfect world it would not be necessary to keep a set of seen comments
            var seen: HashSet<Fullname> = HashSet()
            override fun next(): CommentType {
                val comment = nodes[current]
                if (comment == null) {
                    throw NoSuchElementException("$current")
                }
                val next = order[current]
                seen.add(current!!)
                current = next
                return comment
            }

            override fun hasNext(): Boolean {
                val nextName = order[current]
                return current != null && !seen.contains(nextName)
            }
        }
        return iterator
    }
}

private fun flatten(thing: Thing, acc: Forest, depth: Int = 0): Forest = when (thing) {
    is Thing.More -> {
        val more = CommentType.More(thing.data)
        acc.add(more, depth)
    }

    is Thing.Comment -> {
        val comment = thing.toCommentType()
        val replies = thing.data.replies
        replies.fold(
            acc.add(comment, depth)
        ) { acc, thing ->
            flatten(thing, acc, depth + 1)
        }
    }

    else -> {
        throw IllegalArgumentException("Forest: Expected More or Comment: $thing")
    }
}
