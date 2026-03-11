package com.sofamaniac.crabir.ui.markdown

import android.content.Context
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class PassThroughTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }

        val action = event.action
        val layout = layout ?: return super.onTouchEvent(event)
        val x = event.x.toInt() - totalPaddingLeft
        val y = event.y.toInt() - totalPaddingTop
        val line = layout.getLineForVertical(y)
        val offset = layout.getOffsetForHorizontal(line, x.toFloat())

        val clickableSpans = listOf(URLSpan::class.java, SpoilerSpan::class.java)
        var clickedSpan: ClickableSpan? = null
        for (span in clickableSpans) {
            val spans = (text as? Spanned)?.getSpans(offset, offset, span)
            if (!spans.isNullOrEmpty()) {
                // Stop on the first clickable span found
                clickedSpan = spans.first()
                break
            }
        }

        if (clickedSpan == null) {
            return false
        }
        when (action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                clickedSpan.onClick(this)
                return true
            }
        }
        // Pass all other events to parent
        return false
    }
}