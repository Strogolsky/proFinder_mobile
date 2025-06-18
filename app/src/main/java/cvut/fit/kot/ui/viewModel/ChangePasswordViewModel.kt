package cvut.fit.kot.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.ChangePasswordRequest
import cvut.fit.kot.domain.useCase.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val useCase: ChangePasswordUseCase
) : ViewModel() {

    data class State(
        val old: String = "",
        val new: String = "",
        val confirm: String = "",
        val loading: Boolean = false,
        val error: String? = null,
        val success: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun setOld(v: String)      { _state.value = _state.value.copy(old = v,  error = null) }
    fun setNew(v: String)      { _state.value = _state.value.copy(new = v,  error = null) }
    fun setConfirm(v: String)  { _state.value = _state.value.copy(confirm = v, error = null) }


    fun save(onSuccess: () -> Unit) = viewModelScope.launch {
        val s = _state.value
        when {
            s.loading                      -> return@launch
            s.new.isBlank()                -> _state.updateErr("New password is empty")
            s.new != s.confirm             -> _state.updateErr("Passwords don’t match")
            else -> {
                _state.value = s.copy(loading = true)
                val result = useCase.invoke(
                    ChangePasswordRequest(s.old, s.new, s.confirm)
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
