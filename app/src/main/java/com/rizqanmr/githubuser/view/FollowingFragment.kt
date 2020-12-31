package com.rizqanmr.githubuser.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.model.User
import com.rizqanmr.githubuser.viewmodel.UserAdapter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import org.json.JSONObject

class FollowingFragment : Fragment() {

    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var userAdapter: UserAdapter
    private var userFav: User? = null
    private lateinit var dataUser: User
    private lateinit var dataUser2: User

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        const val EXTRA_DATA = "DATAUSER"
        const val EXTRA_NOTE = "extra_note"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = UserAdapter(listUser)
        rv_following.apply { layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            ) }
        listUser.clear()
        userFav = requireActivity().intent.getParcelableExtra(FollowersFragment.EXTRA_NOTE)
        if (userFav != null){
            dataUser = requireActivity().intent.getParcelableExtra(FollowersFragment.EXTRA_NOTE) as User
            getDataUser(dataUser.username.toString())
        } else{
            dataUser2 = requireActivity().intent.extras?.getParcelable<User>(FollowersFragment.EXTRA_DATA) as User
            getDataUser(dataUser2.username.toString())
        }
    }

    private fun getDataUser(username: String) {
        try {
            pb_following.visibility = View.VISIBLE
            val client = AsyncHttpClient()
            client.addHeader("Authorization", "token 4eb088281be97614c40f9494a3a4ee87107d82f5")
            client.addHeader("User-Agent", "request")
            val url = "https://api.github.com/users/$username/following"
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray) {
                    pb_following.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    try {
                        val jsonArray = JSONArray(result)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username: String = jsonObject.getString("login")
                            getDataUserDetail(username)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
                ) {
                    pb_following.visibility = View.INVISIBLE
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : "+resources.getString(R.string.bad_request)
                        403 -> "$statusCode : "+resources.getString(R.string.forbidden)
                        404 -> "$statusCode : "+resources.getString(R.string.not_found)
                        else -> "$statusCode : ${error.message + " GIT"}"
                    }
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                        .show()
                }
            })
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun getDataUserDetail(id: String) {
        try {
            pb_following.visibility = View.VISIBLE
            val client = AsyncHttpClient()
            client.addHeader("Authorization", "token 4eb088281be97614c40f9494a3a4ee87107d82f5")
            client.addHeader("User-Agent", "request")
            val url = "https://api.github.com/users/$id"
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
                ) {
                    pb_following.visibility = View.INVISIBLE
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    try {
                        val jsonObject = JSONObject(result)
                        val name: String? = jsonObject.getString("name").toString()
                        val username: String? = jsonObject.getString("login").toString()
                        val avatar: String? = jsonObject.getString("avatar_url").toString()
                        val company: String? = jsonObject.getString("company").toString()
                        val location: String? = jsonObject.getString("location").toString()
                        val repository: Int = jsonObject.getInt("public_repos")
                        val followers: Int = jsonObject.getInt("followers")
                        val following: Int = jsonObject.getInt("following")
                        listUser.add(
                            User(
                                name,
                                username,
                                avatar,
                                company,
                                location,
                                repository,
                                followers,
                                following
                            )
                        )
                        showRVUser()
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
                ) {
                    pb_following.visibility = View.INVISIBLE
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : "+resources.getString(R.string.bad_request)
                        403 -> "$statusCode : "+resources.getString(R.string.forbidden)
                        404 -> "$statusCode : "+resources.getString(R.string.not_found)
                        else -> "$statusCode : ${error.message + " DETAIL"}"
                    }
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                        .show()
                }
            })
        } catch (e: Exception){
            e.printStackTrace()
        }
    }


    private fun showRVUser(){
        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = userAdapter

        userAdapter.setOnItemClickListener(object : UserAdapter.ClickListener{
            override fun onItemClick(v: View, position: Int) {
                Log.v("ITEM", "onItemClick $position")
            }

        })
    }

}