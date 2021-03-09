package com.san.demolicat

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val userSource = UserRepository()
    install(ContentNegotiation) {
        gson {

        }
    }
    install(Authentication) {
        /**
         * Setup the JWT authentication to be used in [Routing].
         * If the token is valid, the corresponding [User] is fetched from the database.
         * The [User] can then be accessed in each [ApplicationCall].
         */
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate {
                it.payload.getClaim("name").asString()?.let(userSource::findUserByName)
            }
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD hello!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }

        post("register") {
            val credentials = call.receive<UserPasswordCredential>()
            userSource.registerNewUser(credentials).let {
                if (it) {
                    call.respond("Successfull login")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Username is already in used")
                }
            }
        }

        post("login") {
            val credentials = call.receive<UserPasswordCredential>()
            userSource.findUserByCredentials(credentials)?.let { user ->
                JwtConfig.makeToken(user).let { token ->
                    call.respondText(token)
                }
            } ?: call.respond(HttpStatusCode.Unauthorized, "Username or password are incorrect")

        }
    }
}

