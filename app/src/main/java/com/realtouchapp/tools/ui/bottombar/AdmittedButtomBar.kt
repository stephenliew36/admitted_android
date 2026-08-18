package com.realtouchapp.tools.ui.bottombar;

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.realtouchapp.admitted.R
import com.realtouchapp.tools.utils.UserInterfaceTool

/*
    作者: RobertChou
    時間: 2018/7/12 下午 09:03
    信箱: didi31139@gmail.com
    說明:
*/
class AdmittedButtomBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val mInterpolator = AccelerateDecelerateInterpolator()
    var isVisible = true
        private set

    private var mTabLayout: LinearLayout? = null

//    private var mTabParams: LinearLayout.LayoutParams? = null
    var currentItemPosition = 0
        private set
    private var mListener: OnTabSelectedListener? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = LinearLayout.VERTICAL

        //        ImageView shadowView = new ImageView(context);
        //        shadowView.setBackgroundResource(R.drawable.actionbar_shadow_up);
        //        addView(shadowView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTabLayout = LinearLayout(context)
        mTabLayout!!.setBackgroundResource(R.drawable.bg_frame_f2f4f6)
        mTabLayout!!.orientation = LinearLayout.HORIZONTAL
        addView(mTabLayout, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))


    }

    fun addItem(tab: BottomBarTab): AdmittedButtomBar {
        tab.setOnClickListener(OnClickListener {

            if (mListener == null) return@OnClickListener

            val pos = tab.tabPosition
            if (currentItemPosition == pos) {
                mListener!!.onTabReselected(pos)
            } else {
                mListener!!.onTabSelected(pos, currentItemPosition)
                tab.isSelected = true
                mListener!!.onTabUnselected(currentItemPosition)
                mTabLayout!!.getChildAt(currentItemPosition).isSelected = false
                currentItemPosition = pos
            }
        })
        var height = UserInterfaceTool().getScreenWidthByRatio(context,0.2f)
        var mTabParams = LinearLayout.LayoutParams(0, height)
        tab.tabPosition = mTabLayout!!.childCount
        if (mTabLayout!!.childCount == 10){
            //設定中間的位置
            mTabParams = LinearLayout.LayoutParams(0,height *85/100)
            mTabParams!!.weight = 3f
//            mTabParams.topMargin = 5
        }else{
            mTabParams!!.weight = 2f
//            mTabParams.topMargin = 20
        }

        tab.layoutParams = mTabParams
        mTabLayout!!.addView(tab)
        return this
    }

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener) {
        mListener = onTabSelectedListener
    }

    fun setCurrentItem(position: Int) {
        mTabLayout!!.post { mTabLayout!!.getChildAt(position).performClick() }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState!!, currentItemPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)

        if (currentItemPosition != ss.position) {
            mTabLayout!!.getChildAt(currentItemPosition).isSelected = false
            mTabLayout!!.getChildAt(ss.position).isSelected = true
        }
        currentItemPosition = ss.position
    }

    @JvmOverloads
    fun hide(anim: Boolean = true) {
        toggle(false, anim, false)
    }

    @JvmOverloads
    fun show(anim: Boolean = true) {
        toggle(true, anim, false)
    }

    private fun toggle(visible: Boolean, animate: Boolean, force: Boolean) {
        if (isVisible != visible || force) {
            isVisible = visible
            val height = height
            if (height == 0 && !force) {
                val vto = viewTreeObserver
                if (vto.isAlive) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val currentVto = viewTreeObserver
                            if (currentVto.isAlive) {
                                currentVto.removeOnPreDrawListener(this)
                            }
                            toggle(visible, animate, true)
                            return true
                        }
                    })
                    return
                }
            }
            val translationY = if (visible) 0 else height
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS.toLong())
                        .translationY(translationY.toFloat())
            } else {
                ViewCompat.setTranslationY(this, translationY.toFloat())
            }
        }
    }

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int, prePosition: Int)

        fun onTabUnselected(position: Int)

        fun onTabReselected(position: Int)
    }

    internal class SavedState : View.BaseSavedState {
        var position: Int = 0

        constructor(source: Parcel) : super(source) {
            position = source.readInt()
        }

        constructor(superState: Parcelable, position: Int) : super(superState) {
            this.position = position
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        private val TRANSLATE_DURATION_MILLIS = 200
    }
}
