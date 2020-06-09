package com.chatcore.smack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chatcore.smack.R
import com.chatcore.smack.models.Message
import com.chatcore.smack.services.UserDataService

class MessageAdapter(val context:Context, val messages: ArrayList<Message>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgAvatar = itemView.findViewById<ImageView>(R.id.imgAvatar)
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtTime = itemView.findViewById<TextView>(R.id.txtTime)
        val txtMessage = itemView.findViewById<TextView>(R.id.txtMessage)

        fun bindMessage(context: Context, message: Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            imgAvatar.setImageResource(resourceId)
            imgAvatar.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            txtName.text = message.userName
            txtTime.text = message.timestamp
            txtMessage.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(context, messages[position])
    }
}