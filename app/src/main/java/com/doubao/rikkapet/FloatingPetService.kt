package com.doubao.rikkapet

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Outline
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.ImageView

class FloatingPetService : Service() {

    private lateinit var windowManager: WindowManager
    private var petView: ImageView? = null
    private var params: WindowManager.LayoutParams? = null
    private val handler = Handler(Looper.getMainLooper())

    private val idleFrames = intArrayOf(
        R.drawable.pet_idle_0,
        R.drawable.pet_idle_1
    )
    private var idleIndex = 0

    private val idleTick = object : Runnable {
        override fun run() {
            idleIndex = (idleIndex + 1) % idleFrames.size
            petView?.setImageResource(idleFrames[idleIndex])
            handler.postDelayed(this, 600)
        }
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        addPet()
    }

    private fun addPet() {
        val size = (120 * resources.displayMetrics.density).toInt()

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val p = WindowManager.LayoutParams(
            size, size,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 300
        }
        params = p

        val iv = ImageView(this).apply {
            setImageResource(R.drawable.pet_idle_0)
            scaleType = ImageView.ScaleType.CENTER_CROP
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            clipToOutline = true
            setBackgroundColor(0xFFF5E6D3.toInt())
        }
        petView = iv

        setupTouch(iv, p)
        windowManager.addView(iv, p)
        handler.postDelayed(idleTick, 600)
    }

    private fun setupTouch(iv: ImageView, p: WindowManager.LayoutParams) {
        var initialX = 0
        var initialY = 0
        var touchX = 0f
        var touchY = 0f
        var moved = false

        iv.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = p.x
                    initialY = p.y
                    touchX = e.rawX
                    touchY = e.rawY
                    moved = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = e.rawX - touchX
                    val dy = e.rawY - touchY
                    if (Math.abs(dx) > 15 || Math.abs(dy) > 15) moved = true
                    p.x = initialX + dx.toInt()
                    p.y = initialY + dy.toInt()
                    try { windowManager.updateViewLayout(iv, p) } catch (_: Exception) {}
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!moved) onTap(iv)
                    true
                }
                else -> false
            }
        }
    }

    private fun onTap(iv: ImageView) {
        handler.removeCallbacks(idleTick)
        idleIndex = 0
        iv.setImageResource(R.drawable.pet_tap)
        handler.postDelayed({
            iv.setImageResource(R.drawable.pet_idle_0)
            handler.postDelayed(idleTick, 600)
        }, 900)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        petView?.let {
            try { windowManager.removeView(it) } catch (_: Exception) {}
        }
        petView = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
