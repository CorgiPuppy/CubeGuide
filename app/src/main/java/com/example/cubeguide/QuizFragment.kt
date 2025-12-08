package com.example.cubeguide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout

class QuizFragment : Fragment() {
    private lateinit var viewModel: QuizViewModel

    private lateinit var tvProgress: TextView
    private lateinit var ivImage: ImageView
    private lateinit var tvQuestion: TextView
    private lateinit var layoutCheckboxes: LinearLayout
    private lateinit var layoutInput: TextInputLayout
    private lateinit var etAnswer: EditText
    private lateinit var btnNext: Button

    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        tvProgress = view.findViewById(R.id.tv_progress)
        ivImage = view.findViewById(R.id.iv_question_image)
        tvQuestion = view.findViewById(R.id.tv_question_text)
        layoutCheckboxes = view.findViewById(R.id.layout_checkboxes)
        layoutInput = view.findViewById(R.id.layout_input)
        etAnswer = view.findViewById(R.id.et_answer)
        btnNext = view.findViewById(R.id.btn_next)

        checkBoxes = listOf(
            view.findViewById(R.id.cb_option1),
            view.findViewById(R.id.cb_option2),
            view.findViewById(R.id.cb_option3),
            view.findViewById(R.id.cb_option4)
        )

        updateUI()

        btnNext.setOnClickListener {
            processAnswer()
        }

        return view
    }

    private fun updateUI() {
        val question = viewModel.getCurrentQuestion()

        tvProgress.text = "Вопрос ${viewModel.currentIndex + 1} из ${viewModel.getTotalQuestionsCount()}"
        tvQuestion.text = question.text

        ivImage.visibility = View.GONE
        layoutCheckboxes.visibility = View.GONE
        layoutInput.visibility = View.GONE
        etAnswer.text.clear()
        checkBoxes.forEach {
            it.isChecked = false
            it.visibility = View.GONE
        }

        if (question.imageResId != null) {
            ivImage.setImageResource(question.imageResId)
            ivImage.visibility = View.VISIBLE
        }

        when (question.type) {
            QuestionType.CHECKBOX -> {
                layoutCheckboxes.visibility = View.VISIBLE

                question.answers.forEachIndexed { index, text ->
                    if (index < checkBoxes.size) {
                        checkBoxes[index].text = text
                        checkBoxes[index].visibility = View.VISIBLE
                    }
                }
            }
            QuestionType.FREE_INPUT -> {
                layoutInput.visibility = View.VISIBLE
            }
        }
    }
}