package com.chatcore.smack.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chatcore.smack.R
import com.chatcore.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }


    fun loginBtnClicked(view: View){
        enableProgress(true)
        val email = emailEdt.text.toString()
        val password = passwordEdt.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            hideKeyboard()
            AuthService.loginUser(email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this, email) { found ->
                        if (found) {
                            finish()
                        } else {
                            errorToast()
                        }

                    }
                } else {
                    errorToast()
                }

            }
        }else{
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_SHORT).show()
        }
    }
    fun signupBtnClicked(view: View){
        val createUserActivity = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserActivity)
        finish()
    }


    fun errorToast() {
        Toast.makeText(
            this, "Something went wrong, please try again.",
            Toast.LENGTH_SHORT
        ).show()
        enableProgress(false)
    }
    fun enableProgress(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }

        loginBtn.isEnabled = !enable
        signupBtn.isEnabled = !enable
    }

    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }
    }
}
