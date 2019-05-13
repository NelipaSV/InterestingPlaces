package com.nsv.interestingplaces.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.nsv.interestingplaces.R
import com.nsv.interestingplaces.constant.Constant
import com.nsv.interestingplaces.interfaces.MakeRoute

class ScreenSlidePageFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDistance: TextView
    private lateinit var btnMap: Button
    private lateinit var route: MakeRoute

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MakeRoute){route = context}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.item_layout, container, false)

        tvName = view.findViewById(R.id.tv_name_place)
        tvAddress = view.findViewById(R.id.tv_adress_place)
        tvDescription = view.findViewById(R.id.tv_description_place)
        tvDistance = view.findViewById(R.id.tv_distance_to)
        btnMap = view.findViewById(R.id.button)

        btnMap.setOnClickListener { route.makeRoute() }
        setText()

        return view
    }

    private fun setText(){
        val name = arguments?.getString(Constant.NAME)
        val address = arguments?.getString(Constant.ADDRESS)
        val description = arguments?.getString(Constant.DESCRIPTION)
        val distance = arguments?.getString(Constant.DISTANCE)

        tvName.text = name
        tvAddress.text = address
        tvDescription.text = description
        tvDistance.text = distance
    }
}