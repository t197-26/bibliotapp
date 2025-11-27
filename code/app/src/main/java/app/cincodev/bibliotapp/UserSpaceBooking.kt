package app.cincodev.bibliotapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserSpaceBooking : AppCompatActivity() {

    private lateinit var inputData: TextInputEditText
    private lateinit var inputHoraInicio: TextInputEditText
    private lateinit var inputHoraFim: TextInputEditText
    private lateinit var txtNomeEspaco: TextView
    private lateinit var btnConfirmar: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var espacoId: String = ""
    private var espacoNome: String = ""

    private val calendarioInicio = Calendar.getInstance()
    private val calendarioFim = Calendar.getInstance()

    private val sdfData = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val sdfHora = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_space_booking)

        inicializarViews()
        recuperarDadosIntent()
        configurarCliques()
    }

    private fun inicializarViews() {
        txtNomeEspaco = findViewById(R.id.txtNomeEspaco)
        inputData = findViewById(R.id.inputData)
        inputHoraInicio = findViewById(R.id.inputHoraInicio)
        inputHoraFim = findViewById(R.id.inputHoraFim)
        btnConfirmar = findViewById(R.id.btnConfirmarReserva)

        findViewById<ImageButton>(R.id.btnBackBooking).setOnClickListener { finish() }
    }

    private fun recuperarDadosIntent() {
        espacoId = intent.getStringExtra("ESPACO_ID") ?: ""
        espacoNome = intent.getStringExtra("ESPACO_NOME") ?: "Espaço"
        txtNomeEspaco.text = "Reservando: $espacoNome"

        val inicioMillis = intent.getLongExtra("DATA_INICIO_MILLIS", 0)
        val fimMillis = intent.getLongExtra("DATA_FIM_MILLIS", 0)

        if (inicioMillis > 0 && fimMillis > 0) {
            calendarioInicio.timeInMillis = inicioMillis
            calendarioFim.timeInMillis = fimMillis

            inputData.setText(sdfData.format(calendarioInicio.time))
            inputHoraInicio.setText(sdfHora.format(calendarioInicio.time))
            inputHoraFim.setText(sdfHora.format(calendarioFim.time))
        }
    }

    private fun configurarCliques() {
        inputData.setOnClickListener {
            val hoje = Calendar.getInstance()
            val anoIni = calendarioInicio.get(Calendar.YEAR)
            val mesIni = calendarioInicio.get(Calendar.MONTH)
            val diaIni = calendarioInicio.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, ano, mes, dia ->
                calendarioInicio.set(ano, mes, dia)
                calendarioFim.set(ano, mes, dia)
                inputData.setText(sdfData.format(calendarioInicio.time))
            }, anoIni, mesIni, diaIni)

            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        inputHoraInicio.setOnClickListener {
            TimePickerDialog(this, { _, hora, minuto ->
                calendarioInicio.set(Calendar.HOUR_OF_DAY, hora)
                calendarioInicio.set(Calendar.MINUTE, minuto)
                calendarioInicio.set(Calendar.SECOND, 0)
                inputHoraInicio.setText(String.format("%02d:%02d", hora, minuto))
            }, calendarioInicio.get(Calendar.HOUR_OF_DAY), calendarioInicio.get(Calendar.MINUTE), true).show()
        }

        inputHoraFim.setOnClickListener {
            TimePickerDialog(this, { _, hora, minuto ->
                calendarioFim.set(Calendar.HOUR_OF_DAY, hora)
                calendarioFim.set(Calendar.MINUTE, minuto)
                calendarioFim.set(Calendar.SECOND, 0)
                inputHoraFim.setText(String.format("%02d:%02d", hora, minuto))
            }, calendarioFim.get(Calendar.HOUR_OF_DAY), calendarioFim.get(Calendar.MINUTE), true).show()
        }

        btnConfirmar.setOnClickListener {
            validarEProcessar()
        }
    }

    private fun validarEProcessar() {
        if (inputData.text.isNullOrEmpty() || inputHoraInicio.text.isNullOrEmpty() || inputHoraFim.text.isNullOrEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        if (calendarioFim.before(calendarioInicio) || calendarioFim.equals(calendarioInicio)) {
            Toast.makeText(this, "A hora final deve ser depois da inicial.", Toast.LENGTH_LONG).show()
            return
        }

        btnConfirmar.isEnabled = false
        btnConfirmar.text = "Verificando..."
        verificarConflitoNoFirebase()
    }

    private fun verificarConflitoNoFirebase() {
        db.collection("reservas")
            .whereEqualTo("spaceName", espacoNome)
            .get()
            .addOnSuccessListener { documents ->
                var temConflito = false
                val novoInicio = calendarioInicio.timeInMillis
                val novoFim = calendarioFim.timeInMillis

                for (doc in documents) {
                    val inicioExistente = doc.getTimestamp("startTime")?.toDate()?.time ?: 0
                    val fimExistente = doc.getTimestamp("endTime")?.toDate()?.time ?: 0

                    if (novoInicio < fimExistente && novoFim > inicioExistente) {
                        temConflito = true
                        break
                    }
                }

                if (temConflito) {
                    Toast.makeText(this, "Conflito! Horário já reservado.", Toast.LENGTH_LONG).show()
                    btnConfirmar.isEnabled = true
                    btnConfirmar.text = "Confirmar Agendamento"
                } else {
                    salvarReserva()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro de conexão.", Toast.LENGTH_SHORT).show()
                btnConfirmar.isEnabled = true
            }
    }

    private fun salvarReserva() {
        val userId = auth.currentUser?.uid ?: "anonimo"

        val matricula = "2025001"

        val novaReserva = hashMapOf(
            "spaceName" to espacoNome,
            "userId" to userId,
            "matricula" to matricula, // Salva a matrícula!
            "startTime" to Timestamp(calendarioInicio.time),
            "endTime" to Timestamp(calendarioFim.time),
            "status" to "Confirmado"
        )

        db.collection("reservas")
            .add(novaReserva)
            .addOnSuccessListener {
                startActivity(Intent(this, UserSuccesfulBooking::class.java))
                finish()
            }
            .addOnFailureListener {
                startActivity(Intent(this, UserFailedBooking::class.java))
                finish()
            }
    }
}