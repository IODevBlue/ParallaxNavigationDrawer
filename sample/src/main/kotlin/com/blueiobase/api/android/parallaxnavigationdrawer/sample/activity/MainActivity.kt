package com.blueiobase.api.android.parallaxnavigationdrawer.sample.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer
import com.blueiobase.api.android.parallaxnavigationdrawer.sample.R

class MainActivity : AppCompatActivity() {

    /** Back-pressed handler for Android 33 and above. */
    private val onBackInvokedCallback by lazy { OnBackInvokedCallback { onSupportNavigateUp() } }

    /** Backward compatibility back-pressed handler for Android versions less than 33. */
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onSupportNavigateUp()
            }
        }
    }

    private val isAndroid33: Boolean
        get() = Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU
    private val parallaxNavigationDrawer: ParallaxNavigationDrawer by lazy { findViewById(R.id.pnd) }
    private val leftDrawerButton: ImageButton by lazy { findViewById(R.id.left_drawer_button) }
    private val rightDrawerButton: ImageButton by lazy { findViewById(R.id.right_drawer_button) }
    private val githubButton: ImageButton by lazy { findViewById(R.id.github_button) }
    private val repoLink = "https://github.com/IODevBlue/ParallaxNavigationDrawer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isAndroid33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                onBackInvokedCallback
            )
        } else {
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
        }
        parallaxNavigationDrawer.apply {
            setOnLeftDrawerStateChangedListener {
                if (it)
                    Toast.makeText(this@MainActivity, "Left Drawer Open!", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@MainActivity, "Left Drawer Close!", Toast.LENGTH_SHORT)
                        .show()
            }
            setOnRightDrawerStateChangedListener {
                if (it)
                    Toast.makeText(this@MainActivity, "Right Drawer Open!", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@MainActivity, "Right Drawer Close!", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        leftDrawerButton.setOnClickListener {
            parallaxNavigationDrawer.toggleLeftDrawer()
        }

        rightDrawerButton.setOnClickListener {
            parallaxNavigationDrawer.toggleRightDrawer()
        }

        githubButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(repoLink)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isAndroid33) {
            onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback)
        } else {
            onBackPressedCallback.apply {
                isEnabled = false
                remove()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAndroid33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, onBackInvokedCallback)
        } else {
            onBackPressedDispatcher.addCallback(onBackPressedCallback.apply { isEnabled = true })
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return if (parallaxNavigationDrawer.onBackPressed()) super.onNavigateUp() else false
    }
}