package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val unmatechedPasswordTextView = findViewById<TextView>(R.id.LoginUnmatchedPasswordTextView)
        val matriculaEditText = findViewById<EditText>(R.id.LoginMatriculaEditText)
        val accessButton = findViewById<Button>(R.id.LoginAccessButton)
        val passwordEditTextTextPassword = findViewById<EditText>(R.id.LoginPasswordEditTextTextPassword)
        val arrowBackButtonView = findViewById<ImageButton>(R.id.LoginArrowBack)
        arrowBackButtonView.setOnClickListener {
            startActivity(Intent(this, Welcome::class.java))
            // onBackPressedDispatcher.onBackPressed()
        }

        val loginForgetPasswordButton = findViewById<Button>(R.id.LoginForgetPasswordButton)
        loginForgetPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }

        matriculaEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                return
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (unmatechedPasswordTextView.isVisible) {
                    unmatechedPasswordTextView.visibility = View.INVISIBLE
                }

                if (!s.isNullOrEmpty() && s.length >= 7 && passwordEditTextTextPassword.length() >= 8) {
                    accessButton.backgroundTintList = ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.unifor_marinho
                        )
                    accessButton.setTextColor(ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.white
                        ))
                    accessButton.isEnabled = true
                } else {
                    accessButton.isEnabled = false
                    accessButton.backgroundTintList = ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.light_gray
                        )
                    accessButton.setTextColor(ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.gray
                        ))
                }
            }
        })

        passwordEditTextTextPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                return
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int ) {
                if (unmatechedPasswordTextView.isVisible) {
                    unmatechedPasswordTextView.visibility = View.INVISIBLE
                }

                if (!s.isNullOrEmpty() && s.length >= 8 && matriculaEditText.length() >= 7) {
                    accessButton.backgroundTintList = ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.unifor_marinho
                        )
                    accessButton.setTextColor(ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.white
                        ))
                    accessButton.isEnabled = true
                } else {
                    accessButton.isEnabled = false
                    accessButton.backgroundTintList = ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.light_gray
                        )
                    accessButton.setTextColor(ContextCompat
                        .getColorStateList(
                            this@LoginActivity,
                            R.color.gray
                        ))
                }
            }
        })


        accessButton.setOnClickListener {
            val db = Firebase.firestore
            val docRef = db.collection("users")
                .document(matriculaEditText.text.toString())
            val adminIntent = Intent(this, AdminHome::class.java)
            val userIntent = Intent(this, UserHome::class.java)

            lifecycleScope.launch {
                val document = docRef.get().await()
                val isCorrectPassword = checkPassword(document, passwordEditTextTextPassword.text.toString())

                if (document.exists() && isCorrectPassword) {
                    getSharedPreferences("bibliotapp_shared_preferences", MODE_PRIVATE)
                        .edit {
                            // dados de sessao podem ser salvos aqui
                            putString("matricula", matriculaEditText.text.toString())
                        }

                    if (document.get("isAdmin") == true) {
                        startActivity(adminIntent)
                        return@launch
                    } else {
                        startActivity(userIntent)
                        return@launch
                    }
                }

                unmatechedPasswordTextView.visibility = View.VISIBLE

            }
        }
    }

    fun checkPassword(document: DocumentSnapshot, enteredPassword: String): Boolean {
        if (document.get("password") == enteredPassword) {
            return true;
        }

        return false;
    }
}