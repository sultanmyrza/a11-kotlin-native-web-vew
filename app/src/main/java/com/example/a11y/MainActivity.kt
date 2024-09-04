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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.a11y.ui.theme.A11yTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A11yTheme {
                Scaffold { innerPadding ->
                    WebViewContent(
                        url = "https://www.npmshops.com",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(url: String, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // Set a custom WebViewClient to handle page navigation
                webViewClient = WebViewClient()
                
                // Configure WebView settings with security measures
                settings.apply {
                    // Enable JavaScript (required for many modern websites)
                    // Note: This can introduce security risks, use with caution
                    javaScriptEnabled = true
                    
                    // Prevent loading of mixed content (HTTP content in HTTPS pages)
                    mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                    
                    // Disable file system access
                    allowFileAccess = false
                    
                    // Disable content provider access
                    allowContentAccess = false
                    
                    // Prevent access to local files from web content
                    allowFileAccessFromFileURLs = false
                    allowUniversalAccessFromFileURLs = false
                }
                
                // Load the specified URL
                loadUrl(url)
            }
        },
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