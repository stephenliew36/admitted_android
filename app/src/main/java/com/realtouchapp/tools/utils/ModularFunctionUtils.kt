package com.realtouchapp.tools.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.format.Time
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
    作者: RobertChou
    時間: 2018/8/16 上午 01:25
    信箱: didi31139@gmail.com
    說明:
*/
object ModularFunctionUtils {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun openNAVIGATIONOrClose(window: Window, openOrNot: Boolean) {
        val v = window.decorView
        //		if (android.os.Build.VERSION.SDK_INT>19) {
        var viewFlag = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        if ((!openOrNot)!!) {
            viewFlag = viewFlag or (View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
        v.systemUiVisibility = viewFlag
        //		}
    }

    /**
     * 關閉彈出的軟鍵盤
     */
    fun CloseSoftInput(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)

    }

    /**
     * 產生亂數 最小到最大
     */
    fun getRandom(min: Int, max: Int): Int {
        val seed = System.currentTimeMillis()
        val r = Random()
        r.setSeed(seed)

        return min + r.nextInt(max - min + 1)
    }

    /**
     * 產生亂數 最小到最大
     */
    fun getRandom(min: Int, max: Int, i: Int): Int {
        val seed = System.currentTimeMillis() + i
        val r = Random()
        r.setSeed(seed)

        return min + r.nextInt(max - min + 1)
    }

    /**
     * 使用方式:
     * <br></br>x=0 return 20120803150305;
     * <br></br> x=1 return 2012-08-03 15:03;
     * <br></br>x=2 return2012-08-03;
     * <br></br>x=3 return 15:03 ,
     * <br></br>x=4 return 20120803
     * <br></br>x=5 return 2012-08-03 15:03:05
     */
    fun getTime(x: Int): String {
        val t = Time() // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow() // 取得系统时间。

        val time_int = t.format2445().subSequence(0, 8).toString() + t.format2445().subSequence(9, 15).toString()
        val time_hour = (t.format2445().subSequence(9, 11).toString()
                + ":" + t.format2445().subSequence(11, 13).toString())
        val time_format = t.format3339(true) + " " + time_hour
        val time_date = t.format3339(true)
        when (x) {
            0 -> return time_int
            1 -> return time_format
            2 -> return time_date
            3 -> return time_hour
            4 -> return t.format2445().subSequence(0, 8).toString()
            5 -> return time_format + ":" + t.format2445().subSequence(13, 15).toString()
            else -> return " "
        }
    }

    /**
     *
     * 輸入日期 回傳時間格式字串
     *
     * @return 2014-11-22 12:34:56
     * @author Robert Chou didi31139@gmail.com
     * @date 2014/12/4 上午4:32:51
     */
    fun returnTimeString(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): String {
        var month = month
        var time = ""
        time = "$year-"
        month = month + 1
        if (month < 10) {
            time = time + "0" + month + "-"
        } else {
            time = "$time$month-"
        }
        if (day < 10) {
            time = time + "0" + day + " "
        } else {
            time = "$time$day "
        }
        if (hour < 10) {
            time = time + "0" + hour + ":"
        } else {
            time = "$time$hour:"
        }
        if (minute < 10) {
            time = time + "0" + minute + ":"
        } else {
            time = "$time$minute:"
        }
        if (second < 10) {
            time = time + "0" + second
        } else {
            time = time + second
        }
        return time
    }

    /**
     *
     * 時間字串轉換Calendar
     *
     * @param timeString 輸入2014-12-22 12:23:56
     * @return 格式為timeString 的Calendar
     * @author Robert Chou didi31139@gmail.com
     * @date 2014/12/4 上午4:48:02
     */
    fun timeStringToCalendar(timeString: String): Calendar {
        val calendar = Calendar.getInstance()

        var year = ""
        if (timeString.length > 4) {
            year = timeString.substring(0, 4)
        } else {
            year = "0"
        }
        var month = ""
        if (timeString.length > 7) {
            month = timeString.substring(5, 7)
        } else {
            month = "0"
        }
        var day = ""
        if (timeString.length > 10) {
            day = timeString.substring(8, 10)
        } else {
            day = "0"
        }
        var hour = ""
        if (timeString.length > 13) {
            hour = timeString.substring(11, 13)
        } else {
            hour = "0"
        }
        var minute = ""
        if (timeString.length > 16) {
            minute = timeString.substring(14, 16)
        } else {
            minute = "0"
        }
        var sceond = ""
        if (timeString.length > 19) {
            sceond = timeString.substring(17, 19)
        } else {
            sceond = "0"
        }
        var monthint = Integer.valueOf(month)
        monthint = monthint - 1
        calendar.set(Integer.valueOf(year), monthint, Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(sceond))
        return calendar
    }

