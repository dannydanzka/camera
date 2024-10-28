package com.roberto.camera

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegistroAdapter(
    private val registros: List<Registro>,
    private val onDeleteClick: (Registro, Int) -> Unit
) : RecyclerView.Adapter<RegistroAdapter.RegistroViewHolder>() {

    inner class RegistroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codigoTextView: TextView = itemView.findViewById(R.id.textCodigo)
        val descripcionTextView: TextView = itemView.findViewById(R.id.textDescripcion)
        val deleteButton: TextView = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_registro, parent, false)
        return RegistroViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegistroViewHolder, position: Int) {
        val registro = registros[position]
        holder.codigoTextView.text = registro.codigo
        holder.descripcionTextView.text = registro.descripcion
        holder.deleteButton.setOnClickListener {
            onDeleteClick(registro, position)
        }
    }

    override fun getItemCount(): Int = registros.size
}
