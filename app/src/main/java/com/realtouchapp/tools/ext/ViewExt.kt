package com.realtouchapp.tools.ext

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.ext.closeSoftIntPut
import com.realtouchapp.admitted.member.view.adapter.SpinnerArrayAdapter
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity
import com.realtouchapp.tools.utils.UserInterfaceTool

private val screentWidth = 375f
private val screenHeigtht = 770f

//會自行換算
fun View.setViewSizeOrignalWH(w: Int, h: Int) {
    setViewSizeByRatioWH( w/screentWidth, h/ screenHeigtht)
}

fun View.setViewSizeOrignalWW(w: Int, h: Int) {
    setViewSizeByRatioWW( w/screentWidth, h/ screentWidth)
}

fun View.setViewSizeOrignalHH(w: Int, h: Int) {
    setViewSizeByRatioHH( w/screenHeigtht, h/ screenHeigtht)
}

fun View.setViewSizeOrignalHW(w: Int, h: Int) {
    setViewSizeByRatioHW( w/screenHeigtht, h/ screentWidth)
}

fun View.setViewSizeOrignalWidth(w: Int) {
    setViewSizeWidthByRatio(w/ screentWidth)
}
fun View.setViewSizeOrignalHeight(h: Int) {
    setViewSizeHeightByRatio(h/ screenHeigtht)
}

fun View.setMarginBottomOrignal(h:Int) {
    this.setMarginBottomRatioHWithoutBar(h/ screenHeigtht)
}
fun View.setMarginTopOrignal(h:Int) {
    this.setMarginTopRatioHWithoutBar(h/ screenHeigtht)
}
fun View.setMarginRightOrignal(w:Int) {
    this.setMarginRightRatioW(w/ screentWidth)
}
fun View.setMarginLeftOrignal(w:Int) {
    this.setMarginLeftRatioW(w/ screentWidth)
}

fun View.setViewSize(w: Int, h: Int) {

    UserInterfaceTool().setViewSize(this, w, h)
}
fun View.setViewSizeWidthByRatio(w: Float) {
    UserInterfaceTool().setViewSizeWidth(this, UserInterfaceTool().getScreenWidthByRatio(this.context,w))
}
fun View.setViewSizeHeightByRatio(h: Float) {
    UserInterfaceTool().setViewSizeHeight(this, UserInterfaceTool().getScreenHeightByRatio(this.context,h))
}

fun View.setViewSizeWidth(w: Int) {
    UserInterfaceTool().setViewSizeWidth(this, w)
}
fun View.setViewSizeHeight(h: Int) {
    UserInterfaceTool().setViewSizeHeight(this, h)
}

fun View.setViewSizeByRatioWH(w: Float, h: Float) {

    UserInterfaceTool().setViewSizeByRatioWH(this, w, h)
}

fun View.setViewSizeByRatioWW(w: Float, h: Float) {

    UserInterfaceTool().setViewSizeByRatioWW(this, w, h)

}

fun View.setViewSizeByRatioHH(w: Float, h: Float) {

    UserInterfaceTool().setViewSizeByRatioHH(this, w, h)
}

fun View.setViewSizeByRatioHW(w: Float, h: Float) {

    UserInterfaceTool().setViewSizeByRatioHW(this, w, h)
}

fun View.setMargin(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
    UserInterfaceTool().setMargin(this, leftMargin, topMargin, rightMargin, bottomMargin)
}

fun View.setMarginBydp(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
    UserInterfaceTool().setMarginByDpUnit(this, leftMargin, topMargin, rightMargin, bottomMargin)
}

fun View.setMarginLeft(leftMargin: Int) {
    UserInterfaceTool().setMarginLeft(this, leftMargin)
}

fun View.setMarginLeftRatioW(ratio: Float) {
    UserInterfaceTool().setMarginLeft(this, UserInterfaceTool().getScreenWidthByRatio(this.context, ratio))
}

fun View.setMarginTop(topMargin: Int) {
    UserInterfaceTool().setMarginTop(this, topMargin)
}

fun View.setMarginRight(rightMargin: Int) {
    UserInterfaceTool().setMarginRight(this, rightMargin)
}

fun View.setMarginRightRatioW(ratio: Float) {
    UserInterfaceTool().setMarginRight(this, UserInterfaceTool().getScreenWidthByRatio(this.context, ratio))
}

fun View.setMarginBottom(bottomMargin: Int) {
    UserInterfaceTool().setMarginBottom(this, bottomMargin)
}

fun View.setMarginBottomRatioH(ratio: Float) {
    UserInterfaceTool().setMarginBottom(this, UserInterfaceTool().getScreenHeightByRatio(this.context, ratio))
}
//扣掉系統status bar 和 nav bar的高度
fun View.setMarginBottomRatioHWithoutBar(ratio: Float) {
    UserInterfaceTool().setMarginBottom(this, UserInterfaceTool().getScreenHeightWithoutBarByRatio(this.context, ratio))
}
fun View.setMarginLeftBydp(leftMargin: Int) {
    UserInterfaceTool().setMarginLeft(this, UserInterfaceTool().getPixelFromDpByDevice(this.context, leftMargin))
}

