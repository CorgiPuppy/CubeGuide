package com.example.cubeguide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class ResultFragment : Fragment() {
    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val viewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        val tvScore = view.findViewById<TextView>(R.id.tv_score)
        tvScore.text = "Правильно: ${viewModel.score} из ${viewModel.getTotalQuestionsCount()}"

        val btnHome = view.findViewById<Button>(R.id.btn_home)
        btnHome.setOnClickListener {
            viewModel.currentIndex = 0
            viewModel.score = 0
            viewModel.isGameFinished = false

            findNavController().navigate(R.id.action_resultFragment_to_welcomeFragment)
        }

        return view
    }
}