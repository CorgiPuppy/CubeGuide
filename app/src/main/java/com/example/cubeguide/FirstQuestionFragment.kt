package com.example.cubeguide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar

class FirstQuestionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val mActivity = activity as AppCompatActivity
        mActivity.setSupportActionBar(toolbar);
        var actionBar : ActionBar? = mActivity.supportActionBar
        if(actionBar != null){
            actionBar.title = "Вопрос 1 из 6"
        }

        return view
    }
}