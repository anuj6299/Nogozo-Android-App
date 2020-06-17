package com.startup.startup.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecoration(val verticalSpace: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State){
        outRect.bottom = verticalSpace
        outRect.left = verticalSpace
        outRect.right = verticalSpace
        outRect.top = verticalSpace
    }
}