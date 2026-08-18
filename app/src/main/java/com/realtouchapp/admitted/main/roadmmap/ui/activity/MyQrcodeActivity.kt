package com.realtouchapp.admitted.main.roadmmap.ui.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.ActivityMyQrcodeBinding
import com.realtouchapp.admitted.main.roadmmap.viewmodel.PorfileActivityViewModel
import com.realtouchapp.admitted.network.entity.UserInfo
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.text.toFloat
import android.media.MediaScannerConnection
import com.realtouchapp.admitted.ext.configureEdgeToEdge

@AndroidEntryPoint
class MyQrcodeActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityMyQrcodeBinding
    private val viewModel by viewModels<PorfileActivityViewModel>()

    private lateinit var userInfo: UserInfo

    private lateinit var qrgEncoder: QRGEncoder
    private var bitmap: Bitmap? = null
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                saveImageToGallery()
            } else {
                // 如果用戶拒絕權限，提供去設置頁面的選項
                Toast.makeText(this, "Need permissions to save image to gallery.", Toast.LENGTH_LONG).show()
                // 可以添加一個按鈕或提示，讓用戶跳轉到應用設置頁面
                showSettingsDialog()
            }
        }

    companion object {
        private const val TAG = "MyQrcodeActivity"

        fun intent(activity: AppCompatActivity) {
            val intent = Intent()
                .setClass(activity, MyQrcodeActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initializeView()
        subscribeUi()
        viewModel.getUserInfoCache()
    }

    override fun onDestroy() {
        bitmap?.recycle()
        bitmap = null

        super.onDestroy()
    }

    private fun initializeView() {
        binding.ivActivityMyQrcodeBack.setOnClickListener(this)

        binding.ivActivityMyQrcodeQrcode.setBackgroundResource(R.drawable.bg_black_border)
//        val padding = dpToPx(5f)
//        binding.ivActivityMyQrcodeQrcode.setPadding(padding, padding, padding, padding)
    }

    private fun subscribeUi() {
        viewModel.userInfo.observe(this) {
            it?.let { userInfo ->
                this.userInfo = userInfo

                generateQrcode(userInfo.systemNumber ?: "")

                binding.clActivityMyQrcodeShare.setOnClickListener(this)
                binding.clActivityMyQrcodeDownload.setOnClickListener(this)
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_activity_my_qrcode_back -> {
                finish()
            }
            R.id.cl_activity_my_qrcode_share -> {
                shareImage()
            }
            R.id.cl_activity_my_qrcode_download -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10 及以上不需要存儲權限
                    saveImageToGallery()
                } else {
                    // Android 9 及以下需要請求權限
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        saveImageToGallery()
                    } else {
                        // 如果用戶之前拒絕過權限，應該解釋為什麼需要這個權限
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(this, "Need permissions to save image to gallery.", Toast.LENGTH_LONG).show()
                        }
                        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }

    private fun generateQrcode(data: String) {
        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4

        qrgEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, smallerDimension)
            .apply {
                colorBlack = Color.BLACK
                colorWhite = Color.WHITE
            }
        try {
            bitmap = qrgEncoder.getBitmap(1)
            binding.ivActivityMyQrcodeQrcode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    private fun saveImageToGallery() {
        bitmap?.let { bitmap ->
            try {
                val filename = userInfo.systemNumber?.let {
                    "$it.jpg"
                } ?: run {
                    "${System.currentTimeMillis()}.jpg"
                }
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10 及以上使用 MediaStore API
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Admitted")
                    }

                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        }

                        Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(this, "QR code picture cannot be saved.", Toast.LENGTH_SHORT).show()
                    }
                    
                } else {
                    // Android 9 及以下使用傳統方式
                    val imagesDir = File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "Admitted").apply {
                        if (!exists()) mkdirs()
                    }

                    val imageFile = File(imagesDir, filename)
                    FileOutputStream(imageFile).use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }

                    // 通知媒體庫更新
                    MediaScannerConnection.scanFile(
                        this,
                        arrayOf(imageFile.absolutePath),
                        arrayOf("image/jpeg")
                    ) { _, _ ->
                        Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Save QR code picture failed.", e)
                Toast.makeText(this, "Save QR code picture failed.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "QR code picture not exist.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareImage() {
        bitmap?.let { bitmap ->
            try {
                val uri = saveImageToCache(bitmap)
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/jpeg"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(shareIntent, "Share QR code"))
            } catch (e: Exception) {
                Toast.makeText(this, "Share failed.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Share image failed", e)
            }
        } ?: run {
            Toast.makeText(this, "QR code picture not exist.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToCache(bitmap: Bitmap): Uri {
        val cachePath = File(cacheDir, "shared_images").apply {
            mkdirs()
        }
        val file = File(cachePath, "qrcode_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        }
        return FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
    }

    private fun showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Need permissions")
            .setMessage("Need permissions to save image to gallery, please grant them in Settings")
            .setPositiveButton("Go to Settings") { _, _ ->
                // 跳轉到應用設置頁面
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}