package xyz.akko.projectchat.views.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val coroutineScope = MainScope()
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    val loginFinished = MutableLiveData(false)

    fun login(userName: String = "", passWord: String = "")
    {
        coroutineScope.launch {
            _isLoading.value = true
            delay(500)
            loginFinished.value = true
        }
    }
}