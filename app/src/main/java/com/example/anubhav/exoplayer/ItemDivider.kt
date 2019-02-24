package com.example.anubhav.exoplayer;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

class ItemDivider(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {
    private var mOrientation: Int = 0

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontalDividers(canvas, parent)
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVerticalDividers(canvas, parent)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            return
        }

        mOrientation = (parent.layoutManager as LinearLayoutManager).orientation
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = mDivider.intrinsicWidth
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.top = mDivider.intrinsicHeight
        }
    }

    private fun drawHorizontalDividers(canvas: Canvas, parent: RecyclerView) {
        val parentTop = parent.paddingTop
        val parentBottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val parentLeft = child.right + params.rightMargin
            val parentRight = parentLeft + mDivider.intrinsicWidth

            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom)
            mDivider.draw(canvas)
        }
    }

    private fun drawVerticalDividers(canvas: Canvas, parent: RecyclerView) {
        val parentLeft = parent.paddingLeft
        val parentRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val parentTop = child.bottom + params.bottomMargin
            val parentBottom = parentTop + mDivider.intrinsicHeight

            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom)
            mDivider.draw(canvas)
        }
    }
}
