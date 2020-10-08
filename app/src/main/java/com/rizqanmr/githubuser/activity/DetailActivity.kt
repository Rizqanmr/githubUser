package com.rizqanmr.githubuser.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.model.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        actionBar()
        getData()
    }

    private fun actionBar(){
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Detail User"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun getData(){
        val dataUser = intent.extras?.getParcelable<User>("DATAUSER") as User
        tv_name_detail.text = dataUser.name
        tv_username_detail.text = "( " + dataUser.username + " )"
        tv_company_detail.text = dataUser.company
        tv_location_detail.text = dataUser.location
        tv_repository_detail.text = dataUser.repository
        tv_followers_detail.text = dataUser.followers
        tv_following_detail.text = dataUser.following
        Glide.with(this).load(dataUser.avatar)
            .into(iv_avatar_detail)
    }
}