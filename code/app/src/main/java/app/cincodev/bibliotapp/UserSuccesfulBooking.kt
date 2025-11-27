package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserSuccesfulBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_succesful_booking)

        lifecycleScope.launch {
            delay(2000) // Espera 2 segundos exibindo a mensagem de sucesso

            // --- CORREÇÃO DE FLUXO ---
            // Leva para a lista "Minhas Reservas" para confirmar que deu certo
            // Adicionei flags para limpar o histórico e não deixar voltar para o loading
            val intent = Intent(this@UserSuccesfulBooking, ReserveSpace::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}