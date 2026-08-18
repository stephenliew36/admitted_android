package com.realtouchapp.admitted.ext

import android.content.Context
import android.util.Log
import android.widget.Toast

fun printLogError(title: String, content: String) {
    Log.e(title, content)
}
val isTesting = false
fun testAction(meathod: () -> Unit) {
    if (isTesting){
        meathod()
    }

}

fun testPrint(context: Context,msg: String){
    if (isTesting){
        var TAG ="Test:"+ context.javaClass.simpleName

        Log.d(TAG, msg)
    }
}
fun testPrint(TAG:String,msg: String){
    if (isTesting){
        Log.d("Test:"+TAG, msg)
    }
}
fun testToast(context: Context,txt:String){
    if (isTesting){
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show()
    }
}