fun View.setMarginTopBydp(topMargin: Int) {
    UserInterfaceTool().setMarginTop(this, UserInterfaceTool().getPixelFromDpByDevice(this.context, topMargin))
}

fun View.setMarginBydp(margin: Int) {
    var marginDp = UserInterfaceTool().getPixelFromDpByDevice(this.context, margin)
    this.setMargin(marginDp, marginDp, marginDp, marginDp)
}

fun View.setMarginTopRatioH(ratio: Float) {
    UserInterfaceTool().setMarginTop(this, UserInterfaceTool().getScreenHeightByRatio(this.context, ratio))
}
//扣掉系統status bar 和 nav bar的高度
fun View.setMarginTopRatioHWithoutBar(ratio: Float) {
    UserInterfaceTool().setMarginTop(this, UserInterfaceTool().getScreenHeightWithoutBarByRatio(this.context, ratio))
}


fun View.setMarginRightBydp(rightMargin: Int) {
    UserInterfaceTool().setMarginRight(this, UserInterfaceTool().getPixelFromDpByDevice(this.context, rightMargin))
}

fun View.setMarginBottomBydp(bottomMargin: Int) {
    UserInterfaceTool().setMarginBottom(this, UserInterfaceTool().getPixelFromDpByDevice(this.context, bottomMargin))
}

fun View.setTextSize(spUnit: Int) {
    UserInterfaceTool().setTextSize(this, spUnit)
}

fun View.setPressedImage(unPressedDrawableID: Int, pressedDrawableID: Int) {
    UserInterfaceTool().setPressedImage(this, unPressedDrawableID, pressedDrawableID)
}

fun View.setPressedBackground(unPressedDrawableID: Int, pressedDrawableID: Int) {
    UserInterfaceTool().setPressedBackground(this, unPressedDrawableID, pressedDrawableID)
}


/**
 * 設定按鈕 被按住的顏色背景
 *
 * @param unPressedColor 未按下的顏色背景 R.color.color1
 * @param pressedColor   按下的顏色 R.color.color 0為不給
 */
fun View.setPressedTextColor(unPressedColor: Int, pressedColor: Int) {
    val context = this.context
    if (pressedColor == 0) {
        (this as TextView).setTextColor(context.resources.getColor(unPressedColor))
        return
    }
    val colorStateList = ColorStateList(arrayOf(intArrayOf(R.attr.state_pressed), intArrayOf(R.attr.state_focused), intArrayOf(R.attr.state_checked), intArrayOf()), intArrayOf(context.resources.getColor(pressedColor), context.resources.getColor(pressedColor), context.resources.getColor(pressedColor), context.resources.getColor(unPressedColor)))
    (this as TextView).setTextColor(colorStateList)
}

/**
 * 設定按鈕 被按住的顏色背景
 *
 * @param unPressedColor 未按下的顏色背景 R.color.color1
 * @param pressedColor   按下的顏色 R.color.color 0為不給
 */
fun View.setTextColor(unPressedColor: Int, pressedColor: Int) {
    val context = this.context
    if (pressedColor == 0) {
        (this as TextView).setTextColor(context.resources.getColor(unPressedColor))
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var unPress = ContextCompat.getColor(this.context, unPressedColor)
        var pressed = ContextCompat.getColor(this.context, pressedColor)
        var colorStateList = ColorStateList(arrayOf(intArrayOf(R.attr.state_pressed)
                , intArrayOf(R.attr.state_focused)
                , intArrayOf(R.attr.state_checked), intArrayOf())
                , intArrayOf(pressed
                , pressed
                , pressed
                , unPress))
        (this as TextView).setTextColor(colorStateList)
    } else {
        var colorStateList = ColorStateList(arrayOf(intArrayOf(R.attr.state_pressed), intArrayOf(R.attr.state_focused), intArrayOf(R.attr.state_checked), intArrayOf()), intArrayOf(context.resources.getColor(pressedColor), context.resources.getColor(pressedColor), context.resources.getColor(pressedColor), context.resources.getColor(unPressedColor)))
        (this as TextView).setTextColor(colorStateList)
    }


}

/**
 * 設定按鈕 被按住的顏色背景
 *
 * @param unPressedColor 未按下的顏色背景 R.color.color1
 * @param pressedColor   按下的顏色 R.color.color 0為不給
 */
fun View.setPressedTextColor(unPressedColor: String, pressedColor: String) {
    val colorStateList = ColorStateList(arrayOf(intArrayOf(R.attr.state_pressed), intArrayOf(R.attr.state_focused), intArrayOf(R.attr.state_checked), intArrayOf())
            , intArrayOf(Color.parseColor(pressedColor)
            , Color.parseColor(pressedColor)
            , Color.parseColor(pressedColor)
            , Color.parseColor(unPressedColor)))
    (this as TextView).setTextColor(colorStateList)
}

