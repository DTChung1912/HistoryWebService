package com.example.route

import com.example.models.User
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityUser
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeUser() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/user") {

            get("/main")
            {
                val formParameters = call.receiveParameters()
                val test1 = formParameters["test1"].toString()
                val test2 = formParameters["test2"].toString()
                call.respondText("Welcome to Ktor Mysql $test1 , $test2")
            }

            get("/list") {
                val list = db.from(EntityUser)
                    .select()
                    .map {
                        User(
                            user_id = it[EntityUser.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            email = it[EntityUser.email].orEmpty(),
                            birthday = it[EntityUser.birthday].orEmpty(),
                            phone_number = it[EntityUser.phone_number].orEmpty(),
                            address = it[EntityUser.address].orEmpty(),
                            last_active = it[EntityUser.last_active].orEmpty(),
                            account_type_id = it[EntityUser.account_type_id] ?: 0,
                            access_id = it[EntityUser.access_id] ?: 0
                        )
                    }

                if (list.size > 0) {
                    call.respond(list)
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
                }
            }

            get("/detail")
            {
                val userId = call.request.queryParameters["user_id"].toString()

                val user = db.from(EntityUser)
                    .select()
                    .where {
                        EntityUser.user_id eq userId
                    }
                    .map {
                        User(
                            user_id = it[EntityUser.user_id]!!,
                            user_name = it[EntityUser.user_name],
                            user_image = it[EntityUser.user_image],
                            email = it[EntityUser.email],
                            birthday = it[EntityUser.birthday],
                            phone_number = it[EntityUser.phone_number],
                            address = it[EntityUser.address],
                            last_active = it[EntityUser.last_active],
                            account_type_id = it[EntityUser.account_type_id],
                            access_id = it[EntityUser.access_id] ?: 0
                        )
                    }
                    .firstOrNull()

                if (user != null)
                    call.respond(
                        user
                    )
                else
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
            }

            post("/register")
            {
                val userReq: User = call.receive()
                val userId = userReq.user_id

                val userCheck = db.from(EntityUser)
                    .select()
                    .where(EntityUser.user_id eq userId)
                    .map {
                        User(
                            user_id = it[EntityUser.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            email = it[EntityUser.email].orEmpty(),
                            birthday = it[EntityUser.birthday].orEmpty(),
                            phone_number = it[EntityUser.phone_number].orEmpty(),
                            address = it[EntityUser.address].orEmpty(),
                            last_active = it[EntityUser.last_active].orEmpty(),
                            account_type_id = it[EntityUser.account_type_id] ?: 0,
                            access_id = it[EntityUser.access_id] ?: 0
                        )
                    }

                if (userCheck.size == 0) {
                    val noOfRowsAffected = db.insert(EntityUser)
                    {
                        set(it.user_id, userReq.user_id)
                        set(it.user_name, userReq.user_name)
                        set(it.user_image, userReq.user_image)
                        set(it.email, userReq.email)
                        set(it.birthday, userReq.birthday)
                        set(it.phone_number, userReq.phone_number)
                        set(it.address, userReq.address)
                        set(it.last_active, userReq.last_active)
                        set(it.account_type_id, userReq.account_type_id)
                    }
                    val user = db.from(EntityUser)
                        .select()
                        .where {
                            EntityUser.user_id eq userId
                        }
                        .map {
                            User(
                                user_id = it[EntityUser.user_id]!!,
                                user_name = it[EntityUser.user_name],
                                user_image = it[EntityUser.user_image],
                                email = it[EntityUser.email],
                                birthday = it[EntityUser.birthday],
                                phone_number = it[EntityUser.phone_number],
                                address = it[EntityUser.address],
                                last_active = it[EntityUser.last_active],
                                account_type_id = it[EntityUser.account_type_id],
                                access_id = it[EntityUser.access_id] ?: 0
                            )
                        }
                    if (noOfRowsAffected > 0) {
                        call.respond(user)
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            GenericResponse(isSuccess = false, data = "Error to register the user")
                        )
                    }
                } else {
                    call.respond(userCheck[0])
                }
            }

            put("/update")
            {
                val user: User = call.receive()

                val noOfRowsAffected = db.update(EntityUser)
                {
                    set(it.user_name, user.user_name)
                    set(it.user_image, user.user_image)
                    set(it.email, user.email)
                    set(it.birthday, user.birthday)
                    set(it.phone_number, user.phone_number)
                    set(it.address, user.address)
                    set(it.last_active, user.last_active)
                    set(it.account_type_id, user.account_type_id)
                    set(it.access_id, user.access_id)
                    where {
                        it.user_id eq user.user_id
                    }
                }

                val userInfo = db.from(EntityUser)
                    .select()
                    .where {
                        EntityUser.user_id eq user.user_id
                    }
                    .map {
                        User(
                            user_id = it[EntityUser.user_id]!!,
                            user_name = it[EntityUser.user_name],
                            user_image = it[EntityUser.user_image],
                            email = it[EntityUser.email],
                            birthday = it[EntityUser.birthday],
                            phone_number = it[EntityUser.phone_number],
                            address = it[EntityUser.address],
                            last_active = it[EntityUser.last_active],
                            account_type_id = it[EntityUser.account_type_id],
                            access_id = it[EntityUser.access_id] ?: 0
                        )
                    }
                    .firstOrNull()

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        user
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = "Error to update the user")
                    )
                }
            }


            delete("/delete")
            {
                val userIdStr = call.request.queryParameters["user_id"].toString()
//            val userIdInt = userIdStr?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityUser)
                {
                    it.user_id eq userIdStr
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = "$noOfRowsAffected rows are affected")
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = "Error to delete the user")
                    )
                }
            }
        }
    }
}