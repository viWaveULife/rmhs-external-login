package com.viwave.station.demormhslogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOGIN_URL = "https://rossmax-care-dev.web.app/outer_service_login"
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progress_bar)
        webView = findViewById(R.id.web_view)
        loadWebView()
    }

    private fun loadWebView() {
        progressBar.visibility = View.VISIBLE

        webView.apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                loadsImagesAutomatically = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                setSupportZoom(true)
                useWideViewPort = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                userAgentString = "$userAgentString RossmaxHealthStyleService Android"
            }
            isVerticalScrollBarEnabled = true
            isHorizontalScrollBarEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            setInitialScale(0)
            clearCache(true)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url)
                    return true
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.progress = newProgress
                    if (progressBar.progress == 100) {
                        progressBar.visibility = View.GONE
                        webView.evaluateJavascript("javascript:setServiceId('oService1')") {}
                    }
                }
            }
            addJavascriptInterface(object {
                @JavascriptInterface
                fun onAccessTokenReceived(token: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "token >>> $token", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }, "Android")
            loadUrl(LOGIN_URL)
        }
    }
}
