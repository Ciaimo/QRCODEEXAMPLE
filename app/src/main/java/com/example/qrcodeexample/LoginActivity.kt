package com.example.qrcodeexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        btn_googlesining.setSize(SignInButton.SIZE_STANDARD)
        btn_googlesining.setOnClickListener(){
            var signInAccount = mGoogleSignInClient.signInIntent
            startActivity(signInAccount)
        }

        tv_register.setOnClickListener(){
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        btn_login.setOnClickListener() {
            when {
                //We check if they actually etered an email or a password
                TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Te rog introdu email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //^Same here^
                TextUtils.isEmpty(et_login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Te rog introdu o parola",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    var email: String = et_login_email.text.toString().trim { it <= ' ' }
                    var password: String = et_login_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                        if(task.isSuccessful){

                            Toast.makeText(this@LoginActivity, "Te-ai logat cu succes", Toast.LENGTH_SHORT).show()


                            var intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@LoginActivity, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        var account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private fun updateUI(account: GoogleSignInAccount?) {

    }

}