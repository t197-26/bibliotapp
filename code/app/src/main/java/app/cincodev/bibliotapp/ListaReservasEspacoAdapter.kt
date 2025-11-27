package app.cincodev.bibliotapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListaReservasEspacoAdapter(
    private val context: Context,
    private val dataset: List<ReservaItem>, // Mudado para List para ser mais flexível que Array
    private val onDeleteClick: (String) -> Unit // Função que a Activity vai passar para deletar
) : RecyclerView.Adapter<ListaReservasEspacoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTitle)
        val tvEntrada: TextView = view.findViewById(R.id.tvSubtitle1)
        val tvSaida: TextView = view.findViewById(R.id.tvSubtitle2)
        // Certifique-se de adicionar este ID no seu item_loan.xml
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]

        holder.tvTitulo.text = item.titulo
        holder.tvEntrada.text = item.dataEntrada
        holder.tvSaida.text = item.dataSaida

        // Configura o clique na lixeira
        holder.btnDelete.setOnClickListener {
            // Chama a função da Activity passando o ID da reserva
            onDeleteClick(item.id)
        }
    }

    override fun getItemCount(): Int = dataset.size
}