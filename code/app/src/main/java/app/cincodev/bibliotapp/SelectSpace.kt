package app.cincodev.bibliotapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class SelectSpace : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var salasGridLayout: GridLayout
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_space)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnReservar = findViewById<Button>(R.id.btnReservar)
        btnReservar.setOnClickListener {
            showConfirmationDialog()
        }


        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        val tvStartHour = findViewById<TextView>(R.id.tvStartHour)
        val tvEndHour = findViewById<TextView>(R.id.tvEndHour)

        val selectedDate = intent.getStringExtra("selected_date")
        val startHour = intent.getStringExtra("start_hour")
        val endHour = intent.getStringExtra("end_hour")

        tvSelectedDate.text = selectedDate

        if (startHour != null) {
            tvStartHour.text = formatHour(startHour)
        }
        if (endHour != null) {
            tvEndHour.text = formatHour(endHour)
        }
    }

    override fun onStart() {
        salasGridLayout = findViewById(R.id.gridSalas)

        db.collection("espacos")
            .document("biblioteca")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                    val salas = document.get("salas") as? List<String>
                    salas?.let {
                        preencheGrid(it)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Erro: ", e)
            }

        arrowBackButtonView = findViewById(R.id.LoginArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        super.onStart()
    }

    private fun preencheGrid(salas: List<String>) {
        salasGridLayout.removeAllViews()

        salas.forEachIndexed { index, sala ->
            val ocupada = index % 2 == 0

            val button = Button(this, null).apply {
                text = sala

                background = ContextCompat.getDrawable(this@SelectSpace, if (ocupada) R.drawable.tag_background_yellow else R.drawable.bg_rounded_button)

                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }

                setOnClickListener {
                }
            }

            salasGridLayout.addView(button)
        }
    }

    private fun formatHour(stringHour: String): String {
        val hour = stringHour.toIntOrNull()
        if (hour == null) return "00:00"
        val amPm = if (hour < 12) "AM" else "PM"
        val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format("%02d:00 %s", h, amPm)
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar reserva")
            .setMessage("Você tem certeza que deseja reservar este espaço?")
            .setPositiveButton("Sim") { dialog, _ ->
                Toast.makeText(this, "Espaço reservado com sucesso!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}