package com.rizqanmr.githubuser.view

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.AVATAR
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.COMPANY
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.CONTENT_URI
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.FAVORITE
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.FOLLOWERS
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.FOLLOWING
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.LOCATION
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.NAME
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.REPOSITORY
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.USERNAME
import com.rizqanmr.githubuser.db.UserHelper
import com.rizqanmr.githubuser.model.User
import com.rizqanmr.githubuser.viewmodel.ViewPagerDetailAdapter
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "DATAUSER"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
    }

    private var isFavorite = false
    private lateinit var gitHelper: UserHelper
    private var userFavorite: User? = null
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

        gitHelper = UserHelper.getInstance(applicationContext)
        gitHelper.open()
        userFavorite = intent.getParcelableExtra(EXTRA_NOTE)
        if (userFavorite != null) {
            setDataObject()
            isFavorite = true
            val checked: Int = R.drawable.ic_favorite_red_24
            fab_favorite.setImageResource(checked)
        } else {
            getData()
        }

        viewPagerConfig()
        fab_favorite.setOnClickListener(this)
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
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    private fun getData() {
        val dataUser = intent.extras?.getParcelable<User>(EXTRA_DATA) as User
        tvNameDetail.text = dataUser.name
        tvUsernameDetail.text = dataUser.username
        tvCompanyDetail.text = dataUser.company
        tvLocationDetail.text = dataUser.location
        tvRepositoryDetail.text = dataUser.repository.toString()
        tvFollowersDetail.text = dataUser.followers.toString()
        tvFollowingDetail.text = dataUser.following.toString()
        Glide.with(this).load(dataUser.avatar.toString())
            .error(R.drawable.ic_launcher_round_github_user)
            .into(ivAvatarDetail)
        imageAvatar = dataUser.avatar.toString()
    }

    override fun onClick(view: View?) {
        val checked: Int = R.drawable.ic_favorite_red_24
        val unChecked: Int = R.drawable.ic_twotone_favorite_24
        if (view?.id == R.id.fab_favorite){
            if (isFavorite){
                gitHelper.deleteById(userFavorite?.username.toString())
                Toast.makeText(this, resources.getString(R.string.deleted_favorite), Toast.LENGTH_SHORT).show()
                fab_favorite.setImageResource(unChecked)
                isFavorite = false
            } else {
                val dataName = tvNameDetail.text.toString()
                val dataUsername = tvUsernameDetail.text.toString()
                val dataAvatar = imageAvatar
                val dataCompany = tvCompanyDetail.text.toString()
                val dataLocation = tvLocationDetail.text.toString()
                val dataRepository = tvRepositoryDetail.text.toString()
                val dataFollowers = tvFollowersDetail.text.toString()
                val dataFollowing = tvFollowingDetail.text.toString()
                val dataFavorite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(AVATAR, dataAvatar)
                values.put(COMPANY, dataCompany)
                values.put(LOCATION, dataLocation)
                values.put(REPOSITORY, dataRepository)
                values.put(FOLLOWERS, dataFollowers)
                values.put(FOLLOWING, dataFollowing)
                values.put(FAVORITE, dataFavorite)

                isFavorite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, resources.getString(R.string.added_favorite), Toast.LENGTH_SHORT).show()
                fab_favorite.setImageResource(checked)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gitHelper.close()
    }

}