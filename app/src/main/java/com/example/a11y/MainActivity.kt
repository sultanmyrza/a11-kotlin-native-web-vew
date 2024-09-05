package com.example.a11y

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.a11y.ui.theme.A11yTheme
import androidx.activity.OnBackPressedCallback
import android.os.Build

class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A11yTheme {
                Scaffold { innerPadding ->
                    val rememberedWebView = remember { WebView(this) }
                    webView = rememberedWebView
                    
                    WebViewContent(
                        webView = rememberedWebView,
                        url = "https://www.npmshops.com",
                        modifier = Modifier.padding(innerPadding)
                    )

                    // Handle back navigation
                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                                    override fun handleOnBackPressed() {
                                        if (webView.canGoBack()) {
                                            webView.goBack()
                                        } else {
                                            isEnabled = false
                                            onBackPressedDispatcher.onBackPressed()
                                        }
                                    }
                                })
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(webView: WebView, url: String, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { webView.apply {
            // Set a custom WebViewClient to handle page navigation
            webViewClient = WebViewClient()
            
            // Configure WebView settings with security measures
            settings.apply {
                // Set custom user agent
                userAgentString = "NoonSpaceApp " + userAgentString
                
                // Enable JavaScript (required for many modern websites)
                // Note: This can introduce security risks, use with caution
                javaScriptEnabled = true
                
                // Prevent loading of mixed content (HTTP content in HTTPS pages)
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                
                // Disable file system access
                allowFileAccess = false
                
                // Disable content provider access
                allowContentAccess = false
                
                // Set safe browsing to true for API 26+ (Android 8.0+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    safeBrowsingEnabled = true
                } else {
                    // For older versions, use these settings as a fallback
                    allowFileAccessFromFileURLs = false
                    allowUniversalAccessFromFileURLs = false
                }
            }
            
            // Load the specified URL
            loadUrl(url)
        } },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    A11yTheme {
        Greeting("Android")
    }
}