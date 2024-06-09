package com.tesji.apipokemon

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesji.apipokemon.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), PokemonAdapter.OnItemClicked {

    lateinit var binding: ActivityMainBinding

    lateinit var adatador: PokemonAdapter

    var listaPokemons = arrayListOf<Pokemon>()

    var pokemon = Pokemon(-1, "","", "")

    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPokemon.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()

        obtenerPokemon()

        binding.btnAddUpdate.setOnClickListener {
            var isValido = validarCampos()
            if (isValido) {
                if (!isEditando) {
                    agregarPokemon()
                } else {
                    actualizarPokemon()
                }
            } else {
                Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun setupRecyclerView() {
        adatador = PokemonAdapter(this, listaPokemons)
        adatador.setOnClick(this@MainActivity)
        binding.rvPokemon.adapter = adatador

    }

    fun validarCampos(): Boolean {
        return !(binding.etNombre.text.isNullOrEmpty() || binding.etTipo.text.isNullOrEmpty() || binding.etDescripcion.text.isNullOrEmpty())
    }

    fun obtenerPokemon() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerPokemon()
            runOnUiThread {
                if (call.isSuccessful) {
                    listaPokemons = call.body()!!.listaPokemons
                    setupRecyclerView()
                } else {
                    Toast.makeText(this@MainActivity, "ERROR CONSULTAR TODOS", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun agregarPokemon() {

        this.pokemon.idpoke = -1
        this.pokemon.nombre = binding.etNombre.text.toString()
        this.pokemon.tipo = binding.etTipo.text.toString()
        this.pokemon.descripcion = binding.etDescripcion.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarPokemon(pokemon)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@MainActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerPokemon()
                    limpiarCampos()
                    limpiarObjeto()

                } else {
                    Toast.makeText(this@MainActivity, "ERROR ADD", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun actualizarPokemon() {

        this.pokemon.nombre = binding.etNombre.text.toString()
        this.pokemon.tipo = binding.etTipo.text.toString()
        this.pokemon.descripcion = binding.etDescripcion.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.actualizarPokemon(pokemon.idpoke, pokemon)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@MainActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerPokemon()
                    limpiarCampos()
                    limpiarObjeto()

                    binding.btnAddUpdate.setText("Agregar Pokemon")
                    binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.green)
                    isEditando = false
                }
            }
        }
    }

    fun limpiarCampos() {
        binding.etNombre.setText("")
        binding.etTipo.setText("")
        binding.etDescripcion.setText("")
    }

    fun limpiarObjeto() {
        this.pokemon.idpoke = -1
        this.pokemon.nombre = ""
        this.pokemon.tipo = ""
        this.pokemon.descripcion = ""
    }

    override fun editarPokemon(pokemon: Pokemon) {
        binding.etNombre.setText(pokemon.nombre)
        binding.etTipo.setText(pokemon.tipo)
        binding.etDescripcion.setText(pokemon.descripcion)
        binding.btnAddUpdate.setText("Actualizar Pokemon")
        binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.purple_500)
        this.pokemon = pokemon
        isEditando = true
    }

    override fun borrarPokemon(idpoke: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarPokemon(idpoke)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@MainActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerPokemon()
                }
            }
        }
    }
}