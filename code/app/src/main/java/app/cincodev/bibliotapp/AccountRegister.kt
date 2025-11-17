package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Exemplo de uso
//        fb1.collection(
//            "users"
//        ).document("mNyLNe3BduZqYRDjjzhw").delete()

//        fb1.collection(
//            "users"
//        ).document("mNyLNe3BduZqYRDjjzhw").update(mapOf("name" to "Pedro"))

//        fb1.collection("users").document().get().addOnSuccessListener{
//            result ->
//            etNome.setText(result.get("name").toString())
//        }


class AccountRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_register)

        val matriculaEditText               = findViewById<EditText>(R.id.AccountRegisterMatriculaEditText)
        val fullNameEditText                = findViewById<EditText>(R.id.AccountRegisterFullNameEditText)
        val emailEditText                   = findViewById<EditText>(R.id.AccountRegisterEmailEditText)
        val passwordEditText                = findViewById<EditText>(R.id.AccountRegisterPasswordEditText)
        val passwordConfirmationEditText    = findViewById<EditText>(R.id.AccountRegisterPasswordConfirmationEditText)
        val missingFieldsTextView           = findViewById<TextView>(R.id.AccountRegisterMissingFieldsTextView)
        val passwordNotMatchingTextView     = findViewById<TextView>(R.id.AccountRegisterPasswordNotMatchingTextView)
        val arrowBackButtonView             = findViewById<ImageButton>(R.id.AccountRegisterArrowBack)
        val registerButton                  = findViewById<Button>(R.id.AccountRegisterRegisterButton)

        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val db = Firebase.firestore
        registerButton.setOnClickListener {
            missingFieldsTextView.visibility = View.INVISIBLE
            passwordNotMatchingTextView.visibility = View.INVISIBLE

            val missingField = (
                    matriculaEditText.text.toString().isEmpty()
                            || fullNameEditText.text.toString().isEmpty()
                            || emailEditText.text.toString().isEmpty()
                            || passwordEditText.text.toString().isEmpty()
                            || passwordConfirmationEditText.text.toString().isEmpty()
                    )

            val mismatchedPassword = (
                    passwordEditText.text.toString()
                            != passwordConfirmationEditText.text.toString()
                    )

            if (missingField) { missingFieldsTextView.visibility = View.VISIBLE }
            else if (mismatchedPassword) { passwordNotMatchingTextView.visibility = View.VISIBLE }
            else {
                val docRef = db.collection("users")
                    .document(matriculaEditText.text.toString())

                lifecycleScope.launch {
                    val document = docRef.get().await()
                    if (document.exists()) {
                        inflateCancelPopup()
                    } else {
                        userWrite(
                            docRef = docRef,
                            name = fullNameEditText.text.toString(),
                            email = emailEditText.text.toString(),
                            password = passwordEditText.text.toString()
                        )

                        inflateConfirmationPopup()
                    }
                }
            }
        }
    }

    fun inflateCancelPopup() {
        val dialogView =
            layoutInflater.inflate(R.layout.dialog_cancel_user_register, null)

        val dialog = AlertDialog.Builder(this@AccountRegister)
            .setView(dialogView)
            .create()

        val btnHome = dialogView.findViewById<Button>(R.id.btnHome)

        btnHome.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }

    fun inflateConfirmationPopup() {
        val dialogView =
            layoutInflater.inflate(R.layout.dialog_confirmation_user_register, null)

        val dialog = AlertDialog.Builder(this@AccountRegister)
            .setView(dialogView)
            .create()

        val btnHome = dialogView.findViewById<Button>(R.id.btnHome)

        btnHome.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }

     suspend fun userWrite(
        docRef : DocumentReference,
        name : String,
        email : String,
        password : String
    ) {
         val data = mapOf(
             "name" to name,
             "email" to email,
             "password" to password,
             "created_at" to System.currentTimeMillis()
         )

         docRef.set(data).await()
    }
}