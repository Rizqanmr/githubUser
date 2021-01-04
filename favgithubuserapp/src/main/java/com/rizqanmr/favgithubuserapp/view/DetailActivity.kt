package com.rizqanmr.favgithubuserapp.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rizqanmr.favgithubuserapp.R
import com.rizqanmr.favgithubuserapp.model.User
import com.rizqanmr.favgithubuserapp.viewmodel.ViewPagerDetailAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var imageAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = resources.getString(R.string.detail_user)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            setDisplayHomeAsUpEnabled(true)
        }

        setDataObject()
        viewPagerConfig()

    }

    private fun setDataObject() {
        val favUser = intent.getParcelableExtra(EXTRA_NOTE) as User
        tvNameDetail.text = favUser.name.toString()
        tvUsernameDetail.text = favUser.username.toString()
        tvCompanyDetail.text = favUser.company.toString()
        tvLocationDetail.text = favUser.location.toString()
        tvRepositoryDetail.text = favUser.repository.toString()
        tvFollowersDetail.text = favUser.followers.toString()
        tvFollowingDetail.text = favUser.following.toString()
        Glide.with(this).load(favUser.avatar.toString())
            .error(R.drawable.ic_launcher_round_github_user)
            .into(ivAvatarDetail)
        imageAvatar = favUser.avatar.toString()

        Log.v("DETAIL", "Link img: $imageAvatar")
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

}