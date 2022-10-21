package com.example.route

import com.example.models.Theme
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityTheme
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeTheme() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/theme") {
            get("/list") {
                val list = db.from(EntityTheme)
                    .select()
                    .map {
                        Theme(
                            theme_id = it[EntityTheme.theme_id] ?: 0,
                            theme_name = it[EntityTheme.theme_name].orEmpty(),
                            theme_image = it[EntityTheme.theme_image].orEmpty()
                        )
                    }

                if (list.isNotEmpty()) {
                    call.respond(list)
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
                }
            }

            post("/create") {
                val theme: Theme = call.receive()
                val noOfRowsAffected = db.insert(EntityTheme)
                {
                    set(it.theme_id, null)
                    set(it.theme_name, theme.theme_name)
                    set(it.theme_image, theme.theme_image)
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
                        GenericResponse(isSuccess = false, data = "Error to register the user")
                    )
                }
            }

            put("/update") {
//                val themeId = call.parameters["theme_id"].toString()
//                val themeIdInt = themeId.toInt()

                val theme: Theme = call.receive()

                val noOfRowsAffected = db.update(EntityTheme)
                {
                    set(it.theme_name, theme.theme_name)
                    set(it.theme_image, theme.theme_image)
                    where {
                        it.theme_id eq theme.theme_id!!
                    }
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
                        GenericResponse(isSuccess = false, data = "Error to update the video")
                    )
                }

            }

            delete("/delete/{theme_id}")
            {
                val themeId = call.parameters["theme_id"].toString()
                val themeIdInt = themeId?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityTheme)
                {
                    it.theme_id eq themeIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to delete the video")
                    )
                }
            }
        }
    }
}