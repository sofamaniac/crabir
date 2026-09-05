package com.sofamaniac.crabir.data.remote.utils

import android.util.Log
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode
import com.sofamaniac.crabir.domain.model.Richtext
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.model.TableAlignment
import com.sofamaniac.crabir.domain.model.TableCell
import com.sofamaniac.crabir.domain.model.TableHeader
import com.sofamaniac.crabir.domain.model.TextModifier
import com.sofamaniac.crabir.domain.model.TextStyle

fun RichtextDocument.Companion.fromHtml(html: String): RichtextDocument {
    val html = html.replace("<code>", "<pre>").replace("</code>", "</pre>")
    val document = Ksoup.parse(html)
    val root = document.getElementsByClass("md").first()
        ?: return RichtextDocument(emptyList())
    val children = root.childNodes().flatMap { Richtext.fromHtml(it) }
    val fusedChildren = mutableListOf<Richtext>()
    var par = Richtext.Paragraph(emptyList())
    for (child in children) {
        if (child is Richtext.LeafBlock) {
            par = par.copy(children = par.children + child)
        } else {
            if (par.children.isNotEmpty()) {
                fusedChildren.add(par)
            }
            par = Richtext.Paragraph(emptyList())
            fusedChildren.add(child)
        }
    }
    if (par.children.isNotEmpty()) {
        fusedChildren.add(par)
    }
    return RichtextDocument(fusedChildren)
}

private fun Richtext.Companion.fromHtml(node: Node, style: Int = 0): List<Richtext> {
    return when (node) {
        is Element -> fromHtml(node, style)
        is TextNode if (node.text().isNotBlank()) -> listOf(
            Richtext.Text(
                node.text(),
                if (style == 0) {
                    emptyList()
                } else {
                    listOf(
                        TextModifier(
                            style,
                            0,
                            node.text().length
                        )
                    )
                }
            )
        )

        else -> {
            Log.e("Richtext", "Unknown node: $node")
            emptyList()
        }
    }
}

private fun Richtext.Companion.fromHtml(node: Element, style: Int = 0): List<Richtext> {
    return when (node.tagName()) {
        "h1" -> {
            listOf(Richtext.Heading(1, node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "h2" -> {
            listOf(Richtext.Heading(2, node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "h3" -> {
            listOf(Richtext.Heading(3, node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "h4" -> {
            listOf(Richtext.Heading(4, node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "h5" -> {
            listOf(Richtext.Heading(5, node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "h6" -> {
            listOf(Richtext.Heading(6, node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "p" -> {
            listOf(Richtext.Paragraph(node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "blockquote" -> {
            listOf(Richtext.Blockquote(node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "ul" -> {
            listOf(
                Richtext.ListBlock(
                    node.childNodes().flatMap { Richtext.fromHtml(it) },
                    false
                )
            )
        }

        "ol" -> {
            listOf(
                Richtext.ListBlock(
                    node.childNodes().flatMap { Richtext.fromHtml(it) },
                    true
                )
            )
        }

        "li" -> {
            listOf(Richtext.ListItem(node.childNodes().flatMap { Richtext.fromHtml(it) }))
        }

        "hr" -> {
            listOf(Richtext.HorizontalRule())
        }

        "br" -> {
            listOf(Richtext.LineBreak)
        }

        "span" -> {
            if (node.className().contains("spoiler")) {
                listOf(Richtext.Spoiler(node.childNodes().flatMap { Richtext.fromHtml(it) }))
            } else {
                Log.e("Richtext", "Unknown span: ${node.html()}")
                listOf(Richtext.Raw(node.text()))
            }
        }

        "code" -> {
            Log.d("Richtext", "Code: ${node.childNodes()}")
            if (node.text().contains("\n")) {
                listOf(Richtext.Code(node.text().lines().map { Richtext.Raw(it) }))
            } else {
                listOf(
                    Richtext.Text(
                        node.text(),
                        listOf(TextModifier(TextStyle.InlineCode.value, 0, node.text().length))
                    )
                )
            }
        }

        "pre" -> {
            Log.d("Richtext", "Code: ${node.childNodes()}")
            if (node.text().contains("\n")) {
                listOf(Richtext.Code(node.text().lines().map { Richtext.Raw(it) }))
            } else {
                listOf(
                    Richtext.Text(
                        node.text(),
                        listOf(TextModifier(TextStyle.InlineCode.value, 0, node.text().length))
                    )
                )
            }
        }

        "a" -> {
            val url = node.attr("href")
            val modifiers = if (style != 0) {
                listOf(
                    TextModifier(
                        style,
                        0,
                        node.text().length
                    )
                )
            } else {
                emptyList()
            }
            val dest = node.text().split("/").last()
            when {
                url.startsWith("/u") -> listOf(
                    Richtext.UserLink(
                        dest,
                        false,
                        modifiers,
                    )
                )

                url.startsWith("/r") -> listOf(Richtext.CommunityLink(dest, true, modifiers))
                else -> listOf(Richtext.Link(node.text(), url, modifiers))
            }
        }

        "table" -> {
            listOf(parseTable(node))
        }

        "sup" -> {
            node.childNodes().flatMap {
                Richtext.fromHtml(
                    it,
                    style = style or TextStyle.Superscript.value
                )
            }
        }

        "strong" -> {
            node.childNodes().flatMap {
                Richtext.fromHtml(
                    it,
                    style = style or TextStyle.Bold.value
                )
            }
        }

        "em" -> {
            node.childNodes().flatMap {
                Richtext.fromHtml(
                    it,
                    style = style or TextStyle.Italic.value
                )
            }
        }

        "del" -> {
            node.childNodes().flatMap {
                Richtext.fromHtml(
                    it,
                    style = style or TextStyle.Strikethrough.value
                )
            }
        }

        else -> {
            Log.e("Richtext", "Unknown tag: ${node.tagName()}")
            Log.e("Richtext", "Html: ${node.html()}")
            listOf(Richtext.Raw(node.text()))
        }
    }
}

private fun parseTable(node: Element): Richtext.Table {
    assert(node.tagName() == "table")
    val headersHtml = node.getElementsByTag("thead").first()?.getElementsByTag("tr")?.first()
    val rowsHtml = node.getElementsByTag("tbody").first()?.getElementsByTag("tr")
    val headers = headersHtml?.children()?.mapNotNull { header ->
        when (header.tagName()) {
            "th" -> {
                val alignment = when (header.attr("align")) {
                    "left" -> TableAlignment.Left
                    "right" -> TableAlignment.Right
                    else -> TableAlignment.Center
                }
                TableHeader(alignment, header.childNodes().flatMap { Richtext.fromHtml(it) })
            }

            else -> null
        }
    }
    val rows = rowsHtml?.map { row ->
        row.children().mapNotNull { child ->
            when (child.tagName()) {
                "td" -> TableCell(child.childNodes().flatMap { Richtext.fromHtml(it) })
                else -> null
            }
        }
    }
    return Richtext.Table(headers ?: emptyList(), rows ?: emptyList())
}
