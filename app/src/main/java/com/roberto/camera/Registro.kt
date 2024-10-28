package com.roberto.camera

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros")
data class Registro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val descripcion: String
)
