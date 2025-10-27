package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        val dataset = arrayOf(
            QuickBook(R.drawable.book_01, "Gerenciamento de serviços de TI na prática", "2 dias restantes"),
            QuickBook(R.drawable.book_02, "Governança de TI", "15 dias restantes"),
            QuickBook(R.drawable.book_03, "Sistemas de Informação", "2 dias atrasado")
        )

        val dataset_2 = arrayOf(
            QuickPlace(R.drawable.ic_quickplace, "Sala q12", "24/09/2025 11:00\n24/09/2025 11:00"),
            QuickPlace(R.drawable.book_01, "Gerencimento de TI", "Disponível"),
            QuickPlace(R.drawable.book_03, "Sistemas de Informação", "3 dias")
        )

        val quickBookAdapter = QuickBookAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.quickBookRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = quickBookAdapter

        val quickPlaceAdapter = QuickPlaceAdapter(dataset_2)

        val recyclerView2: RecyclerView = findViewById(R.id.quickPlaceRecycler)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = quickPlaceAdapter

        val btnDigitalizacoes: ImageView = findViewById(R.id.btnDIgitalizacoes)
        val btnReservarEspaco: ImageView = findViewById(R.id.btnReservarEspaco)
        val fabChatbot: FloatingActionButton = findViewById(R.id.fab)

        btnDigitalizacoes.setOnClickListener { openDigitalizacoes() }
        btnReservarEspaco.setOnClickListener { openReservarEspaco() }
        fabChatbot.setOnClickListener { openChatbot() }
    }

    private fun openDigitalizacoes() {
        val intent = Intent(this, UserDigitalizations::class.java)
        startActivity(intent)
    }

    private fun openReservarEspaco() {
        val intent = Intent(this, UserSpaceBooking::class.java)
        startActivity(intent)
    }

    private fun openChatbot() {
        val intent = Intent(this, UserChatbot::class.java)
        startActivity(intent)
    }

}