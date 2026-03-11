package com.sofamaniac.crabir.ui.markdown

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class SpoilerSpan : ClickableSpan() {
    private var isRevealed = false
    private var startingTextColor = Color.WHITE

    override fun onClick(widget: View) {
        isRevealed = true
        widget.invalidate()
    }

    override fun updateDrawState(ds: TextPaint) {
        if (!isRevealed) {
            // Save original text color
            startingTextColor = ds.color
            // Hide text by making it same color as background
            ds.bgColor = Color.GRAY
            ds.color = Color.GRAY
        } else {
            // Reveal text
            ds.bgColor = Color.TRANSPARENT
            ds.color = startingTextColor
        }
    }
}