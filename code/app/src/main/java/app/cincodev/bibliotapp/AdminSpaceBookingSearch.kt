package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

data class Reserva(
    val requisitante: String,
    val local: String,
    val checkIn: String,
    val checkOut: String
)

class AdminSpaceBookingSearch : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton
    private lateinit var reservaAdapter: ReservaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_space_booking_search)

        arrowBackButtonView = findViewById(R.id.SpaceBookingSearchArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val reservas = listOf(
            Reserva("2420382", "C10", "22/09/2025 08:00", "22/09/2025 10:00"),
            Reserva("2320466", "C11", "23/09/2025 10:00", "23/09/2025 12:00"),
            Reserva("2320478", "C12", "24/09/2025 09:00", "24/09/2025 11:00"),
            Reserva("2298120", "C13", "25/09/2025 07:00", "25/09/2025 09:00")
        )

        // Configuração do RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        reservaAdapter = ReservaAdapter(reservas) { reserva ->
            Toast.makeText(
                this,
                "Reserva de ${reserva.local} excluída!",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerView.adapter = reservaAdapter
    }
}
    class ReservaAdapter(
        private val reservas: List<Reserva>,
        private val onDeleteClick: (Reserva) -> Unit
    ) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {


    class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRequisitante: TextView = itemView.findViewById(R.id.tvRequisitante)
        val tvLocal: TextView = itemView.findViewById(R.id.tvLocal)
        val tvCheckIn: TextView = itemView.findViewById(R.id.tvCheckIn)
        val tvCheckOut: TextView = itemView.findViewById(R.id.tvCheckOut)
        val btnExcluir: ImageView = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_booking, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.tvRequisitante.text = "Requisitante: ${reserva.requisitante}"
        holder.tvLocal.text = "Local: ${reserva.local}"
        holder.tvCheckIn.text = "Check-in: ${reserva.checkIn}"
        holder.tvCheckOut.text = "Check-out: ${reserva.checkOut}"


        holder.btnExcluir.setOnClickListener {
            onDeleteClick(reserva)
        }
    }

    override fun getItemCount(): Int = reservas.size
}
