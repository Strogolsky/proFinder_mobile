package cvut.fit.kot.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cvut.fit.kot.data.model.SignUpRequest
import cvut.fit.kot.data.model.SignUpResponse
import cvut.fit.kot.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _signUpResponse = MutableLiveData<Response<SignUpResponse>>()
    val signUpResponse: LiveData<Response<SignUpResponse>> = _signUpResponse

    fun signUp(email: String, password: String) {
        Log.d("SignUp", "Отправляю запрос: email=$email, password=$password")
        val request = SignUpRequest(email, password)
        viewModelScope.launch {
            val response = repository.signUp(request)
            _signUpResponse.postValue(response)
        }
    }
}