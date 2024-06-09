package com.tesji.apipokemon

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("listaPokemons") var listaPokemons: ArrayList<Pokemon>
)
