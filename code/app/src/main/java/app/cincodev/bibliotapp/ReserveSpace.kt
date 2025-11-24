package app.cincodev.bibliotapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import java.util.Calendar
import java.util.Locale


class ReserveSpace : AppCompatActivity() {
    lateinit var botaoCalendario: ImageButton
    lateinit var rangeSlider: RangeSlider
    lateinit var tvSelectedRange: TextView
    lateinit var arrowBackButtonView: ImageButton
    lateinit var tvDate: TextView

    lateinit var goToSelectionButton: Button
    private var selectedDate: String? = null
    private var startHour: Int = 8
    private var endHour: Int = 9
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve_space)

        val dataset = arrayOf<ReservaItem>()

        val customAdapter = ListaReservasEspacoAdapter(this, dataset)

        val recyclerView: RecyclerView = findViewById(R.id.reservaCartoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }

    override fun onStart() {
        botaoCalendario = findViewById(R.id.botaoCalendario)
        rangeSlider = findViewById(R.id.reserveTimeSlider)
        tvSelectedRange = findViewById(R.id.tvSelectedRange)
        tvDate = findViewById(R.id.reserveDate)
        goToSelectionButton = findViewById(R.id.goToSelection)

        tvDate.text = String.format(Locale.US, "%02d/%02d/%04d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))

        rangeSlider.values = listOf(startHour.toFloat(), endHour.toFloat())
        onRangeSliderValuesChange(rangeSlider)

        rangeSlider.addOnChangeListener { slider, _, _ ->
            onRangeSliderValuesChange(slider)
        }

        botaoCalendario.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    tvDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        goToSelectionButton.setOnClickListener {
            val intent = Intent(this, SelectSpace::class.java)

            intent.putExtra("selected_date", tvDate.text)
            intent.putExtra("start_hour", startHour.toString())
            intent.putExtra("end_hour", endHour.toString())

            startActivity(intent)
        }

        super.onStart()
    }

    private fun onRangeSliderValuesChange(slider: RangeSlider) {
        val step = slider.stepSize
        val min = slider.valueFrom
        val max = slider.valueTo

        startHour = slider.values[0].toInt()
        endHour = slider.values[1].toInt()

        if (startHour >= endHour) {
            val tryEnd = (startHour + step).coerceAtMost(max)

            if (tryEnd > endHour) {
                endHour = tryEnd.toInt()
            } else {
                startHour = (endHour - step).coerceAtLeast(min).toInt()
            }

            slider.values = listOf(startHour.toFloat(), endHour.toFloat())
            return
        }

        tvSelectedRange.text = getString(R.string.horario_reserva, startHour, endHour)
    }
}