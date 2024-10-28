package com.roberto.camera

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroActivity : AppCompatActivity() {

    private lateinit var registrosRecyclerView: RecyclerView
    private lateinit var registroAdapter: RegistroAdapter
    private lateinit var registroDao: RegistroDao
    private lateinit var tvNoRegistros: TextView
    private val registros = mutableListOf<Registro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Configuración de la barra de acción
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar DAO
        registroDao = AppDatabase.getDatabase(this).registroDao()

        // Configuración del RecyclerView
        registrosRecyclerView = findViewById(R.id.recyclerViewRegistros)
        registrosRecyclerView.layoutManager = LinearLayoutManager(this)

        // Texto para mostrar cuando no hay registros
        tvNoRegistros = findViewById(R.id.tvNoRegistros)

        // Botón para volver
        val btnAtras = findViewById<Button>(R.id.btnAtras)
        btnAtras.setOnClickListener { finish() }

        // Inicializar adaptador y cargar registros
        registroAdapter = RegistroAdapter(registros) { registro, position ->
            mostrarConfirmacionEliminacion(registro, position)
        }
        registrosRecyclerView.adapter = registroAdapter
        cargarRegistros()
    }

    private fun mostrarConfirmacionEliminacion(registro: Registro, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
        builder.setMessage("¿Deseas eliminar el registro: ${registro.descripcion}?")
        builder.setPositiveButton("Eliminar") { _, _ ->
            eliminarRegistro(registro, position)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun cargarRegistros() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val registrosList = registroDao.getAllRegistros()
                registros.clear()
                registros.addAll(registrosList)
            }
            registroAdapter.notifyDataSetChanged()
            // Mostrar u ocultar el mensaje "No hay registros"
            tvNoRegistros.visibility = if (registros.isEmpty()) TextView.VISIBLE else TextView.GONE
        }
    }

    private fun eliminarRegistro(registro: Registro, position: Int) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                registroDao.deleteRegistro(registro)
            }
            registros.removeAt(position)
            registroAdapter.notifyItemRemoved(position)

            // Actualizar la visibilidad del mensaje "No hay registros"
            tvNoRegistros.visibility = if (registros.isEmpty()) TextView.VISIBLE else TextView.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
