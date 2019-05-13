package com.nsv.interestingplaces.pagerAdapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.nsv.interestingplaces.fragment.ScreenSlidePageFragment
import com.nsv.interestingplaces.model.Item
import android.os.Bundle
import com.nsv.interestingplaces.constant.Constant


class ScreenSlidePagerAdapter(fm: FragmentManager, private val places: List<Item>) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = places.size

    override fun getItem(position: Int): Fragment {
        val screenSlidePageFragment = ScreenSlidePageFragment()
        val args = Bundle()
        val name = places[position].venue.name
        val address = places[position].venue.location.address
        val description = places[position].venue.categories[0].name
        val distance = places[position].venue.location.distance.toString()
        args.putString(Constant.NAME, name)
        args.putString(Constant.ADDRESS, address)
        args.putString(Constant.DESCRIPTION, description)
        args.putString(Constant.DISTANCE, distance)
        screenSlidePageFragment.arguments = args
        return screenSlidePageFragment
    }

    override fun getPageWidth(position: Int): Float {
        return 0.95f
    }


}