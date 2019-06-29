package com.ty.voogla.widght

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author TY
 * RecycleView item 分割线
 */
class SpaceItemDecoration(private val space: Int, private val isGridManager: Boolean = false) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (isGridManager) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space

        } else {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = space
            }
        }
    }
}