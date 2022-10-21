package com.example.route

import com.example.models.Reply
import com.example.models.ReplyRequest
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityReply
import com.example.mysql.entity.EntityUser
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeReply() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/reply") {
            get("/list") {
                val commentId = call.request.queryParameters["comment_id"]
                val commentIdInt = commentId?.toInt() ?: -1
                val list = db.from(EntityReply).innerJoin(EntityUser)
                    .select()
                    .where(
                        EntityReply.comment_id eq commentIdInt
                                and (EntityReply.user_id eq EntityUser.user_id)
                    )
                    .map {
                        Reply(
                            reply_id = it[EntityReply.reply_id] ?: 0,
                            comment_id = it[EntityReply.comment_id] ?: 0,
                            user_id = it[EntityReply.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityReply.content].orEmpty(),
                            date_submitted = it[EntityReply.date_submitted].orEmpty(),
                            like_count = it[EntityReply.like_count] ?: 0,
                            dislike_count = it[EntityReply.dislike_count] ?: 0,
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
                val reply: ReplyRequest = call.receive()
                val noOfRowsAffected = db.insert(EntityReply)
                {
                    set(it.reply_id, null)
                    set(it.comment_id, reply.comment_id)
                    set(it.user_id, reply.user_id)
                    set(it.content, reply.content)
                    set(it.date_submitted, reply.date_submitted)
                    set(it.like_count, 0)
                    set(it.dislike_count, 0)
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
                        GenericResponse(isSuccess = false, data = "Error to post reply")
                    )
                }
            }

            put("/update/{reply_id}") {
                val replyId = call.parameters["reply_id"].toString()
                val replyIdInt = replyId.toInt()

                val reply: Reply = call.receive()

                val noOfRowsAffected = db.update(EntityReply)
                {
                    set(it.comment_id, reply.comment_id)
                    set(it.user_id, reply.user_id)
                    set(it.content, reply.content)
                    set(it.date_submitted, reply.date_submitted)
                    set(it.like_count, 0)
                    set(it.dislike_count, 0)
                    where {
                        it.reply_id eq replyIdInt
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

            put("/update/like") {
                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt() ?: -1

                val reply = db.from(EntityReply)
                    .select()
                    .where { EntityReply.reply_id eq replyIdInt }
                    .map {
                        Reply(
                            reply_id = it[EntityReply.reply_id] ?: 0,
                            comment_id = it[EntityReply.comment_id] ?: 0,
                            user_id = it[EntityUser.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityReply.content].orEmpty(),
                            date_submitted = it[EntityReply.date_submitted].orEmpty(),
                            like_count = it[EntityReply.like_count] ?: 0,
                            dislike_count = it[EntityReply.dislike_count] ?: 0,
                        )
                    }.first()

                val likeCount = reply.like_count!! + 1

                val noOfRowsAffected = db.update(EntityReply)
                {
                    set(it.like_count, likeCount)
                    where {
                        it.reply_id eq replyIdInt
                    }
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = likeCount)
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = -1)
                    )
                }

            }

            put("/update/dislike") {
                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt() ?: -1

                val reply = db.from(EntityReply)
                    .select()
                    .where { EntityReply.reply_id eq replyIdInt }
                    .map {
                        Reply(
                            reply_id = it[EntityReply.reply_id] ?: 0,
                            comment_id = it[EntityReply.comment_id] ?: 0,
                            user_id = it[EntityUser.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityReply.content].orEmpty(),
                            date_submitted = it[EntityReply.date_submitted].orEmpty(),
                            like_count = it[EntityReply.like_count] ?: 0,
                            dislike_count = it[EntityReply.dislike_count] ?: 0,
                        )
                    }.first()

                val dislikeCount = reply.dislike_count!! + 1

                val noOfRowsAffected = db.update(EntityReply)
                {
                    set(it.dislike_count, dislikeCount)
                    where {
                        it.reply_id eq replyIdInt
                    }
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = dislikeCount)
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = -1)
                    )
                }

            }

            put("/update/like/cancel") {
                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt() ?: -1

                val reply = db.from(EntityReply)
                    .select()
                    .where { EntityReply.reply_id eq replyIdInt }
                    .map {
                        Reply(
                            reply_id = it[EntityReply.reply_id] ?: 0,
                            comment_id = it[EntityReply.comment_id] ?: 0,
                            user_id = it[EntityUser.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityReply.content].orEmpty(),
                            date_submitted = it[EntityReply.date_submitted].orEmpty(),
                            like_count = it[EntityReply.like_count] ?: 0,
                            dislike_count = it[EntityReply.dislike_count] ?: 0,
                        )
                    }.first()

                val likeCount = reply.like_count!! - 1

                val noOfRowsAffected = db.update(EntityReply)
                {
                    set(it.like_count, likeCount)
                    where {
                        it.reply_id eq replyIdInt
                    }
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = likeCount)
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = -1)
                    )
                }

            }

            put("/update/dislike/cancel") {
                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt() ?: -1

                val reply = db.from(EntityReply)
                    .select()
                    .where { EntityReply.reply_id eq replyIdInt }
                    .map {
                        Reply(
                            reply_id = it[EntityReply.reply_id] ?: 0,
                            comment_id = it[EntityReply.comment_id] ?: 0,
                            user_id = it[EntityUser.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityReply.content].orEmpty(),
                            date_submitted = it[EntityReply.date_submitted].orEmpty(),
                            like_count = it[EntityReply.like_count] ?: 0,
                            dislike_count = it[EntityReply.dislike_count] ?: 0,
                        )
                    }.first()

                val dislikeCount = reply.dislike_count!! - 1

                val noOfRowsAffected = db.update(EntityReply)
                {
                    set(it.dislike_count, dislikeCount)
                    where {
                        it.reply_id eq replyIdInt
                    }
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = dislikeCount)
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = -1)
                    )
                }

            }

            put("/update/content") {
                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt() ?: -1

                val content = call.request.queryParameters["content"].toString()

                val noOfRowsAffected = db.update(EntityReply)
                {
                    set(it.content, content)
                    where {
                        it.reply_id eq replyIdInt
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

            delete("/delete")
            {
                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityReply)
                {
                    it.reply_id eq replyIdInt
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