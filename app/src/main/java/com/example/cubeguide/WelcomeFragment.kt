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

class WelcomeFragment : Fragment() {
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
            actionBar.title = getString(R.string.app_name)
        }

        val testButton = view.findViewById<Button>(R.id.test_button)
        testButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_quizFragment)
        }

        val stopwatchButton = view.findViewById<Button>(R.id.stopwatch_button)
        stopwatchButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_stopwatchFragment)
        }

        val shareButton = view.findViewById<Button>(R.id.share_button)
        shareButton.setOnClickListener {
            val sendIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_message))
                type = "text/plain"
            }
            val shareIntent = android.content.Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        return view
    }
}