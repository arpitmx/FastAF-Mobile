package com.ncs.fastafmobile

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ncs.fastafmobile.databinding.ActivityMainBinding
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.ScannerConfig

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val scanQrCodeLauncher = registerForActivityResult(ScanCustomCode(), ::handleResult)


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,"android.permission.POST_NOTIFICATIONS") ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale("android.permission.POST_NOTIFICATIONS")) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askNotificationPermission()

        binding.scanBtn.setOnClickListener {
            scanQrCodeLauncher.launch(ScannerConfig.build {
                setOverlayStringRes(R.string.qr_label)
                setShowTorchToggle(true)
                setHapticSuccessFeedback(true)
            })
        }
    }

    fun handleResult(result: QRResult) {

            val response = when (result) {
                is QRResult.QRSuccess -> result.content.rawValue
                QRResult.QRUserCanceled -> "Auth canceled"
                QRResult.QRMissingPermission -> "Missing permission"
                is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
            }

        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()


}
}