    fun timeInMillisToTimeString(TimeInMillis: Long): String {
        var time = ""
        val day = TimeInMillis / (3600 * 24 * 1000)
        time = day.toString() + "D"
        //		相差小時：
        val hour = (TimeInMillis - day * 3600 * 24 * 1000) / (3600 * 1000)
        if (hour < 10) {
            time = time + "0" + hour + "h"
        } else {
            time = time + hour + "h"
        }
        //		相差分鐘：
        val minute = (TimeInMillis - day * 3600 * 24 * 1000 - hour * 3600 * 1000) / (60 * 1000)
        if (minute < 10) {
            time = time + "0" + minute + "m"
        } else {
            time = time + minute + "m"
        }
        //		相差秒：
        val second = (TimeInMillis - day * 3600 * 24 * 1000 - hour * 3600 * 1000 - minute * 60 * 1000) / 1000
        if (second < 10) {
            time = time + "0" + second + "s"
        } else {
            time = time + second + "s"
        }
        return time
    }

    /**
     * 調整ScrollView
     */
    fun updateListViewHeight(myListView: ListView, baseAdapter: BaseAdapter) {
        val myListAdapter = myListView.adapter as BaseAdapter ?: return
//get listview height
        var totalHeight = 0
        val adapterCount = myListAdapter.count
        for (size in 0 until adapterCount) {
            val listItem = myListAdapter.getView(size, null, myListView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        //Change Height of ListView
        val params = myListView.layoutParams
        params.height = totalHeight + myListView.dividerHeight * (adapterCount - 1)
        myListView.layoutParams = params
    }

    /**
     * 刪除資料夾
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }

        // The directory is now empty so delete it
        return dir!!.delete()
    }

    /**
     * 動畫
     *
     * @param start 當前的VIEW Gone
     * @param end   結束後要顯示的VIEW
     * @param fromx 起始X座標
     * @param tox   結束X座標
     * @param fromy 起始Y座標
     * @param toy   結束Y座標
     */
    fun animation(start: View, end: View, fromx: Float,
                  tox: Float, fromy: Float, toy: Float) {

        val changevisibility = object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {

                start.visibility = View.GONE
                // end.setVisibility(View.VISIBLE);
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {
                end.visibility = View.VISIBLE
            }
        }
        //		AnimationListener AnimationY = new AnimationListener() {
        //			@Override
        //			public void onAnimationEnd(Animation animation) {
        //
        //				start.setVisibility(View.GONE);
        //				// end.setVisibility(View.VISIBLE);
        //			}
        //
        //			@Override
        //			public void onAnimationRepeat(Animation animation) {
        //			}
        //
        //			@Override
        //			public void onAnimationStart(Animation animation) {
        //				start.setVisibility(View.GONE);
        //			}
        //		};
        val out = object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {

                start.visibility = View.GONE

            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationStart(animation: Animation) {
                start.visibility = View.GONE
                end.visibility = View.VISIBLE
            }
        }
        // slide in
        if (fromx > tox) {
            val slidein = TranslateAnimation(fromx, tox, fromy, toy)
            slidein.duration = 350
            end.startAnimation(slidein)

            val alpha = AlphaAnimation(1.0f, 0.0f)
            alpha.setAnimationListener(changevisibility)
            alpha.duration = 200
            start.startAnimation(alpha)
        }
        if (fromy > toy) {
            val slidein = TranslateAnimation(fromx, tox, fromy, toy)
            slidein.duration = 350
            end.startAnimation(slidein)

            val alpha = AlphaAnimation(1.0f, 0.0f)
            alpha.setAnimationListener(changevisibility)
            alpha.duration = 200
            start.startAnimation(alpha)
        }
        // slide out
        if (fromx < tox) {
            val slideOut = TranslateAnimation(fromx, tox, fromy, toy)
            slideOut.duration = 350
            start.startAnimation(slideOut)

            val alpha = AlphaAnimation(0.0f, 0.9f)
            alpha.setAnimationListener(out)
            alpha.duration = 200
            end.startAnimation(alpha)
        }
        if (fromy < toy) {
            val slideOut = TranslateAnimation(fromx, tox, fromy, toy)
            slideOut.duration = 350
            end.startAnimation(slideOut)

            val alpha = AlphaAnimation(1.0f, 0.0f)
            alpha.setAnimationListener(changevisibility)
            alpha.duration = 200
            start.startAnimation(alpha)
        }
    }

    /**
     * 複製Assets內所有資料 到 getFilesDir 中
     */
    fun copyAssets(context: Context) {
        val assetManager = context.assets
        var files: Array<String>? = null
        try {
            files = assetManager.list("")
        } catch (e: IOException) {
            //	        Log.e("tag", "Failed to get asset file list.", e);
        }

        for (filename in files!!) {
            var inputStream: InputStream? = null
            var out: OutputStream? = null
            try {
                inputStream = assetManager.open(filename)
                val outFile = File(context.filesDir, filename)
                out = FileOutputStream(outFile)
                copyFile(inputStream!!, out)
                inputStream.close()
                inputStream = null
                out.flush()
                out.close()
                out = null
            } catch (e: IOException) {
                //	            Log.e("tag", "Failed to copy asset file: " + filename, e);
            }

        }
    }

    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int = 0
        read = inputStream.read(buffer)
        while (read != -1) {
            out.write(buffer, 0, read)
            read = inputStream.read(buffer)
        }
    }

    /**
     * 從Assets讀取txt檔案 回傳字串
     *
     * @param FileName FileName.txt
     */
    fun getStringFromtxtByAssets(context: Context, FileName: String): String {
        var result = ""
        val buf = StringBuilder()
        try {
            val json = context.assets.open(FileName)
            val inputStream = BufferedReader(InputStreamReader(json))
            var str: String
            str = inputStream.readLine()
            while (str != null) {
                buf.append(str)

            }

            inputStream.close()
        } catch (e: Exception) {
            return ""
        }

        result = buf.toString()
        return result

    }

    /**
     * 下載文件檔案
     *
     * @param context
     * @param Name    要儲存的文件名
     * @param URL     要下載的網址
     */
    fun downloadFiles(context: Context, Name: String, URL: String) {

        try {
            val url = java.net.URL(URL)
            val conn = url.openConnection()
            conn.connectTimeout = 5000
            val httpConn = conn as HttpURLConnection
            httpConn.requestMethod = "GET"
            httpConn.connect()
            val lenghtOfFile = conn.getContentLength()
            val inputStream = url.openStream()
            val fos = context.openFileOutput(TransferString(Name), Context.MODE_PRIVATE)
            val data = ByteArray(1024)
            var count = 0
            var total: Long = 0
            var progress = 0
            count = inputStream.read(data)
            while (count != -1) {
                total += count.toLong()
                val progress_temp = total.toInt() * 100 / lenghtOfFile
                if (progress_temp % 10 == 0 && progress != progress_temp) {
                    progress = progress_temp
                }
                fos.write(data, 0, count)
                count = inputStream.read(data)
            }
            inputStream.close()
            fos.close()
        } catch (e: MalformedURLException) {

            e.printStackTrace()
        } catch (e: FileNotFoundException) {

            e.printStackTrace()
        } catch (e: IOException) {

            e.printStackTrace()
        }

    }

    /**
     * 複製Asssets資料夾中的db 到指定的資料夾
     *
     * @param context
     * @param dbname      要複製的db名稱
     * @param outFileName 要儲存的路徑 "/data/data/pro.realtouchapp.tte2014/databases/"
     * @throws IOException
     */
    @SuppressLint("SdCardPath")
    @Throws(IOException::class)
    fun copyDataBase(context: Context, dbname: String, outFileName: String) {
        var outFileName = outFileName
        // Open your local db as the input stream
        val myInput = context.assets.open(dbname)
        // Path to the just created empty db
        outFileName = "/data/data/pro.realtouchapp.tte2014/databases/$dbname"
        // Open the empty db as the output stream
        val myOutput = FileOutputStream(outFileName)
        // transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int
        length = myInput.read(buffer)
        while (length > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }
        // Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

    /**
     * 判斷字串是否為數字
     */
    fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    fun isValidMail(email: String): Boolean {
        val check: Boolean
        val p: Pattern
        val m: Matcher

        val EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        p = Pattern.compile(EMAIL_STRING)

        m = p.matcher(email)
        check = m.matches()

        return check
    }

    /**
     * 用來去除字串符號 http://livepluss3.s3.amazonaws.com/exhibits/tte2014/images/
     * news_20140502161441_small.jpg 變成news_20140502161441_small.jpg
     */
    fun TransferString(name: String): String {
        var name = name
        if (name.contains("/")) {
            val tmp = name.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            name = tmp[tmp.size - 1]
            if (tmp.size > 3) {
                name = tmp[tmp.size - 2] + "_" + tmp[tmp.size - 1]
            }

        }
        return name
    }

    /**
     * html包含網址的格式 轉成WebView可以點選
     *
     * @param html
     * @return
     */
    fun createLinkableString(html: String): String {
        val sp = SpannableString(Html.fromHtml(html))
        Linkify.addLinks(sp, Linkify.ALL)
        return "<body>" + Html.toHtml(sp) + "</body>"

    }

    /**
     * 在TextView顯示Html字串
     */
    fun setTvHtmlformat(textView: TextView, html: String) {
        textView.text = Html.fromHtml(html)
    }

    /**
     * 取得兩日期間隔的日期陣列
     *
     * @param startDate 格式為 2015-01-02
     * @param endDate   格式為 2015-02-01
     */
    @SuppressLint("SimpleDateFormat")
    fun getdateDiffArray(startDate: String, endDate: String): Array<String?> {
        //定義時間格式
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        try {
            startCalendar.time = sdf.parse(startDate)
            endCalendar.time = sdf.parse(endDate)
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }

        //使用在沒有跨年度的天數計算
        var dayDiff = endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR)
        //使用在有跨年度的天數計算
        var dayDiff2 = 0
        //年度計算
        val yearDiff = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)
        val tmpCalendar = Calendar.getInstance()
        //如果不同年
        if (yearDiff > 0) {
            tmpCalendar.set(endCalendar.get(Calendar.YEAR), Calendar.DECEMBER, 30)
            dayDiff = tmpCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR)
            tmpCalendar.set(endCalendar.get(Calendar.YEAR), Calendar.JANUARY, 0)
            dayDiff2 = endCalendar.get(Calendar.DAY_OF_YEAR)
        }
        val result = ArrayList<String>()
        result.add(sdf.format(startCalendar.time))
        for (i in 0 until dayDiff) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
            result.add(sdf.format(startCalendar.time))
        }
        //如果沒有跨年度,dayDiff2為零
        for (i in 0 until dayDiff2) {
            tmpCalendar.add(Calendar.DAY_OF_MONTH, 1)
            result.add(sdf.format(tmpCalendar.time))
        }
        val restultArrray = arrayOfNulls<String>(result.size)
        for (i in result.indices) {
            restultArrray[i] = result[i]
        }
        return restultArrray
    }

    /**
     * dp轉換為目前裝置的pixel
     */
    fun getPixelByDevice(context: Context, dpsize: Int): Int {
        var pixel = 0
        val displayMetrics = context.resources
                .displayMetrics
        val realSpSize = ((dpsize * displayMetrics.widthPixels).toFloat()
                / displayMetrics.density / 360f).toInt()
        pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                realSpSize.toFloat(), context.resources.displayMetrics).toInt()
        return pixel

    }

    /**
     *
     * 取得該路徑下的檔案MimeType:
     * ex: <br></br>
     * sdcard/0/test.jpg → image/jpeg <br></br>
     * sdcard/0/test.mp4 → video/mp4<br></br>
     * 如果失敗則回傳空白字元 ""
     *
     * @param filePath 檔案的路徑
     * @author Robert Chou didi31139@gmail.com
     * @date 2015/5/19 下午3:46:30
     * @version
     */
    fun getFileMimeType(filePath: String): String? {

        try {
            val extension = filePath.substring(filePath.lastIndexOf("."))
            val mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    mimeTypeMap)
        } catch (e: Exception) {
            return ""
        }

    }

    fun FullScreencall(window: Window) {

        if (Build.VERSION.SDK_INT < 19) { //19 or above api
            window.decorView.systemUiVisibility = View.GONE
        } else {
            //for lower api versions.
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }
}