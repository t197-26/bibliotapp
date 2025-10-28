package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListaReservasEspacoAdapter(private val dataSet: Array<ReservaItem>) :
    RecyclerView.Adapter<ListaReservasEspacoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val requisitante: TextView = view.findViewById(R.id.dataRequisitante)

        val sala_componente: TextView = view.findViewById(R.id.salaReserva)
        val entrada_componente: TextView = view.findViewById(R.id.entradaReserva)
        val saida_componente: TextView = view.findViewById(R.id.saidaReserva)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_card_reserva_espaco, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.sala_componente.text = dataSet[position].sala
        viewHolder.entrada_componente.text = dataSet[position].entrada
        viewHolder.saida_componente.text = dataSet[position].saida
    }

    override fun getItemCount() = dataSet.size

}