package com.realtouchapp.tools.ext

import android.content.Context
import android.util.TypedValue

// Reference: https://stackoverflow.com/questions/35513636/multiple-variable-let-in-kotlin

fun <T: Any, R: Any> Collection<T?>.whenAllNotNull(
    block: (List<T>) -> R,
    `else`: (() -> R)? = null
) {
    if (this.all { it != null }) {
        block(this.filterNotNull()) // or do unsafe cast to non null collection
    } else {
        `else`?.invoke()
    }
}

fun <T: Any, R: Any> Collection<T?>.whenAnyNotNull(
    block: (List<T>) -> R,
    `else`: ((List<T?>) -> R)? = null
) {
    val notNulls = this.filterNotNull()
    if (this.any { it != null }) {
        block(notNulls)
    } else {
        `else`?.invoke(this.filterNot { notNulls.contains(it) })
    }
}

// 将dp转换为px
fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    ).toInt()
}

// 将sp转换为px
fun Context.spToPx(sp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    ).toInt()
}
