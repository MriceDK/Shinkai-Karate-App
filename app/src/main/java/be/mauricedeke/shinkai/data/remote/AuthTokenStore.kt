package be.mauricedeke.shinkai.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthTokenStore @Inject constructor() {
    var accessToken: String? = null
}
