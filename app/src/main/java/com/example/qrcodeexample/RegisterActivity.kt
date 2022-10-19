package com.example.qrcodeexample

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import kotlinx.android.synthetic.main.activity_register.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //The moment we press the Register button
        btn_register.setOnClickListener() {

            when {
                //We check if they actually etered an email or a password
                TextUtils.isEmpty(et_register_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Te rog introdu email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //^Same here^
                TextUtils.isEmpty(et_register_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Te rog introdu o parola",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    //We get the email and the password
                    var email: String = et_register_email.text.toString().trim { it <= ' ' }
                    var password: String = et_register_password.text.toString().trim { it <= ' ' }

                    //We create the actual register
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            //If registration is succsesfull
                            if (task.isSuccessful) {
                                //We get the actual user
                                var firebaseUser: FirebaseUser = task.result!!.user!!
                                firebaseUser.sendEmailVerification().addOnCompleteListener(){
                                    if(task.isSuccessful){
                                        Log.d(TAG, "Email sent!")
                                    }
                                }
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Te ai connectat cu succes!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }


    }
}