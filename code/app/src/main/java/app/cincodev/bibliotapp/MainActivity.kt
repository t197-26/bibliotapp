package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var senha: EditText
    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("matricula", "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email = findViewById(R.id.editTextText)
        senha = findViewById(R.id.editTextTextPassword)
        btn = findViewById(R.id.button)
    }

    override fun onStart() {
        Log.d("matricula","onStart()")
        super.onStart()
        btn.setOnClickListener {
            validacao()
            var intencao = Intent(this, MainActivityB::class.java)
            startActivity(intencao)
        }
    }

    override fun onResume() {
        Log.d("matricula","onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d("matricula","onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.d("matricula","onStop()")
        super.onStop()
    }

    override fun onRestart() {
        Log.d("matricula","onRestart()")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d("matricula","onDestroy()")
        super.onDestroy()
    }

    private fun validacao() {
        if(email.text.toString() == "narak"&& senha.text.toString() == "123"){
            Toast.makeText(this,"autorizado",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"não autorizado",Toast.LENGTH_SHORT).show()

        }
    }
}
