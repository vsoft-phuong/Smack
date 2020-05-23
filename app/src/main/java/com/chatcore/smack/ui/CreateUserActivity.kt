package com.chatcore.smack.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chatcore.smack.R
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    var avatarUser = "profiledefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"
    fun avatarImgClicked(view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)


        if(color == 0){
            avatarUser = "light$avatar"
        }else{
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

        avatarImg.setBackgroundColor(Color.rgb(r,g,b))

        val saveR = r.toDouble()/255
        val saveG = g.toDouble()/255
        val saveB = b.toDouble()/255
        avatarColor = "[$saveR, $saveG, $saveB, 1]"
        println(avatarColor)

    }

    fun createUserBtnClicked(view: View) {

    }


}
