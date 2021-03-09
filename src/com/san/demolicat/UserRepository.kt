package com.san.demolicat

import io.ktor.auth.*

class UserRepository : IUserRepository {
    private val dataSource = mutableMapOf<String, User>()

    override fun findUserByName(name: String): User {
        return dataSource[name] ?: throw NoSuchElementException()
    }

    override fun findUserByCredentials(credentials: UserPasswordCredential): User? {
        return dataSource[credentials.name]?.run {
            if (password == credentials.password) {
                this
            } else {
                null
            }
        }
    }

    override fun registerNewUser(name: String, password: String) {
        dataSource[name] = User(name, password)
    }

    override fun registerNewUser(credentials: UserPasswordCredential) {
        dataSource[credentials.name] = User(credentials.name, credentials.password)
    }
}