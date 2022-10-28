package com.example.note_app.presentation.screens.note.view_model

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.compose.material.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.note_app.domain.entities.Note
import com.example.note_app.domain.repositories.INoteRepository
import com.example.note_app.infrastructure.model.NoteModel
import com.example.note_app.presentation.screens.note.view.NoteListActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.attribute.AclEntry
import javax.inject.Inject


@HiltViewModel
class NoteListViewModel @Inject constructor(private val repository: INoteRepository) : ViewModel() {
    var noteList = MutableLiveData<List<NoteModel>>(listOf())
        private set

    var showGrid = MutableLiveData(false)

    var noteForDelete = MutableLiveData<NoteModel>(null)


    fun getNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getNotes()
            viewModelScope.launch {
                noteList.value = result
            }
        }
    }

    fun deleteNote(note: NoteModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNote(note)
            viewModelScope.launch {
                noteList.value = noteList.value!!.takeWhile { x -> x.id != note.id }
                noteForDelete.value = null
            }
        }
    }

    fun changeVisibility() {
        showGrid.value = !showGrid.value!!
    }

}

