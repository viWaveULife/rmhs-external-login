//
//  ViewController.swift
//  RMHsExternalLoginIOSExample
//
//  Created by kushim.chiang on 2020/9/15.
//  Copyright © 2020 kushim.chiang. All rights reserved.
//

import UIKit
import WebKit
import SafariServices

enum ServerResponseMessage: String {
    case BUSY
    case NORMAL
}

let _applicationNameForUserAgent = "Version/8.0.2 Safari/600.2.5 RossmaxHealthStyleService iOS"
let _handlerName = "RMHs"

class ViewController: UIViewController {

    // login url is provided by Rossmax / ViWaveULife.
    let loginWebURLString = "https://rossmax-care-dev.web.app/outer_service_login"
    //let loginWebURLString = "https://dummyrmhs.web.app/login"
    
    // serviceId is provided by Rossmax / ViWaveULife.
    let serviceId = "oService1"
    
    var webView: WKWebView!
    
    
    // MARK: - Life cycle functions
    override func loadView() {
        replaceViewByWebView()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let url = URL(string: loginWebURLString) {
            webView.load(URLRequest(url: url))
        }
       
    }

    // MARK: - Implementation functions
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
}

// MARK: -

extension ViewController: WKScriptMessageHandler {
    
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
            // After obtaining the access tokn, you can save it and switch to another display interface.
            showAccessTokenByAlertController(messageBody)
        }

    }
    
    /// Show access token by AlertController
    ///
    /// This function is just a demo.
    private func showAccessTokenByAlertController(_ messageBody: String) {
        
        let titleString = "Access Token"
        let alertController = UIAlertController(title: titleString, message: messageBody, preferredStyle: .alert)
        let okAction = UIAlertAction(title: "OK", style: .default)
        alertController.addAction(okAction)
        
        present(alertController, animated: true)
    }
}

extension ViewController: WKNavigationDelegate {
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        webView.evaluateJavaScript("setServiceId('\(serviceId)')") { (response, error) in
            guard error == nil else {
                return
            }
        }
    }
}
