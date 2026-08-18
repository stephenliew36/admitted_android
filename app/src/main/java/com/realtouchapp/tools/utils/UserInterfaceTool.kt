package com.realtouchapp.tools.utils

import android.R
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class UserInterfaceTool {

    fun getScreenWidth(context: Context):Int{
        val screenWidth :Int
        val wm = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            screenWidth = windowMetrics.bounds.width()
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            screenWidth = displayMetrics.widthPixels
        }
        return screenWidth
    }

    fun getScreenHeight(context: Context):Int{
        val screenHeight :Int
        val wm = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            screenHeight = windowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            screenHeight = displayMetrics.heightPixels
        }
        return screenHeight
    }
    //扣掉Statusbar 和 navBar 的高度
    fun getScreenHeightWithoutBar(context: Context):Int{
        var screenHeight :Int
        val wm = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            screenHeight = windowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            screenHeight = displayMetrics.heightPixels
        }
        var navBarHeight: Int =0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
//        Log.d("nh", "nh"+navBarHeight)
        var statusBarHeight: Int =0
        val resourceId2= context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId2 > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }

        return screenHeight-navBarHeight-statusBarHeight
    }

    fun getScreenHeightWithoutBarByRatio(context: Context, ratio:Float): Int {
        val screenHeight = getScreenHeightWithoutBar(context)
        return (screenHeight*ratio).toInt()
    }
    fun getScreenHeightByRatio(context: Context, ratio:Float): Int {
        val screenHeight = getScreenHeightWithoutBar(context)
        return (screenHeight*ratio).toInt()
    }

    fun getScreenWidthByRatio(context: Context, ratio:Float): Int {
        val screenWidth = getScreenWidth(context)
        return (screenWidth*ratio).toInt()
    }
    /**
     * 設定 view的長寬 單位為畫素(pixel)
     *
     * @param view
     * @param w
     * @param h
     * @author Wang / Robert
     * @date 2015/5/8 下午3:13:42
     * @version
     */
    fun setViewSize(view: View, w: Int, h: Int) {
        try {
            view.layoutParams.width = w
            view.layoutParams.height = h
        } catch (e: Exception) {
            //如果prams不存在 則重新建立
            val params = ViewGroup.LayoutParams(w, h)
            params.width = w
            params.height = h
            view.layoutParams = params
        }

    }

    fun setViewSizeHeight(view: View, h: Int) {
        try {
            view.layoutParams.height = h
        } catch (e: Exception) {
            //如果prams不存在 則重新建立
            val params = ViewGroup.LayoutParams(view.layoutParams.width, h)
            params.height = h
            view.layoutParams = params
        }

    }
    fun setViewSizeWidth(view: View, w: Int) {
        try {
            view.layoutParams.width = w
        } catch (e: Exception) {
            //如果prams不存在 則重新建立
            val params = ViewGroup.LayoutParams(w, view.layoutParams.height)
            params.width = w
            view.layoutParams = params
        }

    }
    fun setViewSizeByRatioWW(view: View, w: Float, h: Float) {
        val screenWidth = getScreenWidth(view.context)
        
        setViewSize(view, (screenWidth * w).toInt(), (screenWidth * h).toInt())
    }

    fun setViewSizeByRatioWH(view: View, w: Float, h: Float) {
        val screenWidth = getScreenWidth(view.context)
        val screenHeight = getScreenHeightWithoutBar(view.context)
        setViewSize(view, (screenWidth * w).toInt(), (screenHeight * h).toInt())
    }

    fun setViewSizeByRatioHH(view: View, w: Float, h: Float) {
        val screenHeight = getScreenHeightWithoutBar(view.context)
        setViewSize(view, (screenHeight * w).toInt(), (screenHeight * h).toInt())
    }

    fun setViewSizeByRatioHW(view: View, w: Float, h: Float) {
        val screenWidth = getScreenWidth(view.context)
        val screenHeight = getScreenHeightWithoutBar(view.context)
        setViewSize(view, (screenHeight * w).toInt(), (screenWidth * h).toInt())
    }


    /**
     * 設定 view的長寬 單位為dp
     *
     * @param view
     * @param w
     * @param h
     * @author Wang / Robert
     * @date 2015/5/8 下午3:13:42
     * @version
     */
    fun setViewSizeByDpUnit(view: View, w: Int, h: Int) {
        setViewSize(view, getPixelFromDpByDevice(view.context, w), getPixelFromDpByDevice(view.context, h))
    }

    /**
     *
     * 設定物件間距  單位為畫素(pixel)
     * 上層類別須為 RelativeLayout or LinearLayout
     *
     * @author Wang / Robert Chou didi31139@gmail.com
     * @date 2015/5/26 下午3:25:33
     * @version
     */
    fun setMargin(view: View, leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
        val params = view.layoutParams
        if (params is LinearLayout.LayoutParams) {
            params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
        } else if (params is RelativeLayout.LayoutParams) {
            params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
        } else if (params is ConstraintLayout.LayoutParams) {
            params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
        }
        view.layoutParams = params
    }

    fun setMarginLeft(view: View, leftMargin:Int) {
        val params = view.layoutParams
        if (params is LinearLayout.LayoutParams) {
            params.leftMargin = leftMargin
        } else if (params is RelativeLayout.LayoutParams) {
            params.leftMargin = leftMargin
        } else if (params is ConstraintLayout.LayoutParams) {
            params.leftMargin = leftMargin
        }
        view.layoutParams = params
    }

    fun setMarginTop(view: View, topMargin:Int) {
        val params = view.layoutParams
        if (params is LinearLayout.LayoutParams) {
            params.topMargin = topMargin
        } else if (params is RelativeLayout.LayoutParams) {
            params.topMargin = topMargin
        } else if (params is ConstraintLayout.LayoutParams) {
            params.topMargin = topMargin
        }
        view.layoutParams = params
    }
    fun setMarginRight(view: View, rightMargin:Int) {
        val params = view.layoutParams
        if (params is LinearLayout.LayoutParams) {
            params.rightMargin = rightMargin
        } else if (params is RelativeLayout.LayoutParams) {
            params.rightMargin = rightMargin
        } else if (params is ConstraintLayout.LayoutParams) {
            params.rightMargin = rightMargin
        }
        view.layoutParams = params
    }
    fun setMarginBottom(view: View, bottomMargin:Int) {
        val params = view.layoutParams
        if (params is LinearLayout.LayoutParams) {
            params.bottomMargin = bottomMargin
        } else if (params is RelativeLayout.LayoutParams) {
            params.bottomMargin = bottomMargin
        } else if (params is ConstraintLayout.LayoutParams) {
            params.bottomMargin = bottomMargin
        }
        view.layoutParams = params
    }
    /**
     *
     * 設定物件間距  單位為畫素(pixel)
     * 上層類別須為 RelativeLayout or LinearLayout
     *
     * @author Wang / Robert Chou didi31139@gmail.com
     * @date 2015/5/26 下午3:25:33
     * @version
     */
    fun setMarginByDpUnit(view: View, leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
        setMargin(view, getPixelFromDpByDevice(view.context, leftMargin), getPixelFromDpByDevice(view.context, topMargin), getPixelFromDpByDevice(view.context, rightMargin), getPixelFromDpByDevice(view.context, bottomMargin))
    }

    /**
     *
     * 根據版本加入背景:
     *
     * @param rootView 物件存在的集合
     * @param viewID   要尋找的ViewID ex: R.id.viewname
     * @param drawable 要設定的背景圖片
     * @author Robert Chou didi31139@gmail.com
     * @date 2015/5/26 下午2:48:26
     * @version
     */
    fun setBackground(view: View, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     *
     *  設定View的文字大小(以360DP寬的比例)
     *
     * @param sp 為當前的sp整數下去做轉換
     * @author Robert Chou didi31139@gmail.com
     * @date 2014/11/19 上午12:07:08
     */
    fun setTextSize(view: View, sp: Int) {
        val displayMetrics = view.context.resources.displayMetrics
        val realSpSize = ((sp * displayMetrics.widthPixels).toFloat() / displayMetrics.density / 360f).toInt()
        if (view is TextView) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        } else if (view is Button) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        } else if (view is EditText) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        } else {
            (view as TextView).setTextSize(TypedValue.COMPLEX_UNIT_SP, realSpSize.toFloat())
        }
    }

    /**
     * 取得換算的文字大小單位pixel
     */
    fun getTextSize(context: Context, sp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return ((sp * displayMetrics.widthPixels).toFloat() / displayMetrics.density / 360f).toInt()
    }

    /**
     *
     * 輸入DP單位數值 根據裝置動態 回傳像素:
     *
     * @param dpSize 整數 單位為dp
     * @return dp根據裝置動態計算 回傳pixel
     * @author Robert Chou didi31139@gmail.com
     * @date 2015/6/17 下午5:25:39
     * @version
     */
    fun getPixelFromDpByDevice(context: Context, dpSize: Int): Int {
        var pixel = 0
        val displayMetrics = context.resources.displayMetrics
        val realSpSize = ((dpSize * displayMetrics.widthPixels).toFloat()
                / displayMetrics.density / 360f).toInt()
        pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                realSpSize.toFloat(), context.resources.displayMetrics).toInt()
        return pixel
    }

    /**
     * 設定 壓下的圖片切換效果
     *
     * @param unPressedDrawable 未按下的圖片 R.drawable.image
     * @param pressedDrawable   未按下的圖片 R.drawable.pressedimage
     */
    fun setPressedImage(view: View, unPressedDrawable: Drawable, pressedDrawable: Drawable?) {
        if (pressedDrawable == null) {
            setBackground(view, unPressedDrawable)
            return
        }
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_focused), pressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_checked), pressedDrawable)
        states.addState(intArrayOf(), unPressedDrawable)
        if (view is Button) {
            setBackground(view, states)
        } else {
            (view as ImageView).setImageDrawable(states)
        }

    }

    /**
     * 設定 壓下的圖片切換效果
     *
     * @param unPressedDrawableID 未按下的圖片 R.drawable.image
     * @param pressedDrawableID   未按下的圖片 R.drawable.pressedimage
     */
    fun setPressedImage(view: View, unPressedDrawableID: Int, pressedDrawableID: Int) {
        
        setPressedImage(view, ContextCompat.getDrawable(view.context,unPressedDrawableID)!!, ContextCompat.getDrawable(view.context,pressedDrawableID)!!)
    }

    /**
     * 設定 壓下的圖片切換效果
     *
     * @param unPressedDrawable 未按下的圖片 R.drawable.image
     * @param pressedDrawable   未按下的圖片 R.drawable.pressedimage
     */
    fun setPressedBackground(view: View, unPressedDrawable: Drawable, pressedDrawable: Drawable?) {
        if (pressedDrawable == null) {
            setBackground(view, unPressedDrawable)
            return
        }
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_focused), pressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_checked), pressedDrawable)
        states.addState(intArrayOf(), unPressedDrawable)
        setBackground(view, states)
    }

    /**
     * 設定 壓下的圖片切換效果
     *
     * @param unPressedDrawable 未按下的圖片 R.drawable.image
     * @param pressedDrawable   未按下的圖片 R.drawable.pressedimage
     */
    fun setPressedBackground(view: View, unPressedDrawableID: Int, pressedDrawableID: Int) {
        
        setPressedBackground(view, ContextCompat.getDrawable(view.context,unPressedDrawableID)!!, ContextCompat.getDrawable(view.context,pressedDrawableID)!!)
    }

    /**
     * check box 狀態設定
     *
     * @param basedrawable    未按下的圖片 R.drawable.image
     * @param presseddrawable 未按下的圖片 R.drawable.pressedimage 0為不給
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setCheckDrawable(context: Context, view: View, basedrawable: Int, checkeddrawable: Int) {
        if (checkeddrawable == 0) {
            view.setBackgroundResource(basedrawable)
            return
        }
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_checkable), context.resources.getDrawable(basedrawable))
        states.addState(intArrayOf(android.R.attr.state_checked), context.resources.getDrawable(checkeddrawable))
        states.addState(intArrayOf(), context.resources.getDrawable(basedrawable))

        if (view is CheckBox) {
            view.background = states
        } else if (Build.VERSION.SDK_INT >= 16) {
            view.background = states
        } else {
            view.setBackgroundDrawable(states)
        }
    }

    /**
     * 設定按鈕 被按住的顏色背景
     *
     * @param unPressedColor 未按下的顏色背景 R.color.color1
     * @param pressedColor   按下的顏色 R.color.color 0為不給
     */
    fun setPressedBackgroundColor(view: View, unPressedColor: Int, pressedColor: Int) {
        if (pressedColor == 0) {
            view.setBackgroundColor(unPressedColor)
            return
        }
        val unPressedcolorDrawable = ColorDrawable(view.context.resources.getColor(unPressedColor))
        val pressedcolorDrawable = ColorDrawable(view.context.resources.getColor(pressedColor))
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), pressedcolorDrawable)
        states.addState(intArrayOf(android.R.attr.state_focused), pressedcolorDrawable)
        states.addState(intArrayOf(android.R.attr.state_checked), pressedcolorDrawable)
        states.addState(intArrayOf(), unPressedcolorDrawable)
        setBackground(view, states)
    }


    /**
     * 設定按鈕 被按住的顏色背景
     *
     * @param unPressedColor 未按下的顏色背景 R.color.color1
     * @param pressedColor   按下的顏色 R.color.color 0為不給
     */
    fun setPressedTextColor(view: View, unPressedColor: Int, pressedColor: Int) {
        val context = view.context
        if (pressedColor == 0) {
            (view as TextView).setTextColor(context.resources.getColor(unPressedColor))
            return
        }
        val colorStateList = ColorStateList(arrayOf(intArrayOf(R.attr.state_pressed), intArrayOf(R.attr.state_focused), intArrayOf(R.attr.state_checked), intArrayOf()), intArrayOf(context.resources.getColor(pressedColor), context.resources.getColor(pressedColor), context.resources.getColor(pressedColor), context.resources.getColor(unPressedColor)))
        (view as TextView).setTextColor(colorStateList)
    }

    /**
     * 設定Tab按鈕 被按住的顏色背景
     *
     * @param unPressedColor 未按下的顏色背景 R.color.color1
     * @param pressedColor   按下的顏色 R.color.color 0為不給
     */
    fun setTabPressedImage(view: View, unPressedDrawable: Drawable, pressedDrawable: Drawable?) {
        if (pressedDrawable == null) {
            setBackground(view, unPressedDrawable)
            return
        }
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), unPressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_focused), unPressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_checked), unPressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_selected), pressedDrawable)
        states.addState(intArrayOf(), unPressedDrawable)
        if (view is Button) {
            setBackground(view, states)
        } else {
            (view as ImageView).setImageDrawable(states)
        }

    }

    /**
     * 設定Tab按鈕 被按住的顏色背景
     *
     * @param unPressedColor 未按下的顏色背景 R.color.color1
     * @param pressedColor   按下的顏色 R.color.color 0為不給
     */
    fun setTabPressedImage(view: View, unPressedDrawableID: Int, pressedDrawableID: Int) {
        val unPressedDrawable = view.resources.getDrawable(unPressedDrawableID)
        val pressedDrawable = view.resources.getDrawable(pressedDrawableID)
        if (pressedDrawable == null) {
            setBackground(view, unPressedDrawable)
            return
        }
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), unPressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_focused), unPressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_checked), unPressedDrawable)
        states.addState(intArrayOf(android.R.attr.state_selected), pressedDrawable)
        states.addState(intArrayOf(), unPressedDrawable)
        if (view is Button) {
            setBackground(view, states)
        } else {
            (view as ImageView).setImageDrawable(states)
        }

    }

    /**
     * 設定Tab按鈕 被按住的顏色背景
     *
     * @param unPressedColor 未按下的顏色背景 R.color.color1
     * @param pressedColor   按下的顏色 R.color.color 0為不給
     */
    fun setTabPressedTextColor(view: View, unPressedColor: Int, pressedColor: Int) {
        val context = view.context
        if (pressedColor == 0) {
            (view as TextView).setTextColor(context.resources.getColor(unPressedColor))
            return
        }
        val colorStateList = ColorStateList(arrayOf(intArrayOf(R.attr.state_pressed), intArrayOf(R.attr.state_focused), intArrayOf(R.attr.state_checked), intArrayOf(R.attr.state_selected), intArrayOf()), intArrayOf(context.resources.getColor(unPressedColor), context.resources.getColor(unPressedColor), context.resources.getColor(unPressedColor), context.resources.getColor(pressedColor), context.resources.getColor(unPressedColor)))
        (view as TextView).setTextColor(colorStateList)
    }

    fun setTextView(textView: TextView, width: Int, height: Int, sp: Int, stringid: Int, unPressedColor: Int, pressedColor: Int) {
        setViewSize(textView, width, height)
        setTextSize(textView, sp)
        textView.text = textView.context.getString(stringid)
        setPressedTextColor(textView, unPressedColor, pressedColor)
    }

    fun setTextView(textView: TextView, width: Int, height: Int, sp: Int, string: String, unPressedColor: Int, pressedColor: Int) {
        setViewSize(textView, width, height)
        setTextSize(textView, sp)
        textView.text = string
        setPressedTextColor(textView, unPressedColor, pressedColor)
    }
}