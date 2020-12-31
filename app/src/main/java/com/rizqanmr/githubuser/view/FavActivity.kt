package com.rizqanmr.githubuser.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.db.UserContract.FavColumns.Companion.CONTENT_URI
import com.rizqanmr.githubuser.model.User
import com.rizqanmr.githubuser.utils.MappingHelper
import com.rizqanmr.githubuser.viewmodel.FavAdapter
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavActivity : AppCompatActivity() {
    private lateinit var favAdapter: FavAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = resources.getString(R.string.favorite)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            setDisplayHomeAsUpEnabled(true)
        }
        favAdapter = FavAdapter(this)
        rvFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavActivity)
            setHasFixedSize(true)
            adapter = favAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
        }
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadDataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadDataAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                favAdapter.listUsers = list
            }
        }
    }

    private fun loadDataAsync() {
        GlobalScope.launch(Dispatchers.Main){
            pbFavorite.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            pbFavorite.visibility = View.INVISIBLE
            if (favData.size > 0) {
                favAdapter.listUsers = favData
            } else {
                favAdapter.listUsers = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSnackbarMessage(pesan: String) {
        Snackbar.make(rvFavorite, pesan, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favAdapter.listUsers)
    }

    override fun onResume() {
        super.onResume()
        loadDataAsync()
    }
}