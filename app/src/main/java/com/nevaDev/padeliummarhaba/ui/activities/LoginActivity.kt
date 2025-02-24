package com.nevaDev.padeliummarhaba.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.compiler.plugins.kotlin.ComposeCallableIds.remember
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.ui.auth.UserPreferences
import com.nevaDev.padeliummarhaba.ui.views.CreditCharge
import com.nevaDev.padeliummarhaba.ui.views.CreditPayment
import com.nevaDev.padeliummarhaba.ui.views.LoginScreen
import com.nevaDev.padeliummarhaba.ui.views.PaymentSection1
import com.nevaDev.padeliummarhaba.ui.views.ProfileScreen
import com.nevaDev.padeliummarhaba.ui.views.ReservationOptions
import com.nevaDev.padeliummarhaba.ui.views.SignUpScreen
import com.nevaDev.padeliummarhaba.ui.views.SummaryScreen
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.LoginRequest
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject





class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            _isLoggedIn.value = userPreferences.isLoggedIn.first() // Read from DataStore
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.saveLoginState(isLoggedIn)
            _isLoggedIn.postValue(isLoggedIn)
        }
    }
}









