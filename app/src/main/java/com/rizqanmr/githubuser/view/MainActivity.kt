package com.rizqanmr.githubuser.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.model.User
import com.rizqanmr.githubuser.viewmodel.UserAdapter
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.client.params.ClientPNames
import kotlinx.android.synthetic.main.activity_main.*
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var userAdapter: UserAdapter
    private var noInternetSnackbar: NoInternetSnackbar? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = resources.getString(R.string.app_name)
        userAdapter = UserAdapter(listUser)
        rv_user.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }
        try {
            getDataUser()
            searchUser()
        } catch (e: Throwable){
            e.printStackTrace()
            Toast.makeText(this@MainActivity, resources.getString(R.string.toast_no_internet),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun searchUser(){
        search_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    listUser.clear()
                    getDataSearchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun getDataSearchUser(username: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token b5e09adf22376d5b1420f711b6aa2ae1f2f34598")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDataUserDetail(username)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : " + resources.getString(R.string.bad_request)
                    403 -> "$statusCode : " + resources.getString(R.string.forbidden)
                    404 -> "$statusCode : " + resources.getString(R.string.not_found)
                    else -> "$statusCode : ${error.message + " GIT"}"
                }
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getDataUser() {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token b5e09adf22376d5b1420f711b6aa2ae1f2f34598")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        client.httpClient.params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true)
        client.setTimeout(30000)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDataUserDetail(username)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : " + resources.getString(R.string.bad_request)
                    403 -> "$statusCode : " + resources.getString(R.string.forbidden)
                    404 -> "$statusCode : " + resources.getString(R.string.not_found)
                    else -> "$statusCode : ${error.message + " GIT"}"
                }
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })

    }

    private fun getDataUserDetail(id: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token b5e09adf22376d5b1420f711b6aa2ae1f2f34598")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
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
                    val isFav = "0"
                    listUser.add(
                        User(
                            name,
                            username,
                            avatar,
                            company,
                            location,
                            repository,
                            followers,
                            following,
                            isFav
                        )
                    )
                    showRVUser()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : " + resources.getString(R.string.bad_request)
                    403 -> "$statusCode : " + resources.getString(R.string.forbidden)
                    404 -> "$statusCode : " + resources.getString(R.string.not_found)
                    else -> "$statusCode : ${error.message + " DETAIL"}"
                }
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }


    private fun showRVUser(){
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = userAdapter

        userAdapter.setOnItemClickListener(object : UserAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                Log.v("ITEM", "onItemClick $position")

                val selectedUser: User = listUser[position]
                val toDetail = Intent(this@MainActivity, DetailActivity::class.java)
                toDetail.putExtra("DATAUSER", selectedUser)
                startActivity(toDetail)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_favorite_user ->{
                val toFavorite = Intent(this, FavActivity::class.java)
                startActivity(toFavorite)
                true
            }
            R.id.menu_setting->{
                val toSetting = Intent(this, SettingActivity::class.java)
                startActivity(toSetting)
                true
            }
            else -> true
        }
    }

    override fun onResume() {
        super.onResume()
        noInternetSnackbar =
            NoInternetSnackbar.Builder(this, findViewById(android.R.id.content))
                .apply {
                    indefinite = true
                    noInternetConnectionMessage = resources.getString(R.string.no_internet_conn_msg)
                    onAirplaneModeMessage = resources.getString(R.string.on_airplane_mode_msg)
                    snackbarActionText = resources.getString(R.string.settings)
                    showActionToDismiss = false
                    snackbarDismissActionText = resources.getString(R.string.ok)
                }
                .build()
    }

    override fun onPause() {
        super.onPause()
        noInternetSnackbar?.destroy()
    }
}

