package be.mauricedeke.shinkai.ui.login

import be.mauricedeke.shinkai.data.local.datastore.AppDataStore
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import be.mauricedeke.shinkai.data.remote.api.AuthApi
import be.mauricedeke.shinkai.data.remote.client.AuthClient
import be.mauricedeke.shinkai.data.remote.dto.AuthResponseDto
import be.mauricedeke.shinkai.data.remote.dto.ChangePasswordRequestDto
import be.mauricedeke.shinkai.data.remote.dto.LoginRequestDto
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var fakeApi: FakeAuthApi
    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeApi = FakeAuthApi()
        vm = LoginViewModel(
            authClient = AuthClient(fakeApi),
            tokenStore = AuthTokenStore(),
            appDataStore = mockk(relaxed = true)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasEmptyFields() {
        val state = vm.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.loginSuccess)
    }

    @Test
    fun onEmailChanged_updatesEmail() {
        vm.onEmailChanged("user@shinkai.be")
        assertEquals("user@shinkai.be", vm.uiState.value.email)
    }

    @Test
    fun onPasswordChanged_updatesPassword() {
        vm.onPasswordChanged("s3cret")
        assertEquals("s3cret", vm.uiState.value.password)
    }

    @Test
    fun onEmailChanged_clearsExistingError() {
        vm.onLoginClick()
        vm.onEmailChanged("new@shinkai.be")
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun onPasswordChanged_clearsExistingError() {
        vm.onLoginClick()
        vm.onPasswordChanged("newpass")
        assertNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun onLoginClick_blankEmail_setsErrorMessage() {
        vm.onEmailChanged("")
        vm.onPasswordChanged("password")
        vm.onLoginClick()
        assertNotNull(vm.uiState.value.errorMessage)
        assertEquals("Vul je e-mail en wachtwoord in.", vm.uiState.value.errorMessage)
    }

    @Test
    fun onLoginClick_blankPassword_setsErrorMessage() {
        vm.onEmailChanged("user@shinkai.be")
        vm.onPasswordChanged("")
        vm.onLoginClick()
        assertEquals("Vul je e-mail en wachtwoord in.", vm.uiState.value.errorMessage)
    }

    @Test
    fun onLoginClick_blankBoth_setsErrorMessage() {
        vm.onLoginClick()
        assertNotNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun onLoginClick_blankFields_doesNotStartLoading() {
        vm.onLoginClick()
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun onLoginClick_validCredentials_loginSuccess_setsLoginSuccessTrue() = runTest {
        fakeApi.result = Result.success(AuthResponseDto("access_token", "user123", false))
        vm.onEmailChanged("user@shinkai.be")
        vm.onPasswordChanged("password")
        vm.onLoginClick()
        assertTrue(vm.uiState.value.loginSuccess)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun onLoginClick_networkFailure_setsErrorMessage() = runTest {
        fakeApi.result = Result.failure(Exception("Network error"))
        vm.onEmailChanged("user@shinkai.be")
        vm.onPasswordChanged("wrongpass")
        vm.onLoginClick()
        assertEquals("Ongeldige inloggegevens.", vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun onLoginHandled_clearsLoginSuccess() = runTest {
        fakeApi.result = Result.success(AuthResponseDto("token", "user", false))
        vm.onEmailChanged("u@t.com")
        vm.onPasswordChanged("pw")
        vm.onLoginClick()
        vm.onLoginHandled()
        assertFalse(vm.uiState.value.loginSuccess)
    }

    @Test
    fun onLoginClick_success_mustChangePassword_propagatesToState() = runTest {
        fakeApi.result = Result.success(AuthResponseDto("token", "user", mustChangePassword = true))
        vm.onEmailChanged("u@t.com")
        vm.onPasswordChanged("pw")
        vm.onLoginClick()
        assertTrue(vm.uiState.value.mustChangePassword)
    }

    private class FakeAuthApi : AuthApi {
        var result: Result<AuthResponseDto> = Result.success(AuthResponseDto("", "", false))

        override suspend fun login(body: LoginRequestDto): AuthResponseDto = result.getOrThrow()
        override suspend fun changePassword(body: ChangePasswordRequestDto) {}
    }
}
