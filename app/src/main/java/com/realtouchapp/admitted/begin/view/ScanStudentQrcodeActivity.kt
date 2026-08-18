package com.realtouchapp.admitted.begin.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.viewmodel.ScanStudentQrcodeViewModel
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.common.ui.dialogfragmnet.TextInputDialogFragment
import com.realtouchapp.admitted.databinding.ActivityScanStudentQrcodeBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.admitted.network.result.DeCryptionResponse
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.dpToPx
import com.realtouchapp.tools.ext.whenAllNotNull
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections

@AndroidEntryPoint
class ScanStudentQrcodeActivity: AppCompatActivity(), OnClickListener {
    companion object {
        private const val TAG = "ScanStudentQrcodeActivity"
        private const val IS_INTENT_FROM_MAIN_KEY = "isIntentFromMainKey"

        private const val CAMERA_PERMISSION_REQUEST_CODE = 100

        fun intent(activity: Activity, isIntentFromMain: Boolean = false) {
            val intent = Intent()
                .setClass(activity, ScanStudentQrcodeActivity::class.java)
                .putExtra(IS_INTENT_FROM_MAIN_KEY, isIntentFromMain)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private val viewModel by viewModels<ScanStudentQrcodeViewModel>()
    private lateinit var binding: ActivityScanStudentQrcodeBinding

    private var isIntentFromMain: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanStudentQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        loadIntentExtras()
        setClick()
        subscribeUi()

        checkCameraPermission()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeView.pause()
    }

    private fun loadIntentExtras() {
        isIntentFromMain = intent.getBooleanExtra(IS_INTENT_FROM_MAIN_KEY, false)
    }

    private fun subscribeUi() {
        viewModel.response.observe(this) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.SEARCH_STUDENT -> {
                            val deCryptionResponse = result.data?.data as? DeCryptionResponse
                                ?: run {
                                    startBarcodeDecoding()
                                    return@observe
                                }

                            Log.d(TAG, "subscribeUi: student=${deCryptionResponse.student}, method=${deCryptionResponse.method}, subscribes=${deCryptionResponse.subscribes}")
                            
                            listOf<Any?>(
                                deCryptionResponse.student,
                                deCryptionResponse.method,
                                deCryptionResponse.subscribes
                            ).whenAllNotNull(
                                block = {
                                    AddStudentInfoActivity.intent(
                                        this,
                                        it[0] as Student,
                                        it[1] as Int,
                                        it[2] as String,
                                        isIntentFromMain
                                    )
                                },
                                `else` = {
                                    startBarcodeDecoding()
                                }
                            )
                        }
                        else -> {
                            startBarcodeDecoding()
                        }
                    }
                }
                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else {
                        //失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }

                    startBarcodeDecoding()
                }
                NetworkResult.Status.LOADING -> {
                    //載入中 可顯示載入動畫
                }
            }
        }
    }

    private fun setClick() {
        binding.clPhotoLibrary.setOnClickListener(this)

        binding.ivScanStudentQrcodeBack.setOnClickListener(this)

        binding.btnManualEnterCode.setOnClickListener(this)
    }

    private fun checkCameraPermission() {
        // 檢查相機權限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        // 設定掃描格式
        val formats = Collections.singletonList(BarcodeFormat.QR_CODE)
        binding.barcodeView.decoderFactory = DefaultDecoderFactory(
            formats,
            null,
            null,
            0
        )

        // 設定掃描回調
        startBarcodeDecoding()

        // 開始掃描
        binding.barcodeView.resume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                Toast.makeText(this, "需要相機權限才能掃描 QR Code", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_scan_student_qrcode_back ->
                back()
            R.id.cl_photo_library ->
                openImagePicker()
            R.id.btn_manual_enter_code ->
                showManualEnterCodeDialog()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        back()
    }

    private fun back() {
        if (isIntentFromMain) {
            // 回到「家長」的首頁
            ParentMainActivity.intent(this)
        } else {
            // 回到「家長」初始編輯學生的畫面
            ParentEditStudentActivity.intent(this)
        }
    }

    private val decodeBarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // 掃描到 QR Code
            val qrCodeContent = result.text
            runOnUiThread {
                processQrcodeContent(qrCodeContent)
            }
        }

        override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {
            // 可選：處理掃描點
        }
    }

    private fun startBarcodeDecoding() {
        runOnUiThread {
            binding.barcodeView.decodeContinuous(decodeBarcodeCallback)
        }
    }

    private fun stopBarcodeDecoding() {
        runOnUiThread {
            binding.barcodeView.stopDecoding()
        }
    }

    /** 處理掃描到的QRCode */
    private fun processQrcodeContent(qrcodeContent: String) {
        Log.e(TAG, "qrcodeContent: $qrcodeContent")

        stopBarcodeDecoding()

        viewModel.searchStudent(qrcodeContent)
    }

    // region -- 照片選擇 --

    /** 開啟照片選擇 */
    private fun openImagePicker() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this)) {
            photoLibraryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            // 對於較舊的裝置，Intent.ACTION_PICK 通常不需要額外權限即可取得所選檔案的 URI 讀取權限
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult

        val selectedImageUri: Uri = it.data?.data
            ?: return@registerForActivityResult

        didPickImageWithUri(selectedImageUri)
    }

    private val photoLibraryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) return@registerForActivityResult

        didPickImageWithUri(it)
    }

    /** 處理選擇的照片 */
    private fun didPickImageWithUri(uri: Uri) {
        val bitmap = uriToBitmap(this, uri) ?: run {
            return
        }
        val qrCodeResult = scanQRCode(bitmap) ?: run {
            bitmap.recycle()
            Toast.makeText(this, "未偵測到 QR Code", Toast.LENGTH_SHORT)
                .show()
            return
        }
        bitmap.recycle()

        processQrcodeContent(qrCodeResult)
    }

    /** 從選擇的圖片Uri讀取Bitmap */
    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** 搜尋圖片中的QRCode */
    private fun scanQRCode(bitmap: Bitmap): String? {
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        return try {
            val result = reader.decode(binaryBitmap)
            result.text
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "QR Code not found", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error scanning QR Code", e)
            null
        }
    }

    // endregion -- 照片選擇 --

    // region -- Show manual enter code dialog --

    private fun showManualEnterCodeDialog() {
        stopBarcodeDecoding()

        val runRequest: (TextInputDialogFragment, String) -> Unit =
            { dialog, text ->
                dialog.dismiss()

                viewModel.searchStudent(text)
            }

        TextInputDialogFragment.newInstance(
            title = "Enter student's systemNumber",
            message = null,
            sureButtonText = "OK",
        ).addEditText(this) {
            hint = "Enter systemNumber"
            setBackgroundColor(android.graphics.Color.WHITE)
            setTextColor(android.graphics.Color.BLACK)
            setHintTextColor(android.graphics.Color.GRAY)
            setPadding(dpToPx(5f))
        }.setOnSureListener { dialog ->
            // 处理用户输入
            val inputText = dialog.getInputText(0)
            if (inputText.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter systemNumber", Toast.LENGTH_SHORT)
                    .show()
            } else {
                runRequest(dialog, inputText)
            }
            return@setOnSureListener false
        }.setOnCancelListener {
            startBarcodeDecoding()
        }.also {
            it.show(supportFragmentManager, TextInputDialogFragment.TAG)
        }
    }

    // endregion -- Show manual enter code dialog --
}