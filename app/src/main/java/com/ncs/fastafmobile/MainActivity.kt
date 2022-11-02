package com.ncs.fastafmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ncs.fastafmobile.databinding.ActivityMainBinding
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.config.ScannerConfig

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val scanQrCodeLauncher = registerForActivityResult(ScanCustomCode(), ::handleResult)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


