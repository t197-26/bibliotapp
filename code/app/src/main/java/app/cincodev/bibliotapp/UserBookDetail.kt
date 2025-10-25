package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserBookDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_detail)

        val bookExemplarMenu: ImageView = findViewById(R.id.bookExemplarMenu)

        bookExemplarMenu.setOnClickListener {
            showPopupMenu(bookExemplarMenu)
        }
    }

    private fun showPopupMenu(anchorView: ImageView) {
        // Inflate custom popup layout
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.custom_menu_exemplar, null)

        // Create PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true // dismiss on outside touch
        )

        popupWindow.elevation = 16f // adds shadow

        // Setup button listeners inside popup
        popupView.findViewById<Button>(R.id.btnReservar).setOnClickListener {
            Toast.makeText(this, "Reservar", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupView.findViewById<Button>(R.id.btnDigitalizar).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this, UserDigitalizationOrder::class.java))
        }

        popupView.findViewById<Button>(R.id.btnVerNoMapa).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this, UserSpaceMap::class.java))
        }

        // Show the popup anchored to the menu button
        // You can adjust offsets (x, y) as needed
        popupWindow.showAsDropDown(anchorView, -150, -350)
    }
}
