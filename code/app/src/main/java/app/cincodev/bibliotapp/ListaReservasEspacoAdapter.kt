package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListaReservasEspacoAdapter(private val context: Context, private val dataSet: Array<ReservaItem>) :
    RecyclerView.Adapter<ListaReservasEspacoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val requisitante: TextView = view.findViewById(R.id.dataRequisitante)

        val sala_componente: TextView = view.findViewById(R.id.salaReserva)
        val entrada_componente: TextView = view.findViewById(R.id.entradaReserva)
        val saida_componente: TextView = view.findViewById(R.id.saidaReserva)
        val botao_cancelar: ImageButton = view.findViewById(R.id.buttonCancelar)
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

        viewHolder.botao_cancelar.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Cancelar reserva")
            builder.setMessage("Tem certeza que deseja cancelar sua reserva?")

            builder.setPositiveButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun getItemCount() = dataSet.size

}