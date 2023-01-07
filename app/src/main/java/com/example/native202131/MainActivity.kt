package com.example.native202131

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.native202131.ui.theme.Native202131Theme
import org.slf4j.LoggerFactory

class MainActivity : ComponentActivity() {
    private val logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate savedInstanceState=$savedInstanceState")
        setContent {
            Native202131Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        logger.info("onStart")
    }

    override fun onResume() {
        super.onResume()
        logger.info("onResume")
    }

    override fun onPause() {
        super.onPause()
        logger.info("onPause")
    }

    override fun onStop() {
        super.onStop()
        logger.info("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.info("onDestroy")
    }
}
