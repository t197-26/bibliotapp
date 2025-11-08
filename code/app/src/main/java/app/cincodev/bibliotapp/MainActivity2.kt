package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {
    lateinit var tvResp: TextView
    lateinit var etPrompt: EditText
    lateinit var btnSend: Button
    lateinit var generative: GenerativeModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        tvResp = findViewById(R.id.tvResp)
        etPrompt = findViewById(R.id.etPrompt)
        btnSend = findViewById(R.id.btnSend1)
        generative = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = ""
        )
    }

    override fun onStart() {
        super.onStart()

        btnSend.setOnClickListener {
            lifecycleScope.launch {
                var response = generative.generateContent("oi")

                tvResp.text = response.text.toString()
            }
        }
    }
}