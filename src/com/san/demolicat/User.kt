package com.san.demolicat

import io.ktor.auth.*

data class User(val name: String, val password: String): Principal