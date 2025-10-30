package com.example.cubeguide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class StopwatchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val mActivity = activity as AppCompatActivity
        mActivity.setSupportActionBar(toolbar);
        var actionBar : ActionBar? = mActivity.supportActionBar
        if(actionBar != null){
            actionBar.title = "Секундомер"
        }
        return view
    }
}