package com.example.qrcodeexample

import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_data.*

class CreateData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val buttons = listOf<Button>(btn_create_frame1,btn_create_frame2,btn_create_frame3, btn_create_frame4,btn_create_frame5,btn_create_frame6,btn_create_frame7,btn_create_frame8,btn_create_frame9,btn_create_frame10)
        val listOfButtonTags : MutableList<String> = mutableListOf()

       val user = Firebase.auth.currentUser;

       val userID = FirebaseAuth.getInstance().currentUser!!.uid;

        if(user != null){
            Toast.makeText(this@CreateData, "Esti conectat cu userul " + userID, Toast.LENGTH_SHORT).show();

        }else{

        }

        setContentView(R.layout.activity_create_data)


        btn_upload_data.setOnClickListener(){
            uploadData()
        }

        btn_create_back.setOnClickListener(){
            startActivity(Intent(this@CreateData, MainActivity::class.java))

        }


    }

    private fun uploadData(){
            val hashMap = hashMapOf<String, Any>(
                "url" to et_url_stup_data.text.toString(),
                "rasa" to et_rasa_stup_data.text.toString()
            )

            val userRefference = FirebaseAuth.getInstance().currentUser!!.uid;

            val stup = Stup(et_url_stup_data.text.toString(), et_rasa_stup_data.text.toString(), sw_stadiu.isEnabled,)



            FirebaseUtils().fireStoreDatabase.collection(userRefference).add(stup).addOnSuccessListener {
                Toast.makeText(this, "Ai creat un stup nou", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Eroare " + it, Toast.LENGTH_LONG).show()
            }

    }

    private fun createFrames(){
        val buttons = listOf<Button>(btn_create_frame1,btn_create_frame2,btn_create_frame3, btn_create_frame4,btn_create_frame5,btn_create_frame6,btn_create_frame7,btn_create_frame8,btn_create_frame9,btn_create_frame10)
        val styleButtons = listOf<Button>(btn_create_puiet, btn_create_miere, btn_create_pastura)

        val buttonTags : MutableList<String> = mutableListOf()

        for (buton in buttons){

            buttonTags.add(buton.tag.toString())



//            buton.setOnClickListener {
//                for (styleButton in styleButtons){
//                    styleButton.visibility = View.VISIBLE
//                    styleButton.setOnClickListener {
//                        if (styleButton.tag == "puiet"){
//
//                        }
//                    }
//                }

            }

        Toast.makeText(this, "Button: $buttonTags", Toast.LENGTH_LONG).show()
        }
}