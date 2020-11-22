package com.rizqanmr.githubuser.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.model.User
import com.rizqanmr.githubuser.viewmodel.ViewPagerDetailAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = resources.getString(R.string.detail_user)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            setDisplayHomeAsUpEnabled(true)
        }
        getData()
        viewPagerConfig()
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    private fun getData() {
        val dataUser = intent.extras?.getParcelable<User>("DATAUSER") as User
        tvNameDetail.text = resources.getString(R.string.round_brackets, dataUser.name)
        tvUsernameDetail.text = dataUser.username
        tvCompanyDetail.text = dataUser.company
        tvLocationDetail.text = dataUser.location
        tvRepositoryDetail.text = dataUser.repository.toString()
        tvFollowersDetail.text = dataUser.followers.toString()
        tvFollowingDetail.text = dataUser.following.toString()
        Glide.with(this).load(dataUser.avatar.toString())
            .into(ivAvatarDetail)
    }

}