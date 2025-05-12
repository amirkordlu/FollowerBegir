package com.amk.followerbegir.ui.features.profileScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.followerbegir.util.coroutineExceptionHandler
import com.farsitel.bazaar.core.BazaarSignIn
import com.farsitel.bazaar.storage.BazaarStorage
import com.farsitel.bazaar.storage.callback.BazaarStorageCallback
import com.farsitel.bazaar.util.ext.toReadableString
import kotlinx.coroutines.launch

class AccountViewModel(

) : ViewModel() {

    val userID = mutableStateOf("")
    val hasLogin = mutableStateOf(false)

    val savedData = mutableStateOf("")
    val points = mutableStateOf<Int?>(null)

    val isLoading = mutableStateOf(true)

    fun handleSignInResult(intent: Intent?) {
        val account = intent?.let { BazaarSignIn.getSignedInAccountFromIntent(it) }
        if (account != null && account.accountId.isNotEmpty()) {
            userID.value = account.accountId
            hasLogin.value = true
        }
    }

    fun getBazaarLogin(context: Context, lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(coroutineExceptionHandler) {
            BazaarSignIn.getLastSignedInAccount(
                context, lifecycleOwner
            ) { response ->

                val account = response?.data

                if (account?.accountId?.isNotEmpty() == true) {
                    userID.value = account.accountId
                    hasLogin.value = true
                    Log.v("AccountViewModel", account.accountId.toString())
                }
            }
        }
    }

    fun saveDataInBazaar(context: Context, lifecycleOwner: LifecycleOwner, data: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            BazaarStorage.saveData(context,
                lifecycleOwner,
                data = data.toByteArray(),
                BazaarStorageCallback { savedResponse ->
                    savedData.value = savedResponse?.data?.toReadableString().toString()
                })
        }
    }

    fun loadPointsFromBazaar(context: Context, lifecycleOwner: LifecycleOwner) {
        isLoading.value = true
        viewModelScope.launch(coroutineExceptionHandler) {
            BazaarStorage.getSavedData(
                context,
                lifecycleOwner,
                callback = BazaarStorageCallback { response ->
                    val currentPoints = response?.data?.toReadableString()?.toIntOrNull()
                    points.value = currentPoints
                    isLoading.value = false
                }
            )
        }
    }

    fun addPoints(context: Context, lifecycleOwner: LifecycleOwner, increment: Int) {
        val newPoints = (points.value ?: 0) + increment
        saveDataInBazaar(context, lifecycleOwner, newPoints.toString())
        points.value = newPoints
    }

    fun subtractPoints(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        decrement: Int,
        onError: (String) -> Unit
    ) {
        val currentPoints = points.value ?: 0
        val newPoints = currentPoints - decrement
        if (newPoints >= 0) {
            saveDataInBazaar(context, lifecycleOwner, newPoints.toString())
            points.value = newPoints
        } else {
            onError.invoke("Current point is 0")
        }
    }

}