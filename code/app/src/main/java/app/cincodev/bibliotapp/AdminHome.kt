package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout



class AdminHome : AppCompatActivity() {
    lateinit var arrowBackButtonView : ImageButton
    lateinit var DigitazacoesConstraintLayout : ConstraintLayout
    lateinit var CadastrarMaterialConstraintLayout : ConstraintLayout
    lateinit var EditarMaterialConstraintLayout : ConstraintLayout
    lateinit var EmprestimosConstraintLayout : ConstraintLayout
    lateinit var ReservasEspacoConstraintLayout : ConstraintLayout
    lateinit var BuscaConstraintLayout : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        arrowBackButtonView = findViewById(R.id.AdminHomeArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        DigitazacoesConstraintLayout = findViewById(R.id.AdminHomeBodyDigitazacoesConstraintLayout)
        DigitazacoesConstraintLayout.setOnClickListener {
            startActivity(Intent(this, AdminDigitalizationOrders::class.java))
        }

        CadastrarMaterialConstraintLayout = findViewById(R.id.AdminHomeBodyCadastrarMaterialConstraintLayout)
        CadastrarMaterialConstraintLayout.setOnClickListener {
            startActivity(Intent(this, AdminBookRegister::class.java))
        }

        EditarMaterialConstraintLayout = findViewById(R.id.AdminHomeBodyBuscaConstraintLayout)
        EditarMaterialConstraintLayout.setOnClickListener {
            startActivity(Intent(this, AdminBookSearch::class.java))
        }

        EmprestimosConstraintLayout = findViewById(R.id.AdminHomeBodyEmprestimosConstraintLayout)
        EmprestimosConstraintLayout.setOnClickListener {
            startActivity(Intent(this, AdminLoanSearch::class.java))
        }

        ReservasEspacoConstraintLayout = findViewById(R.id.AdminHomeBodyReservasEspacoConstraintLayout)
        ReservasEspacoConstraintLayout.setOnClickListener {
            startActivity(Intent(this, AdminSpaceBookingSearch::class.java))
        }

    }
}