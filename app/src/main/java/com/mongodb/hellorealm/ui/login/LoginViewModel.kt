package com.mongodb.hellorealm.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.mongodb.hellorealm.R
import io.realm.mongodb.App
import io.realm.mongodb.Credentials


class LoginViewModel(private val realmSync: App) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _isRegistrationRequested = MutableLiveData(false)
    val isRegistrationRequested: LiveData<Boolean> = _isRegistrationRequested

    private fun login(username: String, password: String) {
        val creds = Credentials.emailPassword(username, password)
        realmSync.loginAsync(creds) {
            _loginResult.value = it.isSuccess
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun registerUser(username: String, password: String) {
        realmSync.emailPassword.registerUserAsync(username, password) {
            // re-enable the buttons after user registration returns a result
            if (it.isSuccess) {
                realmSync.currentUser()
            } else {
                Log.i("LoginViewModel", "Successfully registered user.")
                // when the account has been created successfully, log in to the account
            }
        }
    }

    fun onRegistrationClicked() {
        val status = isRegistrationRequested.value ?: return
        _isRegistrationRequested.value = !status
    }

    fun onSubmit(username: String, password: String) {
        val status = _isRegistrationRequested.value ?: true
        if (status)
            registerUser(username = username, password = password)
        else
            login(username = username, password = password)
    }

}