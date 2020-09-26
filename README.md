# Examples of Accessing Rossmax Health Style Portal

This project provides examples to demonstrate how the RMHs partners could enable RMHs membership system to obtain application users’ physical measurement records stored in RMHs through the access token.

### Android

``` kotlin
companion object {
    private const val ROSSMAX_LOGIN_URL = <service-url>
    private const val ROSSMAX_SERVICE_ID = <service-id>
    private const val ROSSMAX_USER_AGENT_STRING = "RossmaxHealthStyleService Android"
}
```
##### Append rossmax user agent string to userAgentString
``` kotlin
val webView: WebView
webView.settings.userAgentString = "$userAgentString $ROSSMAX_USER_AGENT_STRING"
```
##### EvaluateJavascript with the rossmax service id
``` kotlin
webView.evaluateJavascript("javascript:setServiceId('$ROSSMAX_SERVICE_ID')") {}
```
##### AddJavascriptInterface with the interfaceName 'Android'
``` kotlin
webView.addJavascriptInterface(object {
        @JavascriptInterface
        fun onAccessTokenReceived(token: String) {
			// get the access token here
        }
    }, "Android")
```
##### load rossmax login url
``` kotlin
webView.loadUrl(ROSSMAX_LOGIN_URL)
```

### iOS

##### Application Information Service

1. Contact [viWave](https://www.viwaveulife.com) to apply service.
2. Get service URL and service ID.
3. Modify and fill in:

```swift
let loginWebURLString = <service-url>
let serviceId = <service-id>
```


##### Import frameworks

```swift
import WebKit
import SafariServices
```


##### Replace UIView to WKWebview

```swift
override func loadView() {
    replaceViewByWebView()
}

private func replaceViewByWebView() {
    let userContentController = WKUserContentController()
    userContentController.add(self, name: _handlerName)
    let configuration = WKWebViewConfiguration()
    configuration.userContentController = userContentController

    // for Google Auth on phone devices.
    configuration.applicationNameForUserAgent = _applicationNameForUserAgent
    webView = WKWebView(frame: .zero, configuration: configuration)
    webView.navigationDelegate = self
    view = webView
}
```


##### Load login page

```swift
webView.load(URLRequest(url: url))
```


##### Service register

```swift

extension ViewController: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        webView.evaluateJavaScript("setServiceId('\(serviceId)')") { (response, error) in
            guard error == nil else {
                return
            }
        }
    }
}

```


##### Get the access token

```swift

func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
    guard let messageBody = message.body as? String else {
        return
    }

    switch messageBody {
    case ServerResponseMessage.BUSY.rawValue:
        // Timing to display UI "BUSY"
        print("Show indicator start.")
    case ServerResponseMessage.NORMAL.rawValue:
        // Timing to display UI from BUSY to not.
        print("Show indicator stop.")
    default:
        // After obtaining the access token, you can save it and transfer to another view.
    }

}

```


### License

```
Proudly created by viWave.com
```

### Contact us

![viWave.com](https://static.wixstatic.com/media/6b2605_f248ecb6e06543f793aa4cde1291c81d~mv2.png/v1/fill/w_520,h_148,al_c,lg_1/logo_5.png)

T: +886-281781002

E: service@viwave.com
