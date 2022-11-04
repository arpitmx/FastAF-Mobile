
package com.ncs.fastafmobile

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.messaging.FirebaseMessaging
import com.ncs.fastafmobile.databinding.ActivityAuthBinding
import kotlinx.coroutines.processNextEventInCurrentThread
import timber.log.Timber
import kotlin.math.log


class AuthActivity : AppCompatActivity() {

    lateinit var binding : ActivityAuthBinding
    var signup : Boolean = true
    private lateinit var auth: FirebaseAuth
    val TAG = "AuthActivityXYZ"
    val db = Firebase.firestore
    lateinit var sharedPrefs : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var fcmToken : String

    companion object{
        private const val sharedPrefUser = "prefUserDetails"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sharedPrefs = this.getSharedPreferences(sharedPrefUser, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()
        getFCMToken()


            binding.nextButton.setOnClickListener{
                val email = binding.emailEdit.text.toString().trim()
                val pass = binding.passwordEdit.text.toString().trim()

                if (!email.isBlank() && !pass.isBlank()){
                binding.progressBar.visibility = View.VISIBLE
                if (signup){
                    createAccount(email,pass)
                }else {
                    login(email,pass)
                }

            }else{
            Toast.makeText(this, "Fill all inputs", Toast.LENGTH_SHORT).show()
        } }

        binding.changeType.setOnClickListener{
            if (signup==true){
                binding.nextButton.text = "Login"
                signup = false
            }else {
                binding.nextButton.text = "Sign Up"
                signup = true
            }}

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Toast.makeText(this, currentUser?.displayName.toString(), Toast.LENGTH_SHORT).show()
        if(currentUser != null){
            proceed()
        }

    }

    fun proceed(){
        startActivity(Intent(this, MainActivity::class.java))

    }

    fun createAccount(email:String ,pass : String ){
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Authentication Success.",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUser(user!!)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility= View.GONE


                }
            }
    }

    fun login(email:String, pass:String){
    auth.signInWithEmailAndPassword(email, pass)
    .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            Timber.tag(TAG).d("signInWithEmail:success")
            val user = auth.currentUser
            updateUser(user!!)


        } else {
            // If sign in fails, display a message to the user.
            Timber.tag(TAG).w(task.exception, "signInWithEmail:failure")
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.visibility = View.GONE

        }
    }}

    fun updateUser(user: FirebaseUser){

        editor.putString("userID",user.uid)
        editor.apply()
        editor.commit()


        val userDetails = hashMapOf(
                "uid" to user.uid,
                "email" to user.email,
                "apps" to null,
                "fcmToken" to fcmToken
            )

        db.collection("users").document(user.uid)
            .set(userDetails)
            .addOnSuccessListener {
                Timber.tag(TAG).d("Details added!")
                binding.progressBar.visibility = View.GONE
                proceed()
            }
            .addOnFailureListener{ e->
                Timber.tag(TAG).d(e, "Error writing document")
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Data addition failed", Toast.LENGTH_SHORT).show()
            }





    }



    fun setToken(tk: String){
        fcmToken = tk
    }

    fun getFCMToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->

            if (!task.isSuccessful) {
                Timber.tag(TAG).w(task.exception, "Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val tk = task.result
            setToken(tk)

            // Log and toast
            Timber.tag(TAG).d(tk)
           // Toast.makeText(baseContext, tk, Toast.LENGTH_SHORT).show()
        })

    }

}