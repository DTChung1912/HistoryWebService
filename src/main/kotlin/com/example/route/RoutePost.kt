package com.example.route

import com.example.models.Post
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityPost
import com.example.mysql.entity.EntityPostType
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routePost() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/post") {
            get("/list") {
                val list = db.from(EntityPost).innerJoin(EntityPostType)
                    .select()
                    .where(EntityPost.post_type_id eq EntityPostType.post_type_id)
                    .map {
                        Post(
                            post_id = it[EntityPost.post_id] ?: 0,
                            post_type_id = it[EntityPost.post_type_id],
                            post_type_name = it[EntityPostType.post_type_name].orEmpty(),
                            theme_id = it[EntityPost.theme_id] ?: 0,
                            title = it[EntityPost.title].orEmpty(),
                            content = it[EntityPost.content].orEmpty(),
                            pdf = it[EntityPost.pdf].orEmpty(),
                            image = it[EntityPost.image].orEmpty(),
                            description = it[EntityPost.description].orEmpty(),
                            timeline = it[EntityPost.timeline].orEmpty(),
                            place = it[EntityPost.place].orEmpty(),
                            read_count = it[EntityPost.read_count] ?: 0,
                            download_count = it[EntityPost.download_count] ?: 0,
                            rate_count = it[EntityPost.rate_count] ?: 0,
                            date_submitted = it[EntityPost.date_submitted].orEmpty()
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

            get("/detail") {

                val postIntStr = call.request.queryParameters["post_id"].toString()
                val postInInt = postIntStr.toInt() ?: 0

                val post = db.from(EntityPost).innerJoin(EntityPostType)
                    .select()
                    .where(
                        EntityPost.post_id eq postInInt
                                and (EntityPost.post_type_id eq EntityPostType.post_type_id)
                    )
                    .map {
                        Post(
                            post_id = it[EntityPost.post_id] ?: 0,
                            post_type_id = it[EntityPost.post_type_id],
                            post_type_name = it[EntityPostType.post_type_name].orEmpty(),
                            theme_id = it[EntityPost.theme_id] ?: 0,
                            title = it[EntityPost.title].orEmpty(),
                            content = it[EntityPost.content].orEmpty(),
                            pdf = it[EntityPost.pdf].orEmpty(),
                            image = it[EntityPost.image].orEmpty(),
                            description = it[EntityPost.description].orEmpty(),
                            timeline = it[EntityPost.timeline].orEmpty(),
                            place = it[EntityPost.place].orEmpty(),
                            read_count = it[EntityPost.read_count] ?: 0,
                            download_count = it[EntityPost.download_count] ?: 0,
                            rate_count = it[EntityPost.rate_count] ?: 0,
                            date_submitted = it[EntityPost.date_submitted].orEmpty()
                        )
                    }

                if (post.isNotEmpty()) {
                    call.respond(post)
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
                }
            }

            get("/search") {
//                val formParameters = call.receiveParameters()
//                val keyword = formParameters["keyword"]

                val keyword = call.request.queryParameters["keyword"].toString()

                val list = db.from(EntityPost).innerJoin(EntityPostType)
                    .select()
                    .where(EntityPost.post_type_id eq EntityPostType.post_type_id and (EntityPost.title.like("%$keyword%")))
                    .map {
                        Post(
                            post_id = it[EntityPost.post_id] ?: 0,
                            post_type_id = it[EntityPost.post_type_id],
                            post_type_name = it[EntityPostType.post_type_name].orEmpty(),
                            theme_id = it[EntityPost.theme_id] ?: 0,
                            title = it[EntityPost.title].orEmpty(),
                            content = it[EntityPost.content].orEmpty(),
                            pdf = it[EntityPost.pdf].orEmpty(),
                            image = it[EntityPost.image].orEmpty(),
                            description = it[EntityPost.description].orEmpty(),
                            timeline = it[EntityPost.timeline].orEmpty(),
                            place = it[EntityPost.place].orEmpty(),
                            read_count = it[EntityPost.read_count] ?: 0,
                            download_count = it[EntityPost.download_count] ?: 0,
                            rate_count = it[EntityPost.rate_count] ?: 0,
                            date_submitted = it[EntityPost.date_submitted].orEmpty()
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
                val post: Post = call.receive()
                val noOfRowsAffected = db.insert(EntityPost)
                {
                    set(it.post_id, null)
                    set(it.post_type_id, post.post_type_id)
                    set(it.theme_id, post.theme_id)
                    set(it.title, post.title)
                    set(it.content, post.content)
                    set(it.pdf, post.pdf)
                    set(it.image, post.image)
                    set(it.description, post.description)
                    set(it.timeline, post.timeline)
                    set(it.place, post.place)
                    set(it.read_count, 0)
                    set(it.download_count, 0)
                    set(it.rate_count, 0)
                    set(it.date_submitted, post.date_submitted)
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
                        GenericResponse(isSuccess = false, data = "Error to register the post")
                    )
                }
            }

            put("/update")
            {
//                val postIdStr = call.parameters["post_id"].toString()
//                val postIdInt = postIdStr?.toInt() ?: -1
                val post: Post = call.receive()

                val noOfRowsAffected = db.update(EntityPost)
                {
                    set(it.post_type_id, post.post_type_id)
                    set(it.theme_id, post.theme_id)
                    set(it.title, post.title)
                    set(it.content, post.content)
                    set(it.pdf, post.pdf)
                    set(it.image, post.image)
                    set(it.description, post.description)
                    set(it.timeline, post.timeline)
                    set(it.place, post.place)
                    set(it.read_count, 0)
                    set(it.download_count, 0)
                    set(it.rate_count, 0)
                    set(it.date_submitted, post.date_submitted)
                    where {
                        it.post_id eq post.post_id!!
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
                        GenericResponse(isSuccess = false, data = "Error to update the post")
                    )
                }
            }

            put("/update/read")
            {
                val postIdStr = call.request.queryParameters["post_id"].toString()
                val postIdInt = postIdStr?.toInt() ?: -1

                val post = db.from(EntityPost)
                    .select()
                    .where(EntityPost.post_id eq postIdInt)
                    .map {
                        Post(
                            post_id = it[EntityPost.post_id] ?: 0,
                            post_type_id = it[EntityPost.post_type_id],
                            post_type_name = it[EntityPostType.post_type_name].orEmpty(),
                            theme_id = it[EntityPost.theme_id] ?: 0,
                            title = it[EntityPost.title].orEmpty(),
                            content = it[EntityPost.content].orEmpty(),
                            pdf = it[EntityPost.pdf].orEmpty(),
                            image = it[EntityPost.image].orEmpty(),
                            description = it[EntityPost.description].orEmpty(),
                            timeline = it[EntityPost.timeline].orEmpty(),
                            place = it[EntityPost.place].orEmpty(),
                            read_count = it[EntityPost.read_count] ?: 0,
                            download_count = it[EntityPost.download_count] ?: 0,
                            rate_count = it[EntityPost.rate_count] ?: 0,
                            date_submitted = it[EntityPost.date_submitted].orEmpty()
                        )
                    }.first()

                val readCount = post.read_count!! + 1

                val noOfRowsAffected = db.update(EntityPost)
                {
                    set(it.read_count, readCount)
                    where {
                        it.post_id eq postIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to update the post")
                    )
                }
            }

            put("/update/download")
            {
                val postIdStr = call.request.queryParameters["post_id"].toString()
                val postIdInt = postIdStr?.toInt() ?: -1

                val post = db.from(EntityPost)
                    .select()
                    .where(EntityPost.post_id eq postIdInt)
                    .map {
                        Post(
                            post_id = it[EntityPost.post_id] ?: 0,
                            post_type_id = it[EntityPost.post_type_id],
                            post_type_name = it[EntityPostType.post_type_name].orEmpty(),
                            theme_id = it[EntityPost.theme_id] ?: 0,
                            title = it[EntityPost.title].orEmpty(),
                            content = it[EntityPost.content].orEmpty(),
                            pdf = it[EntityPost.pdf].orEmpty(),
                            image = it[EntityPost.image].orEmpty(),
                            description = it[EntityPost.description].orEmpty(),
                            timeline = it[EntityPost.timeline].orEmpty(),
                            place = it[EntityPost.place].orEmpty(),
                            read_count = it[EntityPost.read_count] ?: 0,
                            download_count = it[EntityPost.download_count] ?: 0,
                            rate_count = it[EntityPost.rate_count] ?: 0,
                            date_submitted = it[EntityPost.date_submitted].orEmpty()
                        )
                    }.first()

                val downloadCount = post.download_count!! + 1

                val noOfRowsAffected = db.update(EntityPost)
                {
                    set(it.download_count, downloadCount)
                    where {
                        it.post_id eq postIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to update the post")
                    )
                }
            }

            put("/update/rate")
            {
                val postIdStr = call.request.queryParameters["post_id"].toString()
                val postIdInt = postIdStr?.toInt() ?: -1

                val post = db.from(EntityPost)
                    .select()
                    .where(EntityPost.post_id eq postIdInt)
                    .map {
                        Post(
                            post_id = it[EntityPost.post_id] ?: 0,
                            post_type_id = it[EntityPost.post_type_id],
                            post_type_name = it[EntityPostType.post_type_name].orEmpty(),
                            theme_id = it[EntityPost.theme_id] ?: 0,
                            title = it[EntityPost.title].orEmpty(),
                            content = it[EntityPost.content].orEmpty(),
                            pdf = it[EntityPost.pdf].orEmpty(),
                            image = it[EntityPost.image].orEmpty(),
                            description = it[EntityPost.description].orEmpty(),
                            timeline = it[EntityPost.timeline].orEmpty(),
                            place = it[EntityPost.place].orEmpty(),
                            read_count = it[EntityPost.read_count] ?: 0,
                            download_count = it[EntityPost.download_count] ?: 0,
                            rate_count = it[EntityPost.rate_count] ?: 0,
                            date_submitted = it[EntityPost.date_submitted].orEmpty()
                        )
                    }.first()

                val rateCount = post.rate_count!! + 1

                val noOfRowsAffected = db.update(EntityPost)
                {
                    set(it.rate_count, rateCount)
                    where {
                        it.post_id eq postIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to update the post")
                    )
                }
            }

            delete("/delete")
            {
                val postIdStr = call.request.queryParameters["post_id"].toString()
                val postIdInt = postIdStr?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityPost)
                {
                    it.post_id eq postIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to delete the post")
                    )
                }
            }
        }
    }
}