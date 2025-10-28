package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AccountRegister : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var registerButton: Button
    lateinit var missingFieldsTextView: TextView
    lateinit var MatriculaEditText: EditText
    lateinit var FullNameEditText: EditText
    lateinit var EmailEditText: EditText
    lateinit var PasswordEditText: EditText
    lateinit var PasswordConfirmationEditText: EditText
    lateinit var passwordNotMatchingTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_register)

        arrowBackButtonView = findViewById(R.id.AccountRegisterArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        MatriculaEditText = findViewById(R.id.AccountRegisterMatriculaEditText)
        FullNameEditText = findViewById(R.id.AccountRegisterFullNameEditText)
        EmailEditText = findViewById(R.id.AccountRegisterEmailEditText)
        PasswordEditText = findViewById(R.id.AccountRegisterPasswordEditText)
        PasswordConfirmationEditText = findViewById(R.id.AccountRegisterPasswordConfirmationEditText)
        missingFieldsTextView = findViewById(R.id.AccountRegisterMissingFieldsTextView)
        passwordNotMatchingTextView = findViewById(R.id.AccountRegisterPasswordNotMatchingTextView)
        registerButton = findViewById(R.id.AccountRegisterRegisterButton)

        registerButton.setOnClickListener {

            missingFieldsTextView.visibility = View.INVISIBLE

            passwordNotMatchingTextView.visibility = View.INVISIBLE

            if (MatriculaEditText.text.length == 0
                || FullNameEditText.text.length == 0
                || EmailEditText.text.length == 0
                || PasswordEditText.text.length == 0
                || PasswordConfirmationEditText.text.length == 0) {

                missingFieldsTextView.visibility = View.VISIBLE

            } else if (PasswordEditText.text.toString() != PasswordConfirmationEditText.text.toString()) {

                passwordNotMatchingTextView.visibility = View.VISIBLE

            } else {
                val dialogView = layoutInflater.inflate(R.layout.dialog_confirmation_user_register, null)

                val dialog = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create()

                val btnHome = dialogView.findViewById<Button>(R.id.btnHome)

                btnHome.setOnClickListener {
                    dialog.dismiss()
                    startActivity(Intent(this, Welcome::class.java))
                }

                dialog.show()
            }
        }
    }
}