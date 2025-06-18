package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ForgotPasswordRequest
import cvut.fit.kot.domain.useCase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val useCase: ForgotPasswordUseCase
) : ViewModel() {

    data class State(
        val email: String = "",
        val loading: Boolean = false,
        val error: String? = null,
        val success: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun setEmail(v: String) { _state.value = _state.value.copy(email = v, error = null) }

    fun send(onSuccess: () -> Unit) = viewModelScope.launch {
        val s = _state.value
        when {
            s.loading                -> return@launch
            s.email.isBlank()        -> _state.updateErr("Email is empty")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches() ->
                _state.updateErr("Invalid email")
            else -> {
                _state.value = s.copy(loading = true)
                val result = useCase.invoke(ForgotPasswordRequest(s.email))
                _state.value = _state.value.copy(loading = false)
                result.fold(
                    onSuccess = {
                        _state.value = _state.value.copy(success = true)
                        onSuccess()
                    },
                    onFailure = { e -> _state.updateErr(e.localizedMessage ?: "Unknown error") }
                )
            }
        }
    }

    private fun MutableStateFlow<State>.updateErr(msg: String) {
        value = value.copy(error = msg)
    }
}
