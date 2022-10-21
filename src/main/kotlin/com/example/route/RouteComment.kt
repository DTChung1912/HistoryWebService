package com.example.route

import com.example.models.Comment
import com.example.models.CommentRequest
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityComment
import com.example.mysql.entity.EntityUser
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeComment() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/comment") {
            get("/list") {
//                val formParameters = call.receiveParameters()
                val videoId = call.request.queryParameters["video_id"]
                val videoIdInt = videoId?.toInt() ?: -1
                val list = db.from(EntityComment).innerJoin(EntityUser)
                    .select()
                    .where(EntityComment.video_id eq videoIdInt and (EntityComment.user_id eq EntityUser.user_id))
                    .orderBy(EntityComment.comment_id.desc())
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
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
                val comment: CommentRequest = call.receive()
                val noOfRowsAffected = db.insert(EntityComment)
                {
                    set(it.comment_id, null)
                    set(it.video_id, comment.video_id)
                    set(it.user_id, comment.user_id)
                    set(it.content, comment.content)
                    set(it.date_submitted, comment.date_submitted)
                    set(it.reply_count, 0)
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
                        GenericResponse(isSuccess = false, data = "Error to register the comment")
                    )
                }
            }

            put("/update/{comment_id}") {
                val commentId = call.parameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment: Comment = call.receive()

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.video_id, comment.video_id)
                    set(it.user_id, comment.user_id)
                    set(it.content, comment.content)
                    set(it.date_submitted, comment.date_submitted)
                    set(it.reply_count, comment.reply_count)
                    set(it.like_count, comment.like_count)
                    set(it.dislike_count, comment.dislike_count)
                    where {
                        it.comment_id eq commentIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to update the comment")
                    )
                }
            }

            put("/update/like") {
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment = db.from(EntityComment)
                    .select()
                    .where(EntityComment.comment_id eq commentIdInt)
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
                        )
                    }.single()

                val likeCount = comment.like_count!! + 1

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.like_count, likeCount)

                    where {
                        it.comment_id eq commentIdInt
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
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment = db.from(EntityComment)
                    .select()
                    .where(EntityComment.comment_id eq commentIdInt)
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
                        )
                    }.single()

                val dislikeCount = comment.dislike_count!! + 1

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.dislike_count, dislikeCount)

                    where {
                        it.comment_id eq commentIdInt
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
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment = db.from(EntityComment)
                    .select()
                    .where(EntityComment.comment_id eq commentIdInt)
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
                        )
                    }.single()

                val likeCount = comment.like_count!! - 1

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.like_count, likeCount)

                    where {
                        it.comment_id eq commentIdInt
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
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment = db.from(EntityComment)
                    .select()
                    .where(EntityComment.comment_id eq commentIdInt)
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
                        )
                    }.single()

                val dislikeCount = comment.dislike_count!! - 1

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.dislike_count, dislikeCount)

                    where {
                        it.comment_id eq commentIdInt
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

            put("/update/reply") {
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment = db.from(EntityComment)
                    .select()
                    .where(EntityComment.comment_id eq commentIdInt)
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
                        )
                    }.single()

                val replyCount = comment.reply_count!! + 1

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.reply_count, replyCount)

                    where {
                        it.comment_id eq commentIdInt
                    }
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = replyCount)
                    )
                } else {
                    //fail
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenericResponse(isSuccess = false, data = -1)
                    )
                }
            }

            put("/update/reply/cancel") {
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val comment = db.from(EntityComment)
                    .select()
                    .where(EntityComment.comment_id eq commentIdInt)
                    .map {
                        Comment(
                            comment_id = it[EntityComment.comment_id] ?: 0,
                            video_id = it[EntityComment.video_id] ?: 0,
                            user_id = it[EntityComment.user_id].orEmpty(),
                            user_name = it[EntityUser.user_name].orEmpty(),
                            user_image = it[EntityUser.user_image].orEmpty(),
                            content = it[EntityComment.content].orEmpty(),
                            date_submitted = it[EntityComment.date_submitted],
                            reply_count = it[EntityComment.reply_count] ?: 0,
                            like_count = it[EntityComment.like_count] ?: 0,
                            dislike_count = it[EntityComment.dislike_count] ?: 0
                        )
                    }.single()

                val replyCount = comment.reply_count!! - 1

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.reply_count, replyCount)

                    where {
                        it.comment_id eq commentIdInt
                    }
                }

                if (noOfRowsAffected > 0) {
                    //success
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = true, data = replyCount)
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
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val content = call.request.queryParameters["content"].toString()

                val noOfRowsAffected = db.update(EntityComment)
                {
                    set(it.content, content)
                    where {
                        it.comment_id eq commentIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to update the comment")
                    )
                }
            }

            delete("/delete")
            {
                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityComment)
                {
                    it.comment_id eq commentIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to delete the comment")
                    )
                }
            }
        }
    }
}