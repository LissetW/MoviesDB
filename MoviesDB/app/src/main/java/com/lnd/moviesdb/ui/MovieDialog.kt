package com.lnd.moviesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.lnd.moviesdb.R
import com.lnd.moviesdb.application.MoviesDBApp
import com.lnd.moviesdb.data.MovieRepository
import com.lnd.moviesdb.data.db.model.MovieEntity
import com.lnd.moviesdb.databinding.MovieDialogBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

class MovieDialog (
    private val newMovie: Boolean = true,
    private var movie: MovieEntity = MovieEntity(
        title = "",
        genre = "",
        director = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment(){
    // Agregar viewbinding al fragment
    private var _binding: MovieDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog
    // Obtener el boton de aceptar
    private var saveButton: Button? = null
    // Para el repositorio
    private lateinit var repository: MovieRepository

    // Crear y configurar el dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        _binding = MovieDialogBinding.inflate(requireActivity().layoutInflater)

        binding.apply {
            tietTitle.setText(movie.title)
            actvGenre.setText(movie.genre)
            tietDeveloper.setText(movie.director)
        }

        dialog = if (newMovie)
            buildDialog(getString(R.string.save_button), getString(R.string.cancel_button), {
                // Guardar
                val context = requireContext()
                binding.apply {
                    movie.title = tietTitle.text.toString()
                    movie.genre = actvGenre.text.toString()
                    movie.director = tietDeveloper.text.toString()
                }
                try{
                    lifecycleScope.launch {
                        val result = async {
                            repository.insertMovie(movie)
                        }
                        result.await()
                        message(context.getString(R.string.save_sucess))
                        updateUI()
                    }
                }catch (e: IOException){
                    //Manejar la expcepcion
                    e.printStackTrace()
                    message(context.getString(R.string.save_error))
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }, {
                // Cancelar
            })
        else
            buildDialog(getString(R.string.update_button), getString(R.string.delete_button), {
                // Actualizar
                val context = requireContext()

                binding.apply {
                    movie.title = tietTitle.text.toString()
                    movie.genre = actvGenre.text.toString()
                    movie.director = tietDeveloper.text.toString()
                }
                try{
                    lifecycleScope.launch {
                        val result = async {
                            repository.updateMovie(movie)
                        }
                        result.await()
                        message(context.getString(R.string.update_sucess))
                        updateUI()
                    }
                }catch (e: IOException){
                    //Manejar la expcepcion
                    e.printStackTrace()
                    message(context.getString(R.string.update_error))
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }, {
                // Eliminar

                // Duardar el contexto antes de la corrutina para que no se pierda
                val context = requireContext()

                // Dialogo para confirmar
                AlertDialog.Builder(requireContext())
                    .setTitle(context.getString(R.string.confirm_title))
                    .setMessage(context.getString(R.string.confirm_msg, movie.title))
                    .setPositiveButton(context.getString(R.string.accept_button)){ _, _ ->
                        try{
                            lifecycleScope.launch {
                                val result = async {
                                    repository.deleteMmovie(movie)
                                }
                                result.await()
                                message(context.getString(R.string.delete_success))
                                updateUI()
                            }
                        }catch (e: IOException){
                            //Manejar la expcepcion
                            e.printStackTrace()
                            message(context.getString(R.string.delete_error))
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton(context.getString(R.string.cancel_button)){ dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create()
                    .show()
            })

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama despues de que se muestra el dialogo en pantalla
    override fun onStart() {
        super.onStart()
        // Instanciar el repositorio
        repository = (requireContext().applicationContext as MoviesDBApp).repository

        //Referenciamos el boton "Guardar" del dialogo
        saveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        // Cargar géneros en el dropdown
        val genres = resources.getStringArray(R.array.genre_array).toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genres)
        binding.actvGenre.setAdapter(adapter)

        binding.apply {
            setupTextWatcher(
                tietTitle,
                actvGenre,
                tietDeveloper
            )
        }
    }

    private fun validateFields(): Boolean =
        binding.tietTitle.text.toString().isNotBlank() &&
                binding.actvGenre.text.toString().isNotBlank() &&
                binding.tietDeveloper.text.toString().isNotBlank()

    private fun setupTextWatcher(vararg textFields: EditText){
        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        }
        textFields.forEach{ it.addTextChangedListener(textWatcher) }
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text:String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit,
    ): Dialog =
        AlertDialog.Builder(requireContext()).setView(binding.root)
            .setTitle(getString(R.string.title))
            .setPositiveButton(btn1Text){ _, _ ->
                positiveButton()
            }
            .setNegativeButton(btn2Text){ _, _ ->
                negativeButton()
            }.create()
}