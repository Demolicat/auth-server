package com.san.demolicat

import io.ktor.auth.*
import org.mindrot.jbcrypt.BCrypt

class UserRepository : IUserRepository {
    private val dataSource = mutableMapOf<String, User>()

    override fun findUserByName(name: String): User {
        return dataSource[name] ?: throw NoSuchElementException()
    }

    override fun findUserByCredentials(credentials: UserPasswordCredential): User? {
        return dataSource[credentials.name]?.run {
            if (BCrypt.checkpw(credentials.password, password)) {
                print(password)
                this
            } else {
                null
            }
        }
    }

    override fun registerNewUser(credentials: UserPasswordCredential): Boolean {
        return registerNewUser(credentials.name, credentials.password)
    }

    override fun registerNewUser(name: String, password: String): Boolean {
        return if (name !in dataSource) {
            dataSource[name] = User(name, hashPassword(password))
            true
        } else {
            false
        }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}