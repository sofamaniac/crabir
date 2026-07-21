package com.sofamaniac.crabir.domain.model
// File entirely written by gemini

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RichtextTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private fun decode(jsonString: String): RichtextDocument {
        return json.decodeFromString<RichtextDocument>("{\"document\": [$jsonString]}")
    }

    @Test
    fun testHeading() {
        val jsonString = """
            {
              "c": [
                {
                  "e": "raw",
                  "t": "Level 1"
                }
              ],
              "e": "h",
              "l": 1
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val heading = doc.document[0] as Richtext.Heading
        assertEquals(1, heading.level)
        assertTrue(heading.children[0] is Richtext.Raw)
        assertEquals("Level 1", (heading.children[0] as Richtext.Raw).text)
    }

    @Test
    fun testParagraphAndText() {
        val jsonString = """
            {
              "c": [
                {
                  "e": "text",
                  "t": "This is some normal text"
                }
              ],
              "e": "par"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val par = doc.document[0] as Richtext.Paragraph
        val text = par.children[0] as Richtext.Text
        assertEquals("This is some normal text", text.text)
    }

    @Test
    fun testTextModifiers() {
        val jsonString = """
            {
              "c": [
                {
                  "e": "text",
                  "t": "Bold text",
                  "f": [[1, 0, 9]]
                }
              ],
              "e": "par"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val par = doc.document[0] as Richtext.Paragraph
        val text = par.children[0] as Richtext.Text
        assertEquals(1, text.modifiers.size)
        assertEquals(1, text.modifiers[0].style)
        assertEquals(0, text.modifiers[0].start)
        assertEquals(9, text.modifiers[0].end)
    }

    @Test
    fun testLists() {
        val jsonString = """
            {
              "c": [
                {
                  "c": [
                    {
                      "c": [
                        { "e": "text", "t": "item 1" }
                      ],
                      "e": "par"
                    }
                  ],
                  "e": "li"
                }
              ],
              "e": "list",
              "o": false
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val list = doc.document[0] as Richtext.ListBlock
        assertEquals(false, list.ordered)
        val listItem = list.children[0] as Richtext.ListItem
        val par = listItem.children[0] as Richtext.Paragraph
        assertEquals("item 1", (par.children[0] as Richtext.Text).text)
    }

    @Test
    fun testBlockquote() {
        val jsonString = """
            {
              "c": [
                {
                  "c": [
                    { "e": "text", "t": "quote" }
                  ],
                  "e": "par"
                }
              ],
              "e": "blockquote"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val quote = doc.document[0] as Richtext.Blockquote
        val par = quote.children[0] as Richtext.Paragraph
        assertEquals("quote", (par.children[0] as Richtext.Text).text)
    }

    @Test
    fun testSpoiler() {
        val jsonString = """
            {
              "c": [
                {
                  "c": [
                    { "e": "text", "t": "spoiler" }
                  ],
                  "e": "spoilertext"
                }
              ],
              "e": "par"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val par = doc.document[0] as Richtext.Paragraph
        val spoiler = par.children[0] as Richtext.Spoiler
        val text = spoiler.children[0] as Richtext.Text
        assertEquals("spoiler", text.text)
    }

    @Test
    fun testCode() {
        val jsonString = """
            {
              "c": [
                { "e": "raw", "t": "code line 1" }
              ],
              "e": "code"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val code = doc.document[0] as Richtext.Code
        assertEquals("code line 1", (code.children[0] as Richtext.Raw).text)
    }

    @Test
    fun testLink() {
        val jsonString = """
            {
              "u": "https://google.com",
              "e": "link",
              "t": "Google"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val link = doc.document[0] as Richtext.Link
        assertEquals("https://google.com", link.url)
        assertEquals("Google", link.text)
    }

    @Test
    fun testTable() {
        val jsonString = """
            {
              "h": [
                {
                  "a": "C",
                  "c": [ { "e": "text", "t": "Header" } ]
                }
              ],
              "c": [
                [
                  { "c": [ { "e": "text", "t": "Cell" } ] }
                ]
              ],
              "e": "table"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val table = doc.document[0] as Richtext.Table
        assertEquals(1, table.headers.size)
        assertEquals(TableAlignment.Center, table.headers[0].alignment)
        assertEquals(1, table.rows.size)
        assertEquals(1, table.rows[0].size)
        val cell = table.rows[0][0]
        assertEquals("Cell", (cell.children[0] as Richtext.Text).text)
    }

    @Test
    fun testLineBreak() {
        val jsonString = """
            {
              "c": [
                { "e": "text", "t": "line 1" },
                { "e": "br" },
                { "e": "text", "t": "line 2" }
              ],
              "e": "par"
            }
        """.trimIndent()
        val doc = decode(jsonString)
        val par = doc.document[0] as Richtext.Paragraph
        assertTrue(par.children[1] is Richtext.LineBreak)
    }

    @Test
    fun testFullDocument() {
        val jsonString = """
             {
               "document": [
                 {
                   "c": [
                     {
                       "e": "raw",
                       "t": "Level 1"
                     }
                   ],
                   "e": "h",
                   "l": 1
                 },
                 {
                   "c": [
                     {
                       "e": "text",
                       "t": "This is some normal text"
                     }
                   ],
                   "e": "par"
                 }
               ]
             }
        """.trimIndent()
        val document = json.decodeFromString<RichtextDocument>(jsonString)
        assertNotNull(document)
        assertEquals(2, document.document.size)
        assertTrue(document.document[0] is Richtext.Heading)
        assertTrue(document.document[1] is Richtext.Paragraph)
    }
}
