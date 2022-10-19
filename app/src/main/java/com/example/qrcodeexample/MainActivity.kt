package com.example.qrcodeexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var qrScanIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //var userId = intent.getStringExtra("user_id")
        //var emailId = intent.getStringExtra("email_id")

        var userId = FirebaseAuth.getInstance().currentUser!!.uid;
        var emailId = FirebaseAuth.getInstance().currentUser!!.email;


        tv_user_id.text = "User ID :: $userId"
        tv_email_id.text = "Email ID :: $emailId"

        btn_logout.setOnClickListener(){
            //Log out from app
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        setupScanner()
        setOnClickListener()


        btn_create_data.setOnClickListener(){
            startActivity((Intent(this@MainActivity, CreateData::class.java)))
            finish()
        }


    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator(this)
    }

    private fun setOnClickListener() {
        btn_scan.setOnClickListener(){performAction()}
    }

    private fun performAction() {
        // Code to perform action when button is clicked.
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val userRefference = FirebaseAuth.getInstance().currentUser!!;
        val userId = userRefference.uid;
        val db = FirebaseFirestore.getInstance()



        if (result != null) {
            // If QRCode has no data.

            if (result.contents == null) {
                Toast.makeText(this, "getString(R.string.result_not_found),", Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                val obj = result.contents.toString()

                db.collection(userId).get().addOnSuccessListener { result ->
                    for (document in result){
                        if(obj == document.get("numarStup")){
                            Toast.makeText(this, "ai scanat bine bossssss", Toast.LENGTH_LONG).show()

                            var intent = Intent(this@MainActivity, InformationActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("numar_stup", document.getString("numarStup"))
                            intent.putExtra("rasa_stup", document.getString("rasa"))
                            intent.putExtra("stadiu_stup", document.getBoolean("stadiu"))

                            Toast.makeText(this, "TEST4", Toast.LENGTH_LONG).show();

                            startActivity(intent)
                            finish()
                        }
                    }
                }
                    .addOnFailureListener{exception ->
                        Toast.makeText(this, "Eroare" + exception, Toast.LENGTH_LONG).show()
                    }


            /*try {
                    // Converting the data to json format
                    val obj = JSONObject(result.contents)

                    if(obj.getString("numarStup") == numarStup.toString()){
                        startActivity(Intent(this@MainActivity, InformationActivity::class.java))
                        finish()
                    }

                    // Show values in UI.
                    *//*binding.name.text = obj.getString("name")
                    binding.siteName.text = obj.getString("site_name")*//*

                } catch (e: JSONException) {
                    e.printStackTrace()

                    // Data not in the expected format. So, whole object as toast message.
                    Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                }*/
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}