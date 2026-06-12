package dev.piotrprus.particleemitter.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Darcula-inspired palette. */
private object CodeColors {
    val default = Color(0xFFA9B7C6)
    val keyword = Color(0xFFCC7832)
    val string = Color(0xFF6A8759)
    val comment = Color(0xFF808080)
    val number = Color(0xFF6897BB)
    val annotation = Color(0xFFBBB529)
    val functionCall = Color(0xFFFFC66B)
}

private val kotlinKeywords = setOf(
    "package", "import", "fun", "val", "var", "if", "else", "when", "while",
    "for", "return", "object", "class", "interface", "data", "sealed", "enum",
    "is", "in", "null", "true", "false", "this", "super", "private", "public",
    "internal", "protected", "override", "suspend", "by", "lazy", "it",
    "continue", "break", "do", "try", "catch", "finally", "throw", "as",
)

private val tokenPattern = Regex(
    "(//[^\n]*)" +                                        // 1 line comment
        "|(\"\"\"[\\s\\S]*?\"\"\"|\"(?:\\\\.|[^\"\\\\\n])*\")" + // 2 string
        "|(@\\w+)" +                                      // 3 annotation
        "|\\b(\\d+(?:\\.\\d+)?(?:[fFL])?)\\b" +           // 4 number
        "|\\b(\\w+)(?=\\()" +                             // 5 call
        "|\\b([a-zA-Z_]\\w*)\\b",                          // 6 identifier
)

fun highlightKotlin(code: String): AnnotatedString = buildAnnotatedString {
    var cursor = 0
    for (match in tokenPattern.findAll(code)) {
        if (match.range.first > cursor) {
            withColor(code.substring(cursor, match.range.first), CodeColors.default)
        }
        val text = match.value
        val groups = match.groups
        val color = when {
            groups[1] != null -> CodeColors.comment
            groups[2] != null -> CodeColors.string
            groups[3] != null -> CodeColors.annotation
            groups[4] != null -> CodeColors.number
            groups[5] != null ->
                if (text in kotlinKeywords) CodeColors.keyword else CodeColors.functionCall
            else -> if (text in kotlinKeywords) CodeColors.keyword else CodeColors.default
        }
        withColor(text, color)
        cursor = match.range.last + 1
    }
    if (cursor < code.length) {
        withColor(code.substring(cursor), CodeColors.default)
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.withColor(
    text: String,
    color: Color,
) {
    pushStyle(SpanStyle(color = color))
    append(text)
    pop()
}

@Composable
fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 26.sp,
) {
    val highlighted = remember(code) { highlightKotlin(code.trimIndent()) }
    Box(
        modifier = modifier
            .background(DeckColors.codeBackground, RoundedCornerShape(20.dp))
            .border(1.dp, DeckColors.codeBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 40.dp, vertical = 32.dp),
    ) {
        Text(
            text = highlighted,
            fontFamily = FontFamily.Monospace,
            fontSize = fontSize,
            lineHeight = fontSize * 1.45f,
        )
    }
}
