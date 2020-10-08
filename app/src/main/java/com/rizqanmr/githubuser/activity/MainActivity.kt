package com.rizqanmr.githubuser.activity

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.adapter.UserAdapter
import com.rizqanmr.githubuser.model.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var dName: Array<String>
    private lateinit var dUsername: Array<String>
    private lateinit var dLocation: Array<String>
    private lateinit var dCompany: Array<String>
    private lateinit var dRepository: Array<String>
    private lateinit var dFollowers: Array<String>
    private lateinit var dFollowing: Array<String>
    private lateinit var dAvatar: TypedArray
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userAdapter = UserAdapter(users)

        rv_user.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
            prepare()
            addData()
            adapter = userAdapter
        }

        userAdapter.setOnItemClickListener(object : UserAdapter.ClickListener{
            override fun onItemClick(v: View, position: Int) {
                Log.v("ITEM", "onItemClick $position")

                val selectedUser: User = users[position]
                val toDetail = Intent(this@MainActivity, DetailActivity::class.java)
                toDetail.putExtra("DATAUSER", selectedUser)
                startActivity(toDetail)
            }

        })
    }


    private fun prepare(){
        dName = resources.getStringArray(R.array.name)
        dUsername = resources.getStringArray(R.array.username)
        dAvatar = resources.obtainTypedArray(R.array.avatar)
        dLocation = resources.getStringArray(R.array.location)
        dCompany = resources.getStringArray(R.array.company)
        dRepository = resources.getStringArray(R.array.repository)
        dFollowers = resources.getStringArray(R.array.followers)
        dFollowing = resources.getStringArray(R.array.following)
    }

    private fun addData(){
        for (position in dName.indices){
            val user = User(
                dAvatar.getResourceId(position, -1),
                dName[position],
                dUsername[position],
                dLocation[position],
                dCompany[position],
                dRepository[position],
                dFollowers[position],
                dFollowing[position]
            )
            users.add(user)
        }
    }

}