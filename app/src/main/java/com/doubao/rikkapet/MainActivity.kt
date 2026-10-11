package com.doubao.rikkapet

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val REQUEST_OVERLAY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToggle: Button = findViewById(R.id.btn_toggle)
        btnToggle.setOnClickListener {
            if (hasOverlayPermission()) {
                startService(Intent(this, FloatingPetService::class.java))
                Toast.makeText(this, "六花已经浮在桌面上了", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                requestOverlayPermission()
            }
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else true
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY) {
            if (hasOverlayPermission()) {
                startService(Intent(this, FloatingPetService::class.java))
                Toast.makeText(this, "权限已开，六花出现！", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "没给悬浮窗权限，六花出不来", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
