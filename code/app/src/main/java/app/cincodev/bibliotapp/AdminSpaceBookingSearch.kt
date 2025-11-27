package app.cincodev.bibliotapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class AdminSpaceBookingSearch : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton
    private lateinit var reservaAdapter: ReservaAdapter

    private val listaCompleta = mutableListOf<ReservaModel>()
    private val listaFiltrada = mutableListOf<ReservaModel>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_space_booking_search)

        arrowBackButtonView = findViewById(R.id.SpaceBookingSearchArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        reservaAdapter = ReservaAdapter(listaFiltrada) { reserva ->
            confirmarExclusao(reserva)
        }
        recyclerView.adapter = reservaAdapter

        carregarReservasDoFirebase()
        configurarPesquisa()
    }

    private fun carregarReservasDoFirebase() {
        db.collection("reservas")
            .orderBy("startTime")
            .get()
            .addOnSuccessListener { documents ->
                listaCompleta.clear()
                listaFiltrada.clear()

                for (document in documents) {
                    val reserva = document.toObject(ReservaModel::class.java)
                    reserva.id = document.id
                    listaCompleta.add(reserva)
                }

                listaFiltrada.addAll(listaCompleta)
                reservaAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao carregar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun configurarPesquisa() {
        val etPesquisa = findViewById<EditText>(R.id.etPesquisa)
        etPesquisa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().lowercase()
                filtrarLista(texto)
            }
        })
    }

    private fun filtrarLista(texto: String) {
        listaFiltrada.clear()
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaCompleta)
        } else {
            for (item in listaCompleta) {
                // Pesquisa por: Nome da Sala OU Matrícula OU UserID
                if (item.spaceName.lowercase().contains(texto) ||
                    item.matricula.lowercase().contains(texto) ||
                    item.userId.lowercase().contains(texto)) {
                    listaFiltrada.add(item)
                }
            }
        }
        reservaAdapter.notifyDataSetChanged()
    }

    private fun confirmarExclusao(reserva: ReservaModel) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Reserva")
            .setMessage("Tem certeza que deseja cancelar a reserva de ${reserva.spaceName}?")
            .setPositiveButton("Sim") { _, _ -> deletarReserva(reserva) }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarReserva(reserva: ReservaModel) {
        db.collection("reservas").document(reserva.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Reserva excluída!", Toast.LENGTH_SHORT).show()
                listaCompleta.remove(reserva)
                listaFiltrada.remove(reserva)
                reservaAdapter.notifyDataSetChanged()
            }
    }
}
class ReservaAdapter(
    private val reservas: List<ReservaModel>,
    private val onDeleteClick: (ReservaModel) -> Unit
) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    private val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))

    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRequisitante: TextView = itemView.findViewById(R.id.tvRequisitante)
        val tvLocal: TextView = itemView.findViewById(R.id.tvLocal)
        val tvCheckIn: TextView = itemView.findViewById(R.id.tvCheckIn)
        val tvCheckOut: TextView = itemView.findViewById(R.id.tvCheckOut)
        val btnExcluir: ImageView = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]

        val dataInicio = if (reserva.startTime != null) sdf.format(reserva.startTime!!.toDate()) else "--"
        val dataFim = if (reserva.endTime != null) sdf.format(reserva.endTime!!.toDate()) else "--"


        val textoIdentificacao = if (reserva.matricula.isNotEmpty()) {
            "Matrícula: ${reserva.matricula}"
        } else {
            "ID: ${reserva.userId}"
        }

        holder.tvRequisitante.text = textoIdentificacao
        holder.tvLocal.text = "Local: ${reserva.spaceName}"
        holder.tvCheckIn.text = "Check-in: $dataInicio"
        holder.tvCheckOut.text = "Check-out: $dataFim"

        holder.btnExcluir.setOnClickListener { onDeleteClick(reserva) }
    }

    override fun getItemCount(): Int = reservas.size
}