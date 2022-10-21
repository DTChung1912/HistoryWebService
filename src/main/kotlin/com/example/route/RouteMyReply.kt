package com.example.route

import com.example.models.MyReplyStatus
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityMyReply
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeMyReply() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/myreply") {

            get("/list") {
                val userId = call.request.queryParameters["user_id"].toString()

                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt() ?: -1

                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val myList = db.from(EntityMyReply)
                    .select()
                    .where(
                        EntityMyReply.user_id eq userId
                                and (EntityMyReply.video_id eq videoIdInt)
                                and (EntityMyReply.comment_id eq commentIdInt)
                    )
                    .map {
                        MyReplyStatus(
                            my_reply_id = it[EntityMyReply.my_reply_id] ?: 0,
                            user_id = it[EntityMyReply.user_id].orEmpty(),
                            video_id = it[EntityMyReply.video_id] ?: 0,
                            comment_id = it[EntityMyReply.comment_id] ?: 0,
                            reply_id = it[EntityMyReply.reply_id] ?: 0,
                            isLike = it[EntityMyReply.isLike] ?: 0
                        )

                    }

                if (myList.isNotEmpty()) {
                    call.respond(myList)
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
                }
            }

            get("/detail") {
                val userId = call.request.queryParameters["user_id"].toString()

                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt() ?: -1

                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt() ?: -1

                var myReplyId = 0
                val check = db.from(EntityMyReply)
                    .select(EntityMyReply.my_reply_id)
                    .where(
                        EntityMyReply.user_id eq userId
                                and (EntityMyReply.video_id eq videoIdInt)
                                and (EntityMyReply.comment_id eq commentIdInt)
                                and (EntityMyReply.reply_id eq replyIdInt)
                    )
                    .map {
                        myReplyId = it[EntityMyReply.my_reply_id] ?: 0
                    }

                if (!check.isNullOrEmpty()) {
                    val myReply = db.from(EntityMyReply)
                        .select()
                        .where(
                            EntityMyReply.user_id eq userId
                                    and (EntityMyReply.video_id eq videoIdInt)
                                    and (EntityMyReply.comment_id eq commentIdInt)
                                    and (EntityMyReply.reply_id eq replyIdInt)
                        )
                        .map {
                            MyReplyStatus(
                                my_reply_id = it[EntityMyReply.my_reply_id] ?: 0,
                                user_id = it[EntityMyReply.user_id].orEmpty(),
                                video_id = it[EntityMyReply.video_id] ?: 0,
                                comment_id = it[EntityMyReply.comment_id] ?: 0,
                                reply_id = it[EntityMyReply.reply_id] ?: 0,
                                isLike = it[EntityMyReply.isLike] ?: 0
                            )
                        }.single()

                    if (myReply.my_reply_id!! > 0) {
                        call.respond(myReply)
                    } else {
                        call.respond(
                            HttpStatusCode.OK,
                            GenericResponse(isSuccess = false, data = null)
                        )
                    }
                } else {
                    val noOfRowsAffected = db.insert(EntityMyReply)
                    {
                        set(it.my_reply_id, null)
                        set(it.user_id, userId)
                        set(it.video_id, videoIdInt)
                        set(it.comment_id, commentIdInt)
                        set(it.reply_id, replyIdInt)
                        set(it.isLike, 0)
                    }

                    if (noOfRowsAffected > 0) {
                        val myReply = db.from(EntityMyReply)
                            .select()
                            .where(
                                EntityMyReply.user_id eq userId
                                        and (EntityMyReply.video_id eq videoIdInt)
                                        and (EntityMyReply.comment_id eq commentIdInt)
                                        and (EntityMyReply.reply_id eq replyIdInt)
                            )
                            .map {
                                MyReplyStatus(
                                    my_reply_id = it[EntityMyReply.my_reply_id] ?: 0,
                                    user_id = it[EntityMyReply.user_id].orEmpty(),
                                    video_id = it[EntityMyReply.video_id] ?: 0,
                                    comment_id = it[EntityMyReply.comment_id] ?: 0,
                                    reply_id = it[EntityMyReply.reply_id] ?: 0,
                                    isLike = it[EntityMyReply.isLike] ?: 0
                                )
                            }.single()

                        if (myReply.my_reply_id!! > 0) {
                            call.respond(myReply)
                        } else {
                            call.respond(
                                HttpStatusCode.OK,
                                GenericResponse(isSuccess = false, data = null)
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.OK,
                            GenericResponse(isSuccess = false, data = null)
                        )
                    }
                }
            }

            post("/create") {
                val myReply: MyReplyStatus = call.receive()
                val noOfRowsAffected = db.insert(EntityMyReply)
                {
                    set(it.my_reply_id, null)
                    set(it.user_id, myReply.user_id)
                    set(it.video_id, myReply.video_id)
                    set(it.comment_id, myReply.comment_id)
                    set(it.reply_id, myReply.reply_id)
                    set(it.isLike, myReply.isLike)
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
                        GenericResponse(isSuccess = false, data = "Error to register the my video")
                    )
                }
            }

            put("/update/like") {
                val userId = call.request.queryParameters["user_id"].toString()

                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt() ?: -1

                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt() ?: -1

                val replyId = call.request.queryParameters["reply_id"].toString()
                val replyIdInt = replyId.toInt()

                val isLikeStr = call.request.queryParameters["is_like"].toString()
                val isLikeInt = isLikeStr.toInt()

                var myReplyId = 0
                val check = db.from(EntityMyReply)
                    .select(EntityMyReply.my_reply_id)
                    .where(
                        EntityMyReply.user_id eq userId
                                and (EntityMyReply.video_id eq videoIdInt)
                                and (EntityMyReply.comment_id eq commentIdInt)
                                and (EntityMyReply.reply_id eq replyIdInt)
                    )
                    .map {
                        myReplyId = it[EntityMyReply.my_reply_id] ?: 0
                    }

                if (!check.isNullOrEmpty()) {
                    val noOfRowsAffected = db.update(EntityMyReply)
                    {
                        set(it.isLike, isLikeInt)
                        where {
                            it.user_id eq userId and (it.video_id eq videoIdInt and (it.comment_id eq commentIdInt) and (it.reply_id eq replyIdInt))
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
                            GenericResponse(isSuccess = false, data = "Error to update the my video")
                        )
                    }
                } else {
                    val create = db.insert(EntityMyReply)
                    {
                        set(it.my_reply_id, null)
                        set(it.user_id, userId)
                        set(it.video_id, videoIdInt)
                        set(it.comment_id, commentIdInt)
                        set(it.reply_id, replyIdInt)
                        set(it.isLike, 0)
                    }

                    if (create > 0) {
                        val myReply = db.from(EntityMyReply)
                            .select()
                            .where(
                                EntityMyReply.user_id eq userId
                                        and (EntityMyReply.video_id eq videoIdInt)
                                        and (EntityMyReply.comment_id eq commentIdInt)
                                        and (EntityMyReply.reply_id eq replyIdInt)
                            )
                            .map {
                                MyReplyStatus(
                                    my_reply_id = it[EntityMyReply.my_reply_id] ?: 0,
                                    user_id = it[EntityMyReply.user_id].orEmpty(),
                                    video_id = it[EntityMyReply.video_id] ?: 0,
                                    comment_id = it[EntityMyReply.comment_id] ?: 0,
                                    reply_id = it[EntityMyReply.reply_id] ?: 0,
                                    isLike = it[EntityMyReply.isLike] ?: 0
                                )
                            }.single()

                        if (myReply.my_reply_id!! > 0) {
                            call.respond(myReply)
                        } else {
                            call.respond(
                                HttpStatusCode.OK,
                                GenericResponse(isSuccess = false, data = null)
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.OK,
                            GenericResponse(isSuccess = false, data = null)
                        )
                    }
                }
            }

            delete("/delete") {
                val myReplyId = call.request.queryParameters["my_reply_id"].toString()
                val myReplyIdInt = myReplyId?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityMyReply)
                {
                    it.my_reply_id eq myReplyIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to delete the my video")
                    )
                }
            }
        }
    }
}