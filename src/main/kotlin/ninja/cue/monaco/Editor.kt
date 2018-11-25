package ninja.cue.monaco

import javafx.scene.layout.BorderPane
import javafx.scene.web.WebView
import netscape.javascript.JSObject


class Monaco : BorderPane() {
    private val webView = WebView()
    private var window: JSObject? = null
    private var _content = ""
    private var _language = ""
    private var _theme = ""
    init {
        webView.engine.isJavaScriptEnabled = true
        center = webView
        webView.engine.load(javaClass.getResource("index.html").toExternalForm())
        window = webView.engine.executeScript("window") as JSObject

        window?.setMember("javafx", this)
    }

    fun setContent(value: String) {
        System.out.println("EXTERNAL UPDATE: ")
        System.out.println(value)
        window?.call("updateContent", value)
        _content = value
    }

    fun getContent(): String {
        return _content
    }

    fun setLanguage(value: String) {
        _language = value
    }

    fun getLanguage(): String {
        return _language
    }

    fun setTheme(value: String) {
        window?.call("setTheme", value)
        _theme = value
    }

    fun getTheme(): String {
        return _theme
    }

    fun contentChanged(value: String) {
        System.out.println("JS UPDATE: ")
        System.out.println(value)
        _content = value
    }

    fun log(value: String) {
        System.out.println("[JS][LOG] $value")
    }
}
