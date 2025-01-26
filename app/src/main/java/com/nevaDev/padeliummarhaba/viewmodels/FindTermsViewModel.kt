package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.FindTermsResponse
import com.padelium.domain.usecases.FindTermsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class FindTermsViewModel @Inject constructor(
    private val findTermsUseCase: FindTermsUseCase
) : ViewModel() {

    private val _players = MutableLiveData<DataResult>()
    val players: LiveData<DataResult> = _players

    private val _playerFullNames = MutableLiveData<List<String>>(emptyList())
    val playerFullNames: LiveData<List<String>> = _playerFullNames

    private val _selectedPlayers = MutableLiveData<MutableList<Long>>(mutableListOf())
    val selectedPlayers: LiveData<MutableList<Long>> = _selectedPlayers

    private var allPlayersData: List<FindTermsResponse> = emptyList()
    private var searchJob: Job? = null


    private val _sharedExtras = MutableLiveData<List<Long>>(emptyList())
    val sharedExtras: LiveData<List<Long>> = _sharedExtras

    private val _privateExtras = MutableLiveData<List<Long>>(emptyList())
    val privateExtras: LiveData<List<Long>> = _privateExtras

    // Function to update the shared extras list
    fun updateSharedExtras(newSharedExtras: List<Long>) {
        _sharedExtras.value = newSharedExtras
    }

    // Function to update the private extras list
    fun updatePrivateExtras(newPrivateExtras: List<Long>) {
        _privateExtras.value = newPrivateExtras
    }
    /**
     * Fetch players matching the search term.
     */
    fun findTerms(term: RequestBody) {
        // Cancel the previous job if it's still running
        searchJob?.cancel()

        // Start a new job to debounce the input
        searchJob = viewModelScope.launch {
            delay(150)
            _players.value = DataResult.Loading
            try {
                val result = findTermsUseCase.FindTerms(term)
                if (result is DataResult.Success) {
                    allPlayersData = result.data as? List<FindTermsResponse> ?: emptyList()
                    _players.value = DataResult.Success(allPlayersData)
                    _playerFullNames.value = allPlayersData.map { it.fullName }
                } else {
                    _players.value = result
                }
            } catch (e: Exception) {
                _players.value = DataResult.Failure(exception = e, errorCode = null, errorMessage = e.localizedMessage)
            }
        }
    }

    /**
     * Get a player by their full name.
     */
    fun getPlayerByFullName(fullName: String): FindTermsResponse? {
        return allPlayersData.find { it.fullName.equals(fullName, ignoreCase = true) }
    }


    /**
     * Add a selected player's ID to the list.
     */
    fun addSelectedPlayer(playerId: Long) {
        val updatedList = _selectedPlayers.value ?: mutableListOf()
        if (!updatedList.contains(playerId)) {
            updatedList.add(playerId)
            _selectedPlayers.value = updatedList
        }
    }

    /**
     * Remove a selected player's ID from the list.
     */
    fun removeSelectedPlayer(playerId: Long) {
        val updatedList = _selectedPlayers.value ?: mutableListOf()
        if (updatedList.contains(playerId)) {
            updatedList.remove(playerId)
            _selectedPlayers.value = updatedList
        }
    }
}





