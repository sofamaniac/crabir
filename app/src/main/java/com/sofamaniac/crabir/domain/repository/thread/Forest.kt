package com.sofamaniac.crabir.domain.repository.thread

import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.votable.CommentType

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
        depth = this.depth + (thing.name to depth)
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


//data class Forest(
//    val root: Fullname,
//    val nodes: Map<Fullname, CommentType>,
//    val end: Fullname? = null,
//) : Iterable<CommentType> {
//    companion object {
//        fun new(root: Fullname): Forest {
//            return Forest(root, emptyMap(), root)
//        }
//
//        fun create(l: List<Thing>, root: Fullname? = null, depth: Int = 0): Forest? {
//            val base = root ?: l.firstOrNull()?.name
//            return if (base == null) {
//                null
//            } else {
//                l.fold(new(base)) { acc, thing ->
//                    flatten(thing, acc, depth)
//                }
//            }
//        }
//
//        fun create(comment: CommentType): Forest {
//            return Forest(comment.name, mapOf(comment.name to comment))
//        }
//    }
//
//    fun insert(
//        name: Fullname?,
//        comment: Forest,
//        keepRoot: Boolean = true,
//    ): Forest {
//        val base = name?.let(nodes::get) ?: return comment
//
//        val newNodes = nodes.toMutableMap()
//        val isRoot = name == root
//        val next = base.nextSibling
//        val previous = base.previousSibling
//
//        fun insertAndConnectEnd(nextSibling: Fullname?) {
//            for ((key, node) in comment.nodes) {
//                newNodes[key] = node
//                if (nextSibling != null && node.nextSibling == null) {
//                    newNodes[key] = node.copy(nextSibling = nextSibling)
//                    newNodes[nextSibling] =
//                        newNodes[nextSibling]!!.copy(previousSibling = key)
//                }
//            }
//        }
//
//        fun linkStart(start: Fullname?) {
//            if (start == null) return
//            newNodes[comment.root] =
//                newNodes[comment.root]!!.copy(previousSibling = start)
//            val previous = newNodes[start]
//            if (previous != null) {
//                newNodes[previous.name] = previous.copy(nextSibling = comment.root)
//            }
//        }
//
//        // Inserting at the root
//        if (isRoot) {
//            if (!keepRoot) {
//                newNodes.remove(name)
//            } else {
//                newNodes[name] = base.copy(nextSibling = comment.root)
//            }
//            insertAndConnectEnd(next)
//            return Forest(
//                if (keepRoot) base.name else comment.root,
//                newNodes,
//                end ?: comment.end
//            )
//        }
//
//        // Inserting at the end
//        if (next == null) {
//            val previous = base.previousSibling?.let(nodes::get)
//            if (!keepRoot) {
//                newNodes.remove(name)
//            }
//            newNodes[base.name] = base.copy(nextSibling = comment.root)
//            newNodes.putAll(comment.nodes)
//            val start = if (keepRoot) base.name else previous?.name
//            linkStart(start)
//            return Forest(root, newNodes, comment.end)
//        }
//
//        // Inserting in the middle
//        if (keepRoot) {
//            insertAndConnectEnd(next)
//            linkStart(base.name)
//        } else {
//            newNodes.remove(name)
//            insertAndConnectEnd(next)
//            linkStart(previous)
//        }
//
//        return Forest(root, newNodes, end ?: comment.end)
//    }
//
//    override fun iterator(): Iterator<CommentType> {
//        val iterator = object : Iterator<CommentType> {
//            var current: Fullname? = this@Forest.root
//            var seen: HashSet<Fullname> = HashSet()
//            override fun next(): CommentType {
//                val next = this@Forest.nodes[current]?.nextSibling
//                seen.add(current!!)
//                val comment = this@Forest.nodes[current]
//                if (comment == null) {
//                    throw NoSuchElementException("$current")
//                }
//                current = next
//                return comment
//            }
//
//            override fun hasNext(): Boolean {
//                val nextName = nodes[current]?.nextSibling
//                return nodes.contains(current) && !seen.contains(nextName)
//            }
//        }
//        return iterator
//    }
//
//    fun isNotEmpty(): Boolean {
//        return nodes.isNotEmpty()
//    }
//}
//
//fun flatten(thing: Thing, acc: Forest, depth: Int = 0): Forest {
//    when (thing) {
//        is Thing.More -> {
//            val more = CommentType.More(thing.data)
//                .copy(previousSibling = acc.end, depth = max(thing.data.depth, depth))
//            val nodes = acc.nodes.updateIfExists(acc.end) {
//                it.copy(nextSibling = thing.name)
//            } + (thing.name to more)
//            return acc.copy(
//                nodes = nodes,
//                end = thing.name
//            )
//        }
//
//        !is Thing.Comment -> {
//            throw IllegalArgumentException("Unknown thing type: $thing")
//        }
//
//        else -> {}
//    }
//
//    val comment = thing.toCommentType()
//    val children = thing.data.replies
//    val next = children.firstOrNull()
//    val root = comment.copy(
//        previousSibling = acc.end,
//        nextSibling = next?.name,
//        depth = max(comment.depth, depth)
//    )
//    val nodes = acc.nodes.updateIfExists(acc.end) {
//        it.copy(nextSibling = root.name)
//    } + (root.name to root)
//
//
//    return children.fold(acc.copy(nodes = nodes, end = thing.name)) { acc, thing ->
//        flatten(
//            thing,
//            acc,
//            depth + 1,
//        )
//    }
//}
//
//
//fun <K, V> Map<K, V>.updateIfExists(key: K?, update: (V) -> V): Map<K, V> {
//    return if (key == null) {
//        this
//    } else when (val old = this[key]) {
//        null -> {
//            Log.d("ForestNew", "updateIfExists: $key missing")
//            this
//        }
//
//        else -> this + (key to update(old))
//    }
//}
