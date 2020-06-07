package com.chatcore.smack.controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chatcore.smack.R
import com.chatcore.smack.models.Channel
import com.chatcore.smack.services.AuthService
import com.chatcore.smack.services.MessageService
import com.chatcore.smack.services.UserDataService
import com.chatcore.smack.utilities.BROADCAST_USER_DATA_CHANGE
import com.chatcore.smack.utilities.SOCKET_URL
import com.google.android.material.navigation.NavigationView
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<Channel>
    private fun setupAdapter() {
        channelAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        /*val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        setupAdapter()


        socket.connect()
        socket.on("channelCreated", onNewChannel)

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )
    }

    private val onNewChannel = Emitter.Listener { args ->
//        println(args[0] as String)
        runOnUiThread {
            val name = args[0] as String
            val desc = args[1] as String
            val id = args[2] as String

            val newChannel = Channel(name, desc, id)
            MessageService.channels.add(newChannel)
            Toast.makeText(this, "Created $name", Toast.LENGTH_SHORT).show()
            channelAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.off("channelCreated", onNewChannel)
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (AuthService.isLoggedIn) {
                nameTxt.text = UserDataService.name
                emailTxt.text = UserDataService.email
                val resourceId =
                    resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                avatarImg.setImageResource(resourceId)
                avatarImg.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtn.text = "Logout"

                //load list channel
                p0?.let {
                    MessageService.getChannels(it) { success ->
                        if(success){
                            channelAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnClicked(view: View) {
        if (AuthService.isLoggedIn) {
            //logout
            UserDataService.logout()
            nameTxt.text = "Login"
            emailTxt.text = ""
            avatarImg.setImageResource(R.drawable.profiledefault)
            avatarImg.setBackgroundColor(Color.TRANSPARENT)
            loginBtn.text = "Login"
            MessageService.channels.clear()
            channelAdapter.notifyDataSetChanged()
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelImgClicked(view: View) {
        if (AuthService.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("Add") { dialogInterface, i ->
                    val name = dialogView.findViewById<EditText>(R.id.editChannelName)
                    val description = dialogView.findViewById<EditText>(R.id.editChannelDescription)

                    val channelName = name.text.toString()
                    val channelDesc = description.text.toString()

                    //create channel with name and description
                    socket.emit("newChannel", channelName, channelDesc)

                    hideKeyboard()
                    dialogInterface.dismiss()

                }
                .setNegativeButton("Sign in") { dialogInterface, i ->
                    hideKeyboard()
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    fun sendBtnClick(view: View) {
        hideKeyboard()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        }
    }
}
