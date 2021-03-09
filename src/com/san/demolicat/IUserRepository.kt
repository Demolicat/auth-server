package com.san.demolicat

import io.ktor.auth.*

interface IUserRepository {
    fun findUserByName(name: String): User?
    fun findUserByCredentials(credentials: UserPasswordCredential): User?
    fun registerNewUser(name: String, password: String): Boolean
    fun registerNewUser(credentials: UserPasswordCredential): Boolean
}
