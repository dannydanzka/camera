package com.roberto.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LectorActivity : AppCompatActivity() {
    private lateinit var codigo: EditText
    private lateinit var descripcion: EditText
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var registroDao: RegistroDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        codigo = findViewById(R.id.edtCodigo)
        descripcion = findViewById(R.id.edtDescripcion)
        val btnEscanear = findViewById<Button>(R.id.btnEscanear)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val btnVerRegistros = findViewById<Button>(R.id.btnVerRegistros)
        val btnAtras = findViewById<Button>(R.id.btnAtras)

        // Inicializar DAO
        registroDao = AppDatabase.getDatabase(this).registroDao()

        btnEscanear.setOnClickListener { escanearCodigo() }
        btnRegistrar.setOnClickListener { registrarCodigo() }
        btnLimpiar.setOnClickListener { limpiar() }
        btnVerRegistros.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // Configuración del botón "Atrás" para cerrar la actividad
        btnAtras.setOnClickListener {
            finish()
        }

        cameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                iniciarEscaneo()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun escanearCodigo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            iniciarEscaneo()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun iniciarEscaneo() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Escanea un código")
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(false)
        integrator.setCaptureActivity(CustomScannerActivity::class.java)
        integrator.initiateScan()
    }

    private fun registrarCodigo() {
        val codigoLeido = codigo.text.toString()
        val desc = descripcion.text.toString()
        if (codigoLeido.isNotEmpty() && desc.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                registroDao.insertRegistro(Registro(0, codigoLeido, desc))
            }
            Toast.makeText(this, "Código registrado", Toast.LENGTH_SHORT).show()
            limpiar()
        } else {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiar() {
        codigo.setText("")
        descripcion.setText("")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                codigo.setText(result.contents) // Establecer el valor escaneado en el EditText
                Toast.makeText(this, "Código escaneado: ${result.contents}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
