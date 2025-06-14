package cvut.fit.kot.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ChangeEmailRequest
import cvut.fit.kot.data.useCase.ChangeEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val useCase: ChangeEmailUseCase
) : ViewModel() {

    data class State(
        val email: String = "",
        val password: String = "",
        val loading: Boolean = false,
        val error: String? = null,
        val success: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun setEmail(v: String)    { _state.value = _state.value.copy(email = v,     error = null) }
    fun setPassword(v: String) { _state.value = _state.value.copy(password = v,  error = null) }

    fun save(onSuccess: () -> Unit) = viewModelScope.launch {
        val s = _state.value
        when {
            s.loading                    -> return@launch
            s.email.isBlank()            -> _state.updateErr("Email is empty")
            !s.email.isValidEmail()      -> _state.updateErr("Email is invalid")
            s.password.isBlank()         -> _state.updateErr("Password is empty")
            else -> {
                _state.value = s.copy(loading = true)
                val result = useCase.execute(
                    ChangeEmailRequest(s.email, s.password)
                )
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

    private fun String.isValidEmail(): Boolean =
        contains("@") && contains(".")

    private fun MutableStateFlow<State>.updateErr(msg: String) {
        value = value.copy(error = msg)
    }
}
