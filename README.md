# Rossmax Health Style External Login Example Projects

This project provides examples to demonstrate how to enable RMHs partners to implement it in their own App, so that users of the App can successfully log in to the RMHs membership system and allow the App to obtain an access token. 

Partners can obtain App users’ physical measurement records stored in RMHs through the access token.

### Android

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


##### Get access token

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
        // After obtaining the access tokn, you can save it and transfer to another view.
    }

}

```


### License

```
© 2017 by Make A Change.
Proudly created with viWave.com
```

### Contact us

![viWave.com](https://static.wixstatic.com/media/6b2605_f248ecb6e06543f793aa4cde1291c81d~mv2.png/v1/fill/w_520,h_148,al_c,lg_1/logo_5.png)

T: +886-281781002

E: service@viwave.com
