package com.realtouchapp.cplanner.main.begin.ui.adaptercallback

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R


/*
    作者: RobertChou
    時間: 2019/6/2 下午 11:43
    信箱: didi31139@gmail.com
    說明:
*/
class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

    val ALPHA_FULL = 1.0f

    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#3F5D95")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    override fun isLongPressDragEnabled(): Boolean {

        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // Set movement flags based on the layout manager
        if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.adapterPosition)

    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            // Fade out the view as it is swiped out of the parent's bounds
//            val alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width.toFloat()
//            viewHolder.itemView.alpha = alpha
//            viewHolder.itemView.translationX = dX
//        } else {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        }
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive
        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        //刪除
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Here, if dX > 0 then swiping right.
            // If dX < 0 then swiping left.
            // If dX == 0 then at at start position.
            if (dX < 0) {
                // Draw the red delete background
                background.color = backgroundColor
                background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                background.draw(c)
                val deleteIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.vd_ic_remove)
                val intrinsicWidth = deleteIcon!!.intrinsicWidth
                val intrinsicHeight = deleteIcon!!.intrinsicHeight
                // Calculate position of delete icon
                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                // Draw the delete icon
                deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon!!.draw(c)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }else{
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                val itemViewHolder = viewHolder as ItemTouchHelperViewHolder?
                itemViewHolder!!.onItemSelected()
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView!!, viewHolder)

        viewHolder.itemView.alpha = ALPHA_FULL

        if (viewHolder is ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemClear()
        }
    }

    interface ItemTouchHelperAdapter {

        /**
         * Called when an item has been dragged far enough to trigger a move. This is called every time
         * an item is shifted, and **not** at the end of a "drop" event.<br></br>
         * <br></br>
         * Implementations should call [RecyclerView.Adapter.notifyItemMoved] after
         * adjusting the underlying data to reflect this move.
         *
         * @param fromPosition The start position of the moved item.
         * @param toPosition   Then resolved position of the moved item.
         * @return True if the item was moved to the new adapter position.
         *
         * @see RecyclerView.getAdapterPositionFor
         * @see RecyclerView.ViewHolder.getAdapterPosition
         */
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean


        /**
         * Called when an item has been dismissed by a swipe.<br></br>
         * <br></br>
         * Implementations should call [RecyclerView.Adapter.notifyItemRemoved] after
         * adjusting the underlying data to reflect this removal.
         *
         * @param position The position of the item dismissed.
         *
         * @see RecyclerView.getAdapterPositionFor
         * @see RecyclerView.ViewHolder.getAdapterPosition
         */
        fun onItemDismiss(position: Int)
    }

    interface ItemTouchHelperViewHolder {

        /**
         * Called when the [ItemTouchHelper] first registers an item as being moved or swiped.
         * Implementations should update the item view to indicate it's active state.
         */
        fun onItemSelected()


        /**
         * Called when the [ItemTouchHelper] has completed the move or swipe, and the active item
         * state should be cleared.
         */
        fun onItemClear()
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}