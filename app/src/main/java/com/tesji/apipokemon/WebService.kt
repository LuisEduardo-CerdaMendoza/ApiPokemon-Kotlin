package com.tesji.apipokemon

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object AppConstantes {
    const val BASE_URL = "http://10.0.8.110:3000"
}

interface WebService {

    @GET("/Pokemon")
    suspend fun obtenerPokemon(): Response<PokemonResponse>

    @GET("/Pokemon/{idpoke}")
    suspend fun obtenerPokemon(
        @Path("idpoke") idpoke : Int
    ): Response<Pokemon>

    @POST("/Pokemon/add")
    suspend fun agregarPokemon(
        @Body pokemon: Pokemon
    ): Response<String>

    @PUT("/Pokemon/update/{idpoke}")
    suspend fun actualizarPokemon(
        @Path("idpoke") idpoke : Int,
        @Body pokemon: Pokemon
    ): Response<String>

    @DELETE("/Pokemon/delete/{idpoke}")
    suspend fun borrarPokemon(
        @Path("idpoke") idpoke : Int
    ): Response<String>
}

object RetrofitClient {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}