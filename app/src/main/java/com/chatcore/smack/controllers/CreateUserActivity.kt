package com.chatcore.smack.controllers

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chatcore.smack.R
import com.chatcore.smack.services.AuthService
import com.chatcore.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        progressBar.visibility = View.INVISIBLE
    }

    var avatarUser = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"
    fun avatarImgClicked(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)


        if (color == 0) {
            avatarUser = "light$avatar"
        } else {
            avatarUser = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(avatarUser, "drawable", packageName)
        avatarImg.setImageResource(resourceId)
    }

    fun generateBackgroundBtnClicked(view: View) {
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)

        avatarImg.setBackgroundColor(Color.rgb(r, g, b))

        val saveR = r.toDouble() / 255
        val saveG = g.toDouble() / 255
        val saveB = b.toDouble() / 255
        avatarColor = "[$saveR, $saveG, $saveB, 1]"
        println(avatarColor)

    }

    fun createUserBtnClicked(view: View) {
        enableProgress(true)
        val username = usernameEdt.text.toString()
        val email = emailEdt.text.toString()
        val password = passwordEdt.text.toString()

        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email, password) { loginSuccess ->
                        if (loginSuccess) {
//                        println(AuthService.userEmail)
//                        println(AuthService.authToken)
                            AuthService.createUser(
                                username,
                                email,
                                avatarUser,
                                avatarColor
                            ) { createSuccess ->
                                if (createSuccess) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableProgress(false)
                                    finish()
                                } else {
                                    errorToast()
                                }

                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }

            }
        } else {
            Toast.makeText(
                this, "Please fill in all",
                Toast.LENGTH_SHORT
            ).show()
            enableProgress(false)
        }


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
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }

        createUserBtn.isEnabled = !enable
        avatarImg.isEnabled = !enable
        generateBackgroundBtn.isEnabled = !enable
    }

}
