package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BookRequestAdapter(
    private val dataSet: Array<BookRequest>,
) : RecyclerView.Adapter<BookRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val ivRequestBookCover: ImageView = view.findViewById(R.id.ivRequestBookCover)
        private val tvRequestBookTitle: TextView = view.findViewById(R.id.tvRequestBookTitle)
        private val tvRequestDate: TextView = view.findViewById(R.id.tvRequestDate)
        private val tvRequestPages: TextView = view.findViewById(R.id.tvRequestPages)
        private val tvRequestStatus: TextView = view.findViewById(R.id.tvRequestStatus)
        private val btnCloseRequest: ImageButton = view.findViewById(R.id.btnCloseRequest)

        fun bind(request: BookRequest) {
            tvRequestBookTitle.text = request.bookTitle
            tvRequestDate.text = "Pedido em: ${request.requestDate}"
            tvRequestPages.text = "Páginas: ${request.pages}"

            when (request.status) {
                RequestStatus.IN_QUEUE -> {
                    tvRequestStatus.text = "Em fila"
                    tvRequestStatus.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.status_in_queue)
                    )
                    tvRequestStatus.setBackgroundResource(R.drawable.bg_status_in_queue)
                }
                RequestStatus.CANCELLED -> {
                    tvRequestStatus.text = "Cancelado"
                    tvRequestStatus.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.status_cancelled)
                    )
                    tvRequestStatus.setBackgroundResource(R.drawable.bg_status_cancelled)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.unit_book_card, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}
