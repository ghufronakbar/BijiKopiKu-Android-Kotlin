package com.example.bijikopiku.ui

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bijikopiku.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            if (isAllQuestionsAnswered()) {
                handleSearch()
            } else {
                Toast.makeText(this, "Pastikan semua pertanyaan dijawab!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSingleSelection()
    }

    private fun isAllQuestionsAnswered(): Boolean {
        val questionGroups = listOf(
            listOf(binding.cbOften, binding.cbNotOften),
            listOf(binding.cbArabica, binding.cbRobusta),
            listOf(binding.cbLight, binding.cbMedium, binding.cbStrong),
            listOf(binding.cbAlways, binding.cbSometimes, binding.cbNever),
            listOf(
                binding.cbAcidic,
                binding.cbBitter,
                binding.cbCaramel,
                binding.cbChocolate,
                binding.cbFruity,
                binding.cbNutty
            )
        )

        for (group in questionGroups) {
            if (group.none { it.isChecked }) {
                return false
            }
        }
        return true
    }

    private fun handleSearch() {
        val isForCoffeeEnthusiast = binding.cbOften.isChecked
        val coffeeType = when {
            binding.cbArabica.isChecked -> "Arabica"
            binding.cbRobusta.isChecked -> "Robusta"
            else -> ""
        }
        val taste = when {
            binding.cbLight.isChecked -> "Light"
            binding.cbMedium.isChecked -> "Medium"
            binding.cbStrong.isChecked -> "Strong"
            else -> ""
        }
        val isItForSweet = binding.cbAlways.isChecked || binding.cbSometimes.isChecked
        val flavor = when {
            binding.cbAcidic.isChecked -> "Asam"
            binding.cbBitter.isChecked -> "Pahit"
            binding.cbCaramel.isChecked -> "Karamel"
            binding.cbChocolate.isChecked -> "Coklat"
            binding.cbFruity.isChecked -> "Buah"
            binding.cbNutty.isChecked -> "Kacang"
            else -> ""
        }

        // Kirim data ke ResultActivity
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("isForCoffeeEnthusiast", isForCoffeeEnthusiast)
            putExtra("coffeeType", coffeeType)
            putExtra("taste", taste)
            putExtra("isItForSweet", isItForSweet)
            putExtra("flavor", flavor)
        }
        startActivity(intent)
    }


    private fun setupSingleSelection() {
        val questionGroups = listOf(
            listOf(binding.cbOften, binding.cbNotOften),
            listOf(binding.cbArabica, binding.cbRobusta),
            listOf(binding.cbLight, binding.cbMedium, binding.cbStrong),
            listOf(binding.cbAlways, binding.cbSometimes, binding.cbNever),
            listOf(
                binding.cbAcidic,
                binding.cbBitter,
                binding.cbCaramel,
                binding.cbChocolate,
                binding.cbFruity,
                binding.cbNutty
            )
        )

        for (group in questionGroups) {
            group.forEach { checkBox ->
                checkBox.setOnClickListener {
                    group.forEach { other ->
                        if (other != checkBox) {
                            other.isChecked = false
                        }
                    }
                }
            }
        }
    }
}
