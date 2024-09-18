@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.auth

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.yield
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BiometricPromptManager(
    private val activity: AppCompatActivity
) {
    suspend fun showBiometricPrompt(
        title: String,
        description: String
    ): BiometricResult {
        return suspendCancellableCoroutine { continuation ->
            val manager = BiometricManager.from(activity)
            val authenticators =
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setDescription(description)
                .setAllowedAuthenticators(authenticators)

            when (manager.canAuthenticate(authenticators)) {
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    continuation.resume(BiometricResult.HardwareUnavailable)
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    continuation.resume(BiometricResult.FeatureUnavailable)
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    continuation.resumeWithException(Exception(BiometricResult.AuthenticationNotSet.toString()))
                    return@suspendCancellableCoroutine
                }

                else -> Unit
            }

            val prompt = BiometricPrompt(
                activity,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        continuation.resume(BiometricResult.AuthenticationError(errString.toString()))
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        continuation.resume(BiometricResult.AuthenticationSuccess)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        continuation.resume(BiometricResult.AuthenticationFailed)
                    }
                }
            )
            prompt.authenticate(promptInfo.build())

            continuation.invokeOnCancellation {
                prompt.cancelAuthentication()
            }
        }
    }
}