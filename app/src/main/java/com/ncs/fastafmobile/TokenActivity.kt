package com.ncs.fastafmobile

import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import timber.log.Timber

class TokenActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    companion object{
       const val TAG = "TokenActivity123Xyz"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R .layout.activity_token)


        auth = FirebaseAuth.getInstance()

        val token : String
        val extras = intent.extras
        if (extras != null) {
            token = extras.getString("token","-1")

            getTokenValidationResponseCode(token)
            rawJSON()

        }else {
            Toast.makeText(this, "Null token", Toast.LENGTH_SHORT).show()
        }

    }




    fun rawJSON() {

       val email = auth.currentUser!!.email
       val userID = auth.currentUser!!.uid
        lateinit var fcmToken : String

        //Toast.makeText(this, "email : "+email+" \nuserID:"+userID, Toast.LENGTH_SHORT).show()

        val docRef = db.collection("users").document(userID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Timber.tag(TAG).d("DocumentSnapshot data: %s", document.data)
                    fcmToken = document.get("fcmToken").toString()
                } else {
                    Timber.tag(TAG).d("No such document")
                }
            }
            .addOnFailureListener { exception ->
                Timber.tag(TAG).d(exception, "get failed with ")
            }






        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("fuserID", userID)
        jsonObject.put("fcmToken", fcmToken)
        jsonObject.put("fuserEmail", email)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.postFastAFUserDetails(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    Timber.tag("Pretty Printed JSON :").d(prettyJson)

                } else {

                    Timber.tag("RETROFIT_ERROR").e(response.code.toString())

                }
            }
        }
    }


    fun getTokenValidationResponseCode(token: String) {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getTokenValidationResponseCode(token)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(JsonParser.parseString(response.body?.string()))
                    Timber.tag("Pretty Printed JSON :").d(prettyJson)

                } else {

                    Timber.tag("RETROFIT_ERROR").e(response.code.toString())

                }
            }
        }
    }
}