package app.cincodev.bibliotapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReserveSpace : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val listaReservas = mutableListOf<ReservaItem>()
    private lateinit var customAdapter: ListaReservasEspacoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var txtTitulo: TextView
    private lateinit var btnLimpar: ImageButton


    private var dataSelecionada = Calendar.getInstance()

    private val sdfDisplay = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    private val sdfDataTitulo = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_space)

        txtTitulo = findViewById(R.id.txtTituloReservas)
        btnLimpar = findViewById(R.id.btnLimparFiltro)
        recyclerView = findViewById(R.id.reservaCartoes)

        recyclerView.layoutManager = LinearLayoutManager(this)
        buscarTodasReservas()
    }

    override fun onStart() {
        super.onStart()

        // Botão Nova Reserva
        findViewById<Button>(R.id.goToSelection).setOnClickListener {
            startActivity(Intent(this, SelectSpace::class.java))
        }

        // Botão Voltar
        findViewById<ImageButton>(R.id.LoginArrowBack).setOnClickListener { finish() }

        // Botão Calendário (Aplica Filtro)
        findViewById<ImageButton>(R.id.botaoCalendario).setOnClickListener {
            val ano = dataSelecionada.get(Calendar.YEAR)
            val mes = dataSelecionada.get(Calendar.MONTH)
            val dia = dataSelecionada.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                dataSelecionada.set(year, month, dayOfMonth)


                buscarReservasPorData()

                // Atualiza visual
                txtTitulo.text = "Dia: ${sdfDataTitulo.format(dataSelecionada.time)}"
                btnLimpar.visibility = View.VISIBLE // Mostra o botão X

            }, ano, mes, dia).show()
        }

        // linha botão de limpar filtro
        btnLimpar.setOnClickListener {
            buscarTodasReservas()
            txtTitulo.text = "Todas as Reservas"
            btnLimpar.visibility = View.GONE // Esconde o botão X
        }
    }

    private fun buscarTodasReservas() {
        db.collection("reservas")
            .orderBy("startTime", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                processarResultados(documents)
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhuma reserva encontrada.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e -> tratarErro(e) }
    }

    private fun buscarReservasPorData() {
        val inicioDia = dataSelecionada.clone() as Calendar // 00:00:00
        inicioDia.set(Calendar.HOUR_OF_DAY, 0)
        inicioDia.set(Calendar.MINUTE, 0)
        inicioDia.set(Calendar.SECOND, 0)

        val fimDia = dataSelecionada.clone() as Calendar // 23:59:59
        fimDia.set(Calendar.HOUR_OF_DAY, 23)
        fimDia.set(Calendar.MINUTE, 59)
        fimDia.set(Calendar.SECOND, 59)

        db.collection("reservas")
            .whereGreaterThanOrEqualTo("startTime", inicioDia.time)
            .whereLessThanOrEqualTo("startTime", fimDia.time)
            .orderBy("startTime", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                processarResultados(documents)
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nada agendado para este dia.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e -> tratarErro(e) }
    }

    private fun processarResultados(documents: com.google.firebase.firestore.QuerySnapshot) {
        listaReservas.clear()
        for (doc in documents) {
            val model = doc.toObject(ReservaModel::class.java)
            val entrada = model.startTime?.toDate()?.let { sdfDisplay.format(it) } ?: "--"
            val saida = model.endTime?.toDate()?.let { sdfDisplay.format(it) } ?: "--"

            listaReservas.add(
                ReservaItem(doc.id, model.spaceName, "Entrada: $entrada", "Saída: $saida")
            )
        }
        atualizarAdapter()
    }

    private fun atualizarAdapter() {
        customAdapter = ListaReservasEspacoAdapter(this, listaReservas) { idReserva ->
            confirmarCancelamento(idReserva)
        }
        recyclerView.adapter = customAdapter
    }

    private fun tratarErro(e: Exception) {
        Log.e("ReserveSpace", "Erro Firebase", e)
        Toast.makeText(this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show()
    }

    private fun confirmarCancelamento(idReserva: String) {
        AlertDialog.Builder(this)
            .setTitle("Cancelar Reserva?")
            .setMessage("Deseja liberar este espaço?")
            .setPositiveButton("Sim") { _, _ ->
                deletarReserva(idReserva)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarReserva(id: String) {
        db.collection("reservas").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Reserva cancelada!", Toast.LENGTH_SHORT).show()
                // Recarrega a lista atual
                if (btnLimpar.visibility == View.VISIBLE) {
                    buscarReservasPorData()
                } else {
                    buscarTodasReservas()
                }
            }
    }
}