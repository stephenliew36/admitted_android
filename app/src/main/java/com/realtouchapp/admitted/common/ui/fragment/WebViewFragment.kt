package com.realtouchapp.admitted.common.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebBackForwardList
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.FragmentWebviewBinding
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.ext.testToast

/*
*/
class WebViewFragment : Fragment() {
    companion object{
        const val BUNDLE_WEBVIEWEURL = "webview"
    }
    private var _binding: FragmentWebviewBinding? = null

    private val binding get() = _binding!!
    private var webUrl =""
    private var webMenuUrl =""
    private var imageSourceID = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initialView()
    }

    fun getData(){
        webUrl= arguments?.getString(BUNDLE_WEBVIEWEURL,"")!!
        webMenuUrl = webUrl.split("?index=")[0]
    }
    private fun clickBack(){
        if (binding.wvImagezoomfragment.canGoBack()) {
            val historyList: WebBackForwardList = binding.wvImagezoomfragment.copyBackForwardList()
            if (binding.wvImagezoomfragment.url!!.equals(webMenuUrl)){
                //是目錄畫面離開該頁面
                findNavController().popBackStack()
            }else if (historyList.currentIndex==1){
                //是倒數第二頁的話跳到目錄頁面
                binding.wvImagezoomfragment.loadUrl(webMenuUrl)
            }else{
                //返回上一頁
                binding.wvImagezoomfragment.goBack()
            }
        }else{
            findNavController().popBackStack()
        }

    }

    private fun initialView() {
        binding.llSchoolistBack.setOnClickListener {
            clickBack()
        }

        // 启用 JavaScript
        binding.wvImagezoomfragment.settings.javaScriptEnabled = true
        binding.wvImagezoomfragment.settings.setSupportZoom(true)
        binding.wvImagezoomfragment.settings.builtInZoomControls = true
        binding.wvImagezoomfragment.settings.displayZoomControls = false

        // 设置 WebViewClient，以便在 WebView 中打开网页
        binding.wvImagezoomfragment.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.settings?.loadWithOverviewMode = true
                view?.settings?.useWideViewPort = true
            }
        }

        // 加载初始网址
        binding.wvImagezoomfragment.loadUrl(webUrl)

        if (webUrl.isNotEmpty()){
            binding.wvImagezoomfragment.loadUrl(webUrl)
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // 处理返回键事件
                clickBack()
                true
            } else {
                false
            }
        }

    }

    override fun onDestroyView() {
        requireView().setOnKeyListener(null)
        super.onDestroyView()
        _binding = null
    }






}