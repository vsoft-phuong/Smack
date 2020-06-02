package com.chatcore.smack.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chatcore.smack.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginBtnClicked(view: View){

    }
    fun signupBtnClicked(view: View){
        val createUserActivity = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserActivity)
    }
}
