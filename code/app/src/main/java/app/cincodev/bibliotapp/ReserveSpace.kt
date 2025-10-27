package app.cincodev.bibliotapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReserveSpace : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_space)

        val dataset = arrayOf(
            ReservaItem("Cubículo C10", "Entrada: 20/09/2025 09:00", "Saída: 20/09/2025 11:00"),
            ReservaItem("Cubículo C11", "Entrada: 20/09/2025 11:00", "Saída: 20/09/2025 13:00"),
            ReservaItem("Cubículo C12", "Entrada: 20/09/2025 15:00", "Saída: 20/09/2025 16:00"),
            ReservaItem("Cubículo C10", "Entrada: 20/09/2025 09:00", "Saída: 20/09/2025 11:00"),
            ReservaItem("Cubículo C11", "Entrada: 20/09/2025 11:00", "Saída: 20/09/2025 13:00"),
            ReservaItem("Cubículo C12", "Entrada: 20/09/2025 15:00", "Saída: 20/09/2025 13:00"),
            ReservaItem("Cubículo C12", "Entrada: 20/09/2025 15:00", "Saída: 20/09/2025 13:00"),
            ReservaItem("Cubículo C12", "Entrada: 20/09/2025 15:00", "Saída: 20/09/2025 13:00"),
            ReservaItem("Cubículo C12", "Entrada: 20/09/2025 15:00", "Saída: 20/09/2025 13:00")
        )

        val customAdapter = ListaReservasEspacoAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.reservaCartoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter


    }
}