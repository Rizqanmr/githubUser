package com.rizqanmr.githubuser.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rizqanmr.githubuser.R
import com.rizqanmr.githubuser.view.FollowersFragment
import com.rizqanmr.githubuser.view.FollowingFragment

class ViewPagerDetailAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val pages = listOf(
        FollowersFragment(),
        FollowingFragment()
    )

    @StringRes
    private val tabTitle = intArrayOf(
        R.string.followers,
        R.string.following
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }
}