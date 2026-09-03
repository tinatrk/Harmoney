package com.example.harmoney.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmoney.BuildConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event, Action, State>(state: State) : ViewModel() {

    protected val writableState = MutableStateFlow(state)
    val state: StateFlow<State> = writableState.asStateFlow()

    protected val writableAction = MutableSharedFlow<Action?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<Action?> = writableAction.asSharedFlow()

    abstract val tag: String

    abstract fun obtainEvent(event: Event)

    @Suppress("detekt:TooGenericExceptionCaught")
    protected fun launchWithErrorHandling(
        errorMessage: String,
        onError: (suspend (Throwable) -> Unit),
        block: suspend CoroutineScope.() -> Unit,
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e(tag, errorMessage, error)
                }
                onError.invoke(error)
            }
        }
    }

    protected fun <T> Flow<T>.collectWithErrorHandling(
        errorMessage: String,
        onError: suspend (Throwable) -> Unit
    ) {
        catch { error ->
            if (error is CancellationException) {
                throw error
            }

            if (BuildConfig.DEBUG) {
                Log.e(tag, errorMessage, error)
            }
            onError.invoke(error)
        }.launchIn(viewModelScope)
    }
}
