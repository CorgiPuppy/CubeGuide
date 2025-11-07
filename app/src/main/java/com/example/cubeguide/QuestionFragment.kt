package com.example.cubeguide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar

class QuestionFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_question, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        val mActivity = activity as AppCompatActivity
        mActivity.setSupportActionBar(toolbar);
        var actionBar: ActionBar? = mActivity.supportActionBar

        actionBar?.title = viewModel.getProgressText()

        setupQuestion(view)

        return view
    }

    private fun setupQuestion(view: View) {
        val question = viewModel.getCurrentQuestion()

        val questionText = view.findViewById<TextView>(R.id.question_text)
        val questionImage = view.findViewById<ImageView>(R.id.question_image)
        val answerInput = view.findViewById<EditText>(R.id.answer_input)
        val nextButton = view.findViewById<Button>(R.id.next_button)

        questionText.text = question.text

        if (question.type == "image" && question.imageRes != null) {
            questionImage.setImageResource(question.imageRes)
            questionImage.visibility = View.VISIBLE
        } else {
            questionImage.visibility = View.GONE
        }

        nextButton.setOnClickListener {
            val userAnswer = answerInput.text.toString().trim()

            if (userAnswer.isNotEmpty()) {
                viewModel.checkAnswer(userAnswer)

                if (viewModel.moveToNextQuestion())
                    view.findNavController().navigate(
                        R.id.action_questionFragment_self
                    )
                else {
                    view.findNavController().navigate(
                        R.id.action_questionFragment_to_resultsFragment
                    )
                }
            } else
                answerInput.error = "Введите ответ"
        }
    }
}