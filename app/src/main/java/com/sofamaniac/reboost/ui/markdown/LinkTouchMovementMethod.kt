package com.sofamaniac.reboost.ui.markdown

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

// Code produced by Claude
class LinkTouchMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            // Check for URLSpan specifically (actual links), not all ClickableSpans
            val links = buffer.getSpans(off, off, ClickableSpan::class.java)

            if (links.isNotEmpty()) {
                return super.onTouchEvent(widget, buffer, event)
            }
        }

        // No link at touch position, don't consume the event
        return false
    }

    companion object {
        private var sInstance: LinkTouchMovementMethod? = null

        fun getInstance(): MovementMethod {
            if (sInstance == null) {
                sInstance = LinkTouchMovementMethod()
            }
            return sInstance!!
        }
    }
}