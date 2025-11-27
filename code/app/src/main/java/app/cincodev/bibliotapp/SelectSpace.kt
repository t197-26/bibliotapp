package app.cincodev.bibliotapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SelectSpace : AppCompatActivity() {

    private lateinit var adapter: EspacosAdapter
    private val listaEspacos = mutableListOf<EspacoModel>()
    private val salasOcupadasNoHorario = mutableSetOf<String>()
    private val db = FirebaseFirestore.getInstance()

    // Filtros Selecionados
    private val dataFiltro = Calendar.getInstance()
    private val horaInicio = Calendar.getInstance() // Guarda a hora escolhida (ex: 08:00)
    private val horaFim = Calendar.getInstance()    // Guarda a hora escolhida (ex: 09:00)

    // Formatadores
    private val sdfData = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val sdfHora = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_space)

        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Inicializa Horários Padrão (Próxima hora cheia)
        horaInicio.add(Calendar.HOUR_OF_DAY, 1)
        horaInicio.set(Calendar.MINUTE, 0)
        horaFim.time = horaInicio.time
        horaFim.add(Calendar.HOUR_OF_DAY, 1) // Duração de 1h padrão

        val rv = findViewById<RecyclerView>(R.id.rvEspacos)
        rv?.layoutManager = GridLayoutManager(this, 5)

        configurarCliquesFiltro()
        atualizarUIFiltros()

        // Busca inicial
        buscarDisponibilidade()
    }

    private fun configurarCliquesFiltro() {
        // 1. Escolher DATA
        findViewById<ImageButton>(R.id.btnCalendarioFiltro).setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                dataFiltro.set(y, m, d)
                atualizarUIFiltros()
                buscarDisponibilidade()
            }, dataFiltro.get(Calendar.YEAR), dataFiltro.get(Calendar.MONTH), dataFiltro.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 2. Escolher HORA INÍCIO
        findViewById<TextView>(R.id.btnHoraInicio).setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                horaInicio.set(Calendar.HOUR_OF_DAY, h)
                horaInicio.set(Calendar.MINUTE, m)

                // Ajusta o fim automaticamente se ficar menor que o início
                if (horaFim.before(horaInicio)) {
                    horaFim.time = horaInicio.time
                    horaFim.add(Calendar.HOUR_OF_DAY, 1)
                }

                atualizarUIFiltros()
                buscarDisponibilidade()
            }, horaInicio.get(Calendar.HOUR_OF_DAY), horaInicio.get(Calendar.MINUTE), true).show()
        }

        // 3. Escolher HORA FIM
        findViewById<TextView>(R.id.btnHoraFim).setOnClickListener {
            TimePickerDialog(this, { _, h, m ->
                horaFim.set(Calendar.HOUR_OF_DAY, h)
                horaFim.set(Calendar.MINUTE, m)
                atualizarUIFiltros()
                buscarDisponibilidade()
            }, horaFim.get(Calendar.HOUR_OF_DAY), horaFim.get(Calendar.MINUTE), true).show()
        }

        // 4. Botão Atualizar
        findViewById<ImageButton>(R.id.btnAtualizarFiltro).setOnClickListener {
            buscarDisponibilidade()
        }

        // Botão Voltar
        findViewById<ImageButton>(R.id.LoginArrowBack).setOnClickListener { finish() }
    }

    private fun atualizarUIFiltros() {
        findViewById<TextView>(R.id.txtTituloData).text = sdfData.format(dataFiltro.time)
        findViewById<TextView>(R.id.btnHoraInicio).text = sdfHora.format(horaInicio.time)
        findViewById<TextView>(R.id.btnHoraFim).text = sdfHora.format(horaFim.time)
    }

    // --- O MOTOR DA FILTRAGEM ---
    private fun buscarDisponibilidade() {
        // 1. Define o intervalo de TEMPO desejado pelo usuário
        // Combina a Data escolhida com a Hora escolhida
        val filtroInicio = dataFiltro.clone() as Calendar
        filtroInicio.set(Calendar.HOUR_OF_DAY, horaInicio.get(Calendar.HOUR_OF_DAY))
        filtroInicio.set(Calendar.MINUTE, horaInicio.get(Calendar.MINUTE))

        val filtroFim = dataFiltro.clone() as Calendar
        filtroFim.set(Calendar.HOUR_OF_DAY, horaFim.get(Calendar.HOUR_OF_DAY))
        filtroFim.set(Calendar.MINUTE, horaFim.get(Calendar.MINUTE))

        // Validação
        if (filtroFim.before(filtroInicio)) {
            Toast.makeText(this, "Hora final deve ser maior que inicial", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Busca no Firebase TODAS as reservas desse dia (00:00 a 23:59)
        val inicioDia = dataFiltro.clone() as Calendar
        inicioDia.set(Calendar.HOUR_OF_DAY, 0); inicioDia.set(Calendar.MINUTE, 0)
        val fimDia = dataFiltro.clone() as Calendar
        fimDia.set(Calendar.HOUR_OF_DAY, 23); fimDia.set(Calendar.MINUTE, 59)

        db.collection("reservas")
            .whereGreaterThanOrEqualTo("startTime", inicioDia.time)
            .whereLessThanOrEqualTo("startTime", fimDia.time)
            .get()
            .addOnSuccessListener { documents ->
                salasOcupadasNoHorario.clear()

                val millisInicioFiltro = filtroInicio.timeInMillis
                val millisFimFiltro = filtroFim.timeInMillis

                for (doc in documents) {
                    val nomeSala = doc.getString("spaceName") ?: ""

                    // Pega horário da reserva existente
                    val inicioReserva = doc.getTimestamp("startTime")?.toDate()?.time ?: 0
                    val fimReserva = doc.getTimestamp("endTime")?.toDate()?.time ?: 0

                    // LÓGICA DE COLISÃO:
                    // Se a reserva existente sobrepõe o horário que eu quero, a sala está ocupada.
                    if (millisInicioFiltro < fimReserva && millisFimFiltro > inicioReserva) {
                        salasOcupadasNoHorario.add(nomeSala)
                    }
                }

                // 3. Carrega as salas e pinta
                carregarSalas()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar agenda", Toast.LENGTH_SHORT).show()
                carregarSalas() // Carrega mesmo assim
            }
    }

    private fun carregarSalas() {
        db.collection("espacos")
            .orderBy("nome")
            .get()
            .addOnSuccessListener { documents ->
                listaEspacos.clear()
                for (document in documents) {
                    val espaco = document.toObject(EspacoModel::class.java)
                    espaco.id = document.id

                    // SE estiver na lista de colisão, fica Cinza
                    if (salasOcupadasNoHorario.contains(espaco.nome)) {
                        espaco.status = "ocupado"
                    } else {
                        espaco.status = "disponivel"
                    }
                    listaEspacos.add(espaco)
                }

                val recycler = findViewById<RecyclerView>(R.id.rvEspacos)
                adapter = EspacosAdapter(listaEspacos) { espaco ->
                    if (espaco.status == "ocupado") {
                        AlertDialog.Builder(this)
                            .setTitle("Ocupado")
                            .setMessage("A sala ${espaco.nome} já está reservada neste horário.")
                            .setPositiveButton("OK", null).show()
                    } else {
                        confirmarReserva(espaco)
                    }
                }
                recycler?.adapter = adapter
            }
    }

    private fun confirmarReserva(espaco: EspacoModel) {
        // Passa TUDO pronto para a tela de confirmação
        val intent = Intent(this, UserSpaceBooking::class.java)
        intent.putExtra("ESPACO_ID", espaco.id)
        intent.putExtra("ESPACO_NOME", espaco.nome)

        // Passamos os milissegundos exatos que o usuário filtrou
        // Precisamos combinar a data + hora escolhida
        val calInicio = dataFiltro.clone() as Calendar
        calInicio.set(Calendar.HOUR_OF_DAY, horaInicio.get(Calendar.HOUR_OF_DAY))
        calInicio.set(Calendar.MINUTE, horaInicio.get(Calendar.MINUTE))

        val calFim = dataFiltro.clone() as Calendar
        calFim.set(Calendar.HOUR_OF_DAY, horaFim.get(Calendar.HOUR_OF_DAY))
        calFim.set(Calendar.MINUTE, horaFim.get(Calendar.MINUTE))

        intent.putExtra("DATA_INICIO_MILLIS", calInicio.timeInMillis)
        intent.putExtra("DATA_FIM_MILLIS", calFim.timeInMillis)

        startActivity(intent)
    }
}