package com.example.cubeguide

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cubeguide.data.Category

class AddProductFragment : Fragment() {

    private lateinit var viewModel: ShopViewModel
    private var selectedImageUri: String = "" // Строка URI
    private var selectedCategoryId: Int = -1
    private lateinit var ivPreview: ImageView

    // Специальная штука для открытия галереи
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it.toString()
            Glide.with(this).load(it).into(ivPreview) // Показываем выбранное
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_product, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)

        ivPreview = view.findViewById(R.id.iv_preview)
        val btnPickImage = view.findViewById<Button>(R.id.btn_pick_image)
        val etName = view.findViewById<EditText>(R.id.et_name)
        val etDesc = view.findViewById<EditText>(R.id.et_desc)
        val etPrice = view.findViewById<EditText>(R.id.et_price)
        val spinner = view.findViewById<Spinner>(R.id.spinner_categories)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        // 1. Настраиваем Категории (берем из БД)
        viewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            if (categories.isEmpty()) {
                // Если БД пустая, создадим базовые категории
                viewModel.addCategory("Головоломки")
                viewModel.addCategory("Аксессуары")
            } else {
                // Заполняем спиннер
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }

        // Слушатель выбора в спиннере
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category = parent?.getItemAtPosition(position) as Category
                selectedCategoryId = category.id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 2. Кнопка выбора фото
        btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // 3. Сохранение
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val desc = etDesc.text.toString()
            val priceStr = etPrice.text.toString()

            if (name.isNotEmpty() && priceStr.isNotEmpty() && selectedCategoryId != -1) {
                // Если картинку не выбрали, используем пустую строку (адаптер подставит заглушку)
                viewModel.addProductToDb(name, desc, priceStr.toInt(), selectedImageUri, selectedCategoryId)
                Toast.makeText(context, getString(R.string.toast_saved), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // Возвращаемся назад в магазин
            } else {
                Toast.makeText(context, getString(R.string.toast_error_fields), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}