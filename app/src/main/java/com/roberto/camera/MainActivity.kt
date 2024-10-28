package com.roberto.camera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLector = findViewById<Button>(R.id.btnLector)
        val btnCamara = findViewById<Button>(R.id.btnCamara) // Cambia aquí a btnCamara
        val btnVerRegistros = findViewById<Button>(R.id.btnVerRegistros)

        btnLector.setOnClickListener {
            val intent = Intent(this, LectorActivity::class.java)
            startActivity(intent)
        }

        btnCamara.setOnClickListener {
            val intent = Intent(this, FotoActivity::class.java)
            startActivity(intent)
        }

        btnVerRegistros.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
