package com.rizqanmr.favgithubuserapp.viewmodel

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizqanmr.favgithubuserapp.R
import com.rizqanmr.favgithubuserapp.model.User
import com.rizqanmr.favgithubuserapp.utils.CustomClickListener
import com.rizqanmr.favgithubuserapp.view.DetailActivity
import kotlinx.android.synthetic.main.item_user.view.*

class FavAdapter (private val activity: Activity):
    RecyclerView.Adapter<FavAdapter.UserViewHolder>() {

    var listUsers = ArrayList<User>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = this.listUsers.size

    inner class UserViewHolder (v: View) : RecyclerView.ViewHolder(v){
        fun bind(user: User){
            with(itemView){
                name.text = user.name
                username.text = user.username
                Glide.with(itemView.context).load(user.avatar)
                    .apply(RequestOptions().override(250,250))
                    .error(R.drawable.ic_launcher_round_github_user)
                    .into(iv_avatar)
                itemView.setOnClickListener(
                    CustomClickListener(adapterPosition, object : CustomClickListener.OnItemClickCallback{
                        override fun onItemClicked(view: View, position: Int) {
                            val toDetail = Intent(activity, DetailActivity::class.java)
                            toDetail.putExtra(DetailActivity.EXTRA_POSITION, position)
                            toDetail.putExtra(DetailActivity.EXTRA_NOTE, user)
                            activity.startActivity(toDetail)
                        }
                    }))
            }
        }

    }

}
