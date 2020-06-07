package com.chatcore.smack.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chatcore.smack.R
import com.chatcore.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginBtnClicked(view: View){
        val email = emailEdt.text.toString()
        val password = passwordEdt.text.toString()

        AuthService.loginUser(this, email, password){loginSuccess ->
            if(loginSuccess){
                AuthService.findUserByEmail(this, email){found ->
                    if(found){
                        finish()
                    }

                }
            }

        }
    }
    fun signupBtnClicked(view: View){
        val createUserActivity = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserActivity)
        finish()
    }
}
