package com.tesji.apipokemon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class PokemonAdapter (
    var context: Context,
    var listapokemons: ArrayList<Pokemon>
): RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private var onClick: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_pokemon, parent, false)
        return PokemonViewHolder(vista)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = listapokemons.get(position)

        holder.tvIdPokemon.text = pokemon.idpoke.toString()
        holder.tvNombre.text = pokemon.nombre
        holder.tvTipo.text = pokemon.tipo
        holder.tvDescripcion.text = pokemon.descripcion

        holder.btnEditar.setOnClickListener {
            onClick?.editarPokemon(pokemon)
        }

        holder.btnBorrar.setOnClickListener {
            onClick?.borrarPokemon(pokemon.idpoke)
        }
    }

    override fun getItemCount(): Int {
        return listapokemons.size
    }

    inner class PokemonViewHolder(itemView: View): ViewHolder(itemView) {
        val tvIdPokemon = itemView.findViewById(R.id.tvIdPokemon) as TextView
        val tvNombre = itemView.findViewById(R.id.tvNombre) as TextView
        val tvTipo = itemView.findViewById(R.id.tvTipo) as TextView
        val tvDescripcion = itemView.findViewById(R.id.tvDescripcion) as TextView
        val btnEditar = itemView.findViewById(R.id.btnEditar) as Button
        val btnBorrar = itemView.findViewById(R.id.btnBorrar) as Button
    }

    interface OnItemClicked {
        fun editarPokemon(pokemon: Pokemon)
        fun borrarPokemon(idpoke: Int)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

}