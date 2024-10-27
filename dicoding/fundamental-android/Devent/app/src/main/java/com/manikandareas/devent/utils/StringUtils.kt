package com.manikandareas.devent.utils

object StringUtils {
    fun cutPretty(sentences: String, length: Int): String {
        return if (sentences.length > length) {
            sentences.take(length) + "..."
        } else {
            sentences
        }
    }

    fun twoWordsFirst (sentences: String): String {
        return sentences.split(" ").take(2).joinToString(" ")
    }

    fun fromHTML(html: String): String {
        val imageRegex = Regex("<img[^>]*>")

        // Remove image tags while preserving their content
        val sanitizedHtml = imageRegex.replace(html) { matchResult ->
            val content = matchResult.value.substringAfter(">").substringBefore("</img>")
            content.replace("<", "&lt;").replace(">", "&gt;") // Escape remaining HTML tags
        }

        return android.text.Html.fromHtml(sanitizedHtml, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
    }
}