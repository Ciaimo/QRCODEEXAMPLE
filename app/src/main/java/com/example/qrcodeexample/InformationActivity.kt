package com.example.qrcodeexample

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.collection.arraySetOf
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.grpc.Deadline
import kotlinx.android.synthetic.main.activity_information.*

class InformationActivity : AppCompatActivity() {

        private var RASA = "rasa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val db = FirebaseFirestore.getInstance()

        var numarStup = intent.getStringExtra("numar_stup")
        var rasaStup = intent.getStringExtra("rasa_stup")
        var stadiuStup = intent.getBooleanExtra("stadiu_stup", false)




        tv_numar_stup.text = numarStup
        tv_rasa.text = rasaStup

        if(stadiuStup){
            tv_stadiu.text = "Imperecheata"
        }else{
            tv_stadiu.text = "Neimperecheata"
        }


        btn_information_edit_rasa.setOnClickListener(){

            initiateEditingText(btn_information_edit_rasa, btn_information_save_data, et_information_rasa)

            btn_information_save_data.setOnClickListener(){
                saveDataET(btn_information_edit_rasa, btn_information_save_data, et_information_rasa, db, numarStup.toString(), tv_rasa, RASA)
            }
        }




        btn_information_back.setOnClickListener(){
            startActivity(Intent(this@InformationActivity, MainActivity::class.java))
            finish()
        }

        CreateButton(button1,button2,button3,button4,button5,button6,button7,button8,button9,button10)


//        btn_information_add.setOnClickListener {
////            CreateButton(button1,button2,button3,button4,button5,button6,button7,button8,button9,button10)
//
//        }

    }



    private fun initiateEditingText(buttonEdit: Button, buttonSave: Button, editText: EditText){
        buttonEdit.isClickable = false
        buttonEdit.visibility = View.INVISIBLE

        buttonSave.isClickable = true
        buttonSave.visibility = View.VISIBLE

        editText.isClickable = true
        editText.visibility = View.VISIBLE
    }

    private fun disableEditing(buttonEdit: Button, buttonSave: Button, editText: EditText){
        buttonEdit.isClickable = false
        buttonEdit.visibility = View.INVISIBLE

        buttonSave.isClickable = true
        buttonSave.visibility = View.VISIBLE

        editText.isClickable = false
        editText.visibility = View.INVISIBLE
    }

    private fun saveDataET(buttonEdit: Button, buttonSave: Button, editText: EditText, db: FirebaseFirestore, numarStup: String, textView: TextView, whatToEdit: String){

        val userRefference = FirebaseAuth.getInstance().currentUser!!.uid;

        buttonSave.setOnClickListener(){
            when{
                TextUtils.isEmpty(editText.text.toString().trim() {it <= ' '}) -> {
                    disableEditing(buttonSave, buttonEdit, editText)
                    Toast.makeText(this, "Te rog introdu o rasa", Toast.LENGTH_LONG).show()
                }

                else ->{

                    db.collection(userRefference).get().addOnSuccessListener {
                        for(document in it){
                            if(document.getString("numarStup") == numarStup){
                                val doc = db.collection(userRefference).document(document.id)
                                doc.update(whatToEdit, editText.text.toString())
                                textView.text = document.getString(whatToEdit)
                            }
                        }
                    }

                    disableEditing(buttonSave, buttonEdit, editText);


                }
            }
        }
    }





    private fun CreateButton(bd1 : Button, bd2 : Button, bd3 : Button, bd4 : Button, bd5 : Button, bd6 : Button, bd7 : Button, bd8 : Button, bd9 : Button, bd10 : Button){
        val listOfButtons = listOf(bd1, bd2, bd3, bd4, bd5, bd6, bd7, bd8, bd9, bd10)

        val styleButtons = listOf(btn_information_puiet,btn_information_pastura,btn_information_miere)

        val puiet = R.id.puiet
        val miere = R.id.miere

        for(buton in listOfButtons){
            buton.setOnClickListener {
                linearLayout.visibility = View.VISIBLE
                btn_information_save_frame_style.visibility = View.VISIBLE
                for (styleButton in styleButtons){
                    styleButton.setOnClickListener {
                        if (styleButton.tag == "puiet"){
                            buton.setTag(puiet, "puiet")
                            buton.setBackgroundColor(Color.YELLOW)
                            Toast.makeText(this, buton.background.toString(), Toast.LENGTH_LONG).show()
                        }
                        if (styleButton.tag == "pastura"){
                            buton.setTag(miere, "pastura")
                        }
                    }
                }
            }
        }

        btn_information_save_frame_style.setOnClickListener {
            linearLayout.visibility = View.INVISIBLE
            btn_information_save_frame_style.visibility = View.INVISIBLE
        }

    }

    private fun CreateFrames(){

    }



}