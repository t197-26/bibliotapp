package app.cincodev.bibliotapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EspacosAdapter(
    private val listaEspacos: List<EspacoModel>,
    private val onEspacoClick: (EspacoModel) -> Unit
) : RecyclerView.Adapter<EspacosAdapter.EspacoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_reserva_espaco, parent, false)
        return EspacoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EspacoViewHolder, position: Int) {
        holder.bind(listaEspacos[position])
    }

    override fun getItemCount(): Int = listaEspacos.size

    inner class EspacoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtSlot: TextView = itemView.findViewById(R.id.txtSlotEspaco)

        fun bind(espaco: EspacoModel) {
            txtSlot.text = espaco.nome

            val statusLimpo = espaco.status.trim().lowercase()

            if (statusLimpo == "disponivel" || statusLimpo == "disponível") {

                txtSlot.setBackgroundColor(Color.parseColor("#4285F4"))
            } else {

                txtSlot.setBackgroundColor(Color.parseColor("#8C8C8C"))
            }


            itemView.setOnClickListener {
                onEspacoClick(espaco)
            }
        }
    }
}