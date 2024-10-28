package com.roberto.camera

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegistroDao {
    @Query("SELECT * FROM registros")
    fun getAllRegistros(): List<Registro> // Retorno sin Flow

    @Insert
    fun insertRegistro(registro: Registro) // Sin suspend

    @Delete
    fun deleteRegistro(registro: Registro) // Sin suspend, eliminando un solo objeto Registro
}
