package com.blueiobase.api.android.parallaxnavigationdrawer.sample.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer
import com.blueiobase.api.android.parallaxnavigationdrawer.R

class MainActivity : AppCompatActivity() {

    private val parallaxNavigationDrawer: ParallaxNavigationDrawer by lazy { findViewById(R.id.pnd) }
    private val leftDrawerButton: ImageButton by lazy { findViewById(R.id.left_drawer_button) }
    private val rightDrawerButton: ImageButton by lazy { findViewById(R.id.right_drawer_button) }
    private val githubButton: ImageButton by lazy { findViewById(R.id.github_button) }
    private val repoLink = "https://github.com/IODevBlue/ParallaxNavigationDrawer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parallaxNavigationDrawer.apply {
            setOnLeftDrawerStateChangedListener {
                if(it)
                    Toast.makeText(this@MainActivity, "Left Drawer Open!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@MainActivity, "Left Drawer Close!", Toast.LENGTH_SHORT).show()
            }
            setOnRightDrawerStateChangedListener {
                if(it)
                    Toast.makeText(this@MainActivity, "Right Drawer Open!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@MainActivity, "Right Drawer Close!", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        if(parallaxNavigationDrawer.onBackPressed()) super.onBackPressed()
    }
}