package com.rizqanmr.githubuser.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.model.User
import de.hdodenhof.circleimageview.CircleImageView


class UserAdapter (var listUser: ArrayList<User>?):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = listUser?.get(position)
        data?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return listUser?.size ?: 0
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class UserViewHolder (v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        private val ivAvatar: CircleImageView = v.findViewById(R.id.iv_avatar)
        private val tvName: TextView = v.findViewById(R.id.name)
        private val tvUsername: TextView = v.findViewById(R.id.username)

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }

        fun bind(user: User){
            tvName.text = user.name
            tvUsername.text = user.username
            Glide.with(itemView.context).load(user.avatar)
                .apply(RequestOptions().override(250,250))
                .into(ivAvatar)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v,adapterPosition)
            }
        }
    }

    interface ClickListener {
        fun onItemClick(v: View,position: Int)
    }


}