fun <T> RecyclerView.addSwipeLeftDelete(arrlist: ArrayList<T>, onItenDeleted: ((Int) -> Unit)):ItemTouchHelper{
    var itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(
                0,
                ItemTouchHelper.LEFT
            )
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            TODO("Not yet implemented")
        }
        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            // Set the minimum swipe distance to 50% of the RecyclerView width
            return 0.5f // or whatever value suits your needs
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition
            onItenDeleted?.let { it -> it(position) }
//            arrlist.removeAt(position)
            this@addSwipeLeftDelete.adapter!!.notifyItemRemoved(position)
            this@addSwipeLeftDelete.adapter!!.notifyItemRangeChanged(position, arrlist.size - position)

            when (R.attr.direction) {
                ItemTouchHelper.LEFT -> {

//                    arrlist.removeAt(position) //移除資料
//                    this@addSwipeLeftDelete.adapter!!.notifyItemRemoved(position) //告知配適器資料有更換
//                    if (position != arrlist.size) {
//                        //開始改變的位置和總共被影響的位置數量，相當重要否則資料的position不會更改
//                        //刮號內是(改變的位置,受影響的數量)
//                        this@addSwipeLeftDelete.adapter!!.notifyItemRangeChanged(
//                            position,
//                            arrlist.size - position
//                        )
//                    }
                }
            }
        }
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive
            if (isCanceled) {
                val clearPaint =
                    Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
                c?.drawRect(
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    clearPaint
                )
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                return
            }
            //刪除

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Here, if dX > 0 then swiping right.
                // If dX < 0 then swiping left.
                // If dX == 0 then at at start position.
//                if (dX < 0) {
                if (dX < 0) {

                    val background = ColorDrawable()
                    val backgroundColor = Color.parseColor("#3F5D95")
                    // Draw the red delete background
                    background.color = backgroundColor
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    background.draw(c)
                    val deleteIcon = ContextCompat.getDrawable(recyclerView.context, com.realtouchapp.admitted.R.drawable.vd_ic_remove)
                    val intrinsicWidth = deleteIcon!!.intrinsicWidth
                    val intrinsicHeight = deleteIcon!!.intrinsicHeight
                    // Calculate position of delete icon
                    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop + intrinsicHeight

                    // Draw the delete icon
                    deleteIcon!!.setBounds(
                        deleteIconLeft,
                        deleteIconTop,
                        deleteIconRight,
                        deleteIconBottom
                    )
                    deleteIcon!!.draw(c)
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                } else {
                }
            } else {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                );
            }
        }
    })

    itemTouchHelper.attachToRecyclerView(this)
    return itemTouchHelper;

}
fun Context.setSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, arrayRes:Int){
    var tmppArrayList=ArrayList<String>()
    tmppArrayList.add("")
    tmppArrayList.addAll(resources.getStringArray(arrayRes))
    val spinnerArrayAdapter= SpinnerArrayAdapter(tv.context,tmppArrayList.toList())
    sp.adapter = spinnerArrayAdapter
    sp.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent:AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (!tmppArrayList[pos].equals("")){
                    tv.text = tmppArrayList[pos]
                }
                closeSoftIntPut(tv)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                closeSoftIntPut(tv)
            }
        }
    sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(tv.context,0.068f)
    //設定初始值不為0 才能觸發按相同位置
        sp.setSelection(-1,false)
    tv.text = ""
}
fun Context.setSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, arrayRes:Int,meathod:(Int)->Unit){
    var tmppArrayList=ArrayList<String>()
    tmppArrayList.add("")
    tmppArrayList.addAll(resources.getStringArray(arrayRes))
    val spinnerArrayAdapter= SpinnerArrayAdapter(tv.context,tmppArrayList.toList())
    sp.adapter = spinnerArrayAdapter
    sp.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent:AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (!tmppArrayList[pos].equals("")){
                    tv.text = tmppArrayList[pos]
                    meathod(pos)
                }
                closeSoftIntPut(tv)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                closeSoftIntPut(tv)
            }
        }
    sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(tv.context,0.068f)
    //設定初始值不為0 才能觸發按相同位置
        sp.setSelection(-1,false)
    tv.text = ""
}

fun setSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, array:ArrayList<String>){
    var tmppArrayList=array
    tmppArrayList.add(0,"")
    val spinnerArrayAdapter= SpinnerArrayAdapter(tv.context,tmppArrayList.toList())
    sp.adapter = spinnerArrayAdapter

    sp.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, iew: View?, pos: Int, id: Long) {
                if (!tmppArrayList[pos].equals("")){
                    tv.text = tmppArrayList[pos]
                }
                closeSoftIntPut(tv)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                closeSoftIntPut(tv)
            }
        }
    sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(tv.context,0.068f)
    //設定初始值不為0 才能觸發按相同位置
    sp.setSelection(-1,false)
    tv.text = ""
}

private fun animateExpand(arrow: View) {
    val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)

    rotate.duration = 300
    rotate.fillAfter = true
    arrow.setAnimation(rotate)
}

private fun animateCollapse(arrow: View) {
    val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
    rotate.duration = 300
    rotate.fillAfter = true
    arrow.setAnimation(rotate)

}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}