package com.amk.sakoo.ui.features.profileScreen

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.ViewModel
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.entity.PurchaseInfo
import ir.cafebazaar.poolakey.request.PurchaseRequest

class PaymentViewModel(

) : ViewModel() {

    lateinit var localSecurityCheck: SecurityCheck
    lateinit var paymentConfiguration: PaymentConfiguration
    lateinit var payment: Payment

    fun initSecurityCheck(rsaPublicKey: String) {
        localSecurityCheck = SecurityCheck.Enable(rsaPublicKey = rsaPublicKey)
    }

    fun initPaymentConfiguration() {
        paymentConfiguration = PaymentConfiguration(
            localSecurityCheck = localSecurityCheck
        )
    }

    fun initPayment(context: Context) {
        payment = Payment(context = context, config = paymentConfiguration)
    }

    fun connectToBazaar(
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit,
        onDisconnected: () -> Unit
    ) {
        payment.connect {
            connectionSucceed {
                onSuccess()
            }
            connectionFailed { throwable ->
                onFailure(throwable)
            }
            disconnected {
                onDisconnected()
            }
        }
    }

    fun startPurchase(
        productId: String,
        payload: String,
        activityResultRegistry: ActivityResultRegistry,
        onSuccess: (PurchaseInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val purchaseRequest = PurchaseRequest(
            productId = productId,
            payload = payload
        )

        payment.purchaseProduct(
            registry = activityResultRegistry,
            request = purchaseRequest
        ) {
            purchaseFlowBegan {

            }
            failedToBeginFlow { throwable ->

            }
            purchaseSucceed { purchaseEntity ->
                onSuccess.invoke(purchaseEntity)
            }
            purchaseCanceled {

            }
            purchaseFailed { throwable ->
                onFailure.invoke(throwable)
            }
        }
    }

    fun consumePurchase(
        token: String, onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        payment.consumeProduct(token) {
            consumeSucceed {
                onSuccess.invoke()
            }
            consumeFailed { throwable ->
                onFailure.invoke(throwable)
            }
        }
    }
}