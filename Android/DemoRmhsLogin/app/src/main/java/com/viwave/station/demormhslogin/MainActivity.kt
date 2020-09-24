package com.viwave.station.demormhslogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ROSSMAX_LOGIN_URL = "https://rossmax-care-dev.web.app/outer_service_login"
        private const val ROSSMAX_USER_AGENT_STRING = "RossmaxHealthStyleService Android"
        private const val ROSSMAX_SERVICE_ID = "oService1"
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
                // Append rossmax user agent string to userAgentString
                userAgentString = "$userAgentString $ROSSMAX_USER_AGENT_STRING"
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
                        // evaluateJavascript with the rossmax service id
                        webView.evaluateJavascript("javascript:setServiceId('$ROSSMAX_SERVICE_ID')") {}
                    }
                }
            }
            // addJavascriptInterface with the interfaceName 'Android'
            addJavascriptInterface(object {
                @JavascriptInterface
                fun onAccessTokenReceived(token: String) {
                    // get the access token here
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "token >>> $token", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }, "Android")
            // load rossmax login url
            loadUrl(ROSSMAX_LOGIN_URL)
        }
    }
}
