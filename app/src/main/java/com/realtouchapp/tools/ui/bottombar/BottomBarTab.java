package com.realtouchapp.tools.ui.bottombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.realtouchapp.admitted.R;


/**
 * Created by Robert on 2016/11/8.
 */

public class BottomBarTab extends FrameLayout {
    private ImageView mIcon;
    private TextView mTvTitle;
    private Context mContext;
    private int mTabPosition = -1;
    int select_bg = R.color.tab_select;
    int unselect_bg = R.color.tab_unselect;
    Boolean openColorFilter = true;
    public BottomBarTab(Context context, @DrawableRes int icon, CharSequence title,Boolean openColorFilter) {
        this(context, null, icon, title);
        this.openColorFilter = openColorFilter;
    }
    public BottomBarTab(Context context, @DrawableRes int icon, CharSequence title) {
        this(context, null, icon, title);

    }
    public BottomBarTab(Context context, AttributeSet attrs, int icon, CharSequence title) {
        this(context, attrs, 0, icon, title);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon, CharSequence title) {
        super(context, attrs, defStyleAttr);
        init(context, icon, title);
    }

    private void init(Context context, int icon, CharSequence title) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();

        LinearLayout lLContainer = new LinearLayout(context);
        lLContainer.setOrientation(LinearLayout.VERTICAL);
        lLContainer.setGravity(Gravity.CENTER);
        LayoutParams paramsContainer = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsContainer.gravity = Gravity.CENTER;
        lLContainer.setLayoutParams(paramsContainer);

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.CENTER_VERTICAL);
//        params.topMargin =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

        mIcon.setImageResource(icon);
//        mIcon.setLayoutParams(params);
//        mIcon.setColorFilter(ContextCompat.getColor(context, R.color.tab_unselect));
        lLContainer.addView(mIcon);

        mTvTitle = new TextView(context);
        mTvTitle.setText(title);
        mTvTitle.setGravity(Gravity.CENTER);
//        RelativeLayout.LayoutParams paramsTv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsTv.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        paramsTv.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        mTvTitle.setTextSize(12);
        mTvTitle.setTextColor(ContextCompat.getColor(context, R.color.tab_unselect));
//        mTvTitle.setLayoutParams(paramsTv);
        lLContainer.addView(mTvTitle);

        addView(lLContainer);
    }

    public void setTabBg(int select_bg, int unselect_bg) {
        this.select_bg = select_bg;
        this.unselect_bg = unselect_bg;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (openColorFilter == false){
            return;
        }
        if (selected) {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, select_bg));
            mTvTitle.setTextColor(ContextCompat.getColor(mContext, select_bg));

        } else {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, unselect_bg));
            mTvTitle.setTextColor(ContextCompat.getColor(mContext, unselect_bg));
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }
}
