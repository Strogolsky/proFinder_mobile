package cvut.fit.kot.ui.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ResetPasswordRequest
import cvut.fit.kot.data.useCase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val useCase: ResetPasswordUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    data class State(
        val code: String = "",
        val new: String = "",
        val confirm: String = "",
        val loading: Boolean = false,
        val error: String? = null,
        val success: Boolean = false
    )

    private val emailFlow = savedStateHandle.getStateFlow("email", "")
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun setCode(v: String)    { _state.value = _state.value.copy(code = v,    error = null) }
    fun setNew(v: String)     { _state.value = _state.value.copy(new = v,     error = null) }
    fun setConfirm(v: String) { _state.value = _state.value.copy(confirm = v, error = null) }

    fun save(onSuccess: () -> Unit) = viewModelScope.launch {
        val email = emailFlow.value
        val s = _state.value
        when {
            s.loading              -> return@launch
            s.code.isBlank()       -> _state.updateErr("Verification code is empty")
            s.new.isBlank()        -> _state.updateErr("New password is empty")
            s.new != s.confirm     -> _state.updateErr("Passwords don’t match")
            else -> {
                _state.value = s.copy(loading = true)
                val result = useCase.execute(
                    ResetPasswordRequest(
                        email             = email,
                        newPassword       = s.new,
                        confirmPassword   = s.confirm,
                        verificationCode  = s.code
                    )
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

    private fun MutableStateFlow<State>.updateErr(msg: String) {
        value = value.copy(error = msg)
    }
}