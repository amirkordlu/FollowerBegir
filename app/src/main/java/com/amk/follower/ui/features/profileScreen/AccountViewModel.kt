package com.amk.follower.ui.features.profileScreen

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.follower.model.data.UserStoredData
import com.amk.follower.util.coroutineExceptionHandler
import com.farsitel.bazaar.core.BazaarSignIn
import com.farsitel.bazaar.storage.BazaarStorage
import com.farsitel.bazaar.storage.callback.BazaarStorageCallback
import com.farsitel.bazaar.util.ext.toReadableString
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AccountViewModel : ViewModel() {

    val userID = mutableStateOf("")
    val hasLogin = mutableStateOf(false)
    val wallet = mutableStateOf(0)
    val orderNumbers = mutableStateOf<List<Int>>(emptyList())
    val isLoading = mutableStateOf(false)
    val isLoginCheckInProgress = mutableStateOf(true)

    fun handleSignInResult(intent: Intent?) {
        val account = intent?.let { BazaarSignIn.getSignedInAccountFromIntent(it) }
        if (account != null && account.accountId.isNotEmpty()) {
            userID.value = account.accountId
            hasLogin.value = true
        }
    }

    fun getBazaarLogin(context: Context, owner: LifecycleOwner) {
        isLoginCheckInProgress.value = true
        viewModelScope.launch(coroutineExceptionHandler) {
            BazaarSignIn.getLastSignedInAccount(context, owner) { response ->
                val account = response?.data
                if (account?.accountId?.isNotEmpty() == true) {
                    userID.value = account.accountId
                    hasLogin.value = true
                }
                isLoginCheckInProgress.value = false
            }
        }
    }

    private fun saveUserData(context: Context, owner: LifecycleOwner, data: UserStoredData) {
        val json = Json.encodeToString(data)
        BazaarStorage.saveData(context, owner, json.toByteArray(), BazaarStorageCallback { })
    }

    fun loadUserData(context: Context, owner: LifecycleOwner) {
        isLoading.value = true
        viewModelScope.launch(coroutineExceptionHandler) {
            BazaarStorage.getSavedData(context, owner, BazaarStorageCallback { response ->
                val json = response?.data?.toReadableString().orEmpty()
                val userData = try {
                    Json.decodeFromString<UserStoredData>(json)
                } catch (e: Exception) {
                    UserStoredData(0, emptyList())
                }
                wallet.value = userData.wallet
                orderNumbers.value = userData.orderNumbers
                isLoading.value = false
            })
        }
    }

    fun increaseWallet(context: Context, owner: LifecycleOwner, amount: Int) {
        updateUserData(context, owner) { data ->
            data.copy(wallet = data.wallet + amount)
        }
    }

    fun decreaseWallet(
        context: Context,
        owner: LifecycleOwner,
        amount: Int,
        onError: (String) -> Unit
    ) {
        updateUserData(context, owner) { data ->
            if (data.wallet >= amount) {
                data.copy(wallet = data.wallet - amount)
            } else {
                onError("موجودی کافی نیست")
                return@updateUserData null
            }
        }
    }

    fun addOrderNumber(context: Context, owner: LifecycleOwner, order: Int) {
        updateUserData(context, owner) { data ->
            data.copy(orderNumbers = data.orderNumbers + order)
        }
    }

    private fun updateUserData(
        context: Context,
        owner: LifecycleOwner,
        update: (UserStoredData) -> UserStoredData?
    ) {
        viewModelScope.launch(coroutineExceptionHandler) {
            BazaarStorage.getSavedData(context, owner, BazaarStorageCallback { response ->
                val json = response?.data?.toReadableString().orEmpty()
                val current = try {
                    Json.decodeFromString<UserStoredData>(json)
                } catch (e: Exception) {
                    UserStoredData(0, emptyList())
                }

                val updated = update(current) ?: return@BazaarStorageCallback
                wallet.value = updated.wallet
                orderNumbers.value = updated.orderNumbers
                saveUserData(context, owner, updated)
            })
        }
    }
}
