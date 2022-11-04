package com.ncs.fastafmobile

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.ncs.fastafmobile.adapter.RecyclerAdapter
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
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
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
        initRecyclerView()

        binding.actionbar.logout.setOnClickListener{
            FirebaseAuth
                .getInstance()
                .signOut()
            startActivity(Intent(this,AuthActivity :: class.java ))
            finish()

        }

        binding.scanBtn.setOnClickListener {
            scanQrCodeLauncher.launch(ScannerConfig.build {
                setOverlayStringRes(R.string.qr_label)
                setShowTorchToggle(true)
                setHapticSuccessFeedback(true)
            })
        }
    }


    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.layoutManager = layoutManager
        val adapter = RecyclerAdapter(arrayOf("Dropbox","Slack","Facebook","Github","Gmail","Twitter","Linkedin"),
            arrayOf("kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com","kaygo1988@gmail.com"),
            "New auth requested", arrayOf(R.drawable.dropbox,R.drawable.slack,R.drawable.facebook,R.drawable.github,R.drawable.gmail,R.drawable.twitter,R.drawable.linkdin)
        )
        binding.recyclerView.adapter=adapter
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


