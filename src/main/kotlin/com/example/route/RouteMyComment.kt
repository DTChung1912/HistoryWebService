package com.example.route

import com.example.models.MyCommentStatus
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityMyComment
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeMyComment() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/mycomment") {

            get("/list") {
                val userId = call.request.queryParameters["user_id"].toString()

                val videoId = call.request.queryParameters["video_id"].toString()

                val videoIdInt = videoId.toInt() ?: -1
                val myList = db.from(EntityMyComment)
                    .select()
                    .where(
                        EntityMyComment.user_id eq userId
                                and (EntityMyComment.video_id eq videoIdInt)
                    )
                    .map {
                        MyCommentStatus(
                            my_comment_id = it[EntityMyComment.my_comment_id] ?: 0,
                            user_id = it[EntityMyComment.user_id].orEmpty(),
                            video_id = it[EntityMyComment.video_id] ?: 0,
                            comment_id = it[EntityMyComment.comment_id] ?: 0,
                            isLike = it[EntityMyComment.isLike] ?: 0
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

                var myCommentId = 0
                val check = db.from(EntityMyComment)
                    .select(EntityMyComment.my_comment_id)
                    .where(
                        EntityMyComment.user_id eq userId
                                and (EntityMyComment.video_id eq videoIdInt)
                                and (EntityMyComment.comment_id eq commentIdInt)
                    )
                    .map {
                        myCommentId = it[EntityMyComment.my_comment_id] ?: 0
                    }

                if (!check.isNullOrEmpty()) {
                    val myComment = db.from(EntityMyComment)
                        .select()
                        .where(
                            EntityMyComment.user_id eq userId
                                    and (EntityMyComment.video_id eq videoIdInt)
                                    and (EntityMyComment.comment_id eq commentIdInt)
                        )
                        .map {
                            MyCommentStatus(
                                my_comment_id = it[EntityMyComment.my_comment_id] ?: 0,
                                user_id = it[EntityMyComment.user_id].orEmpty(),
                                video_id = it[EntityMyComment.video_id] ?: 0,
                                comment_id = it[EntityMyComment.comment_id] ?: 0,
                                isLike = it[EntityMyComment.isLike] ?: 0
                            )
                        }.single()

                    if (myComment.my_comment_id!! > 0) {
                        call.respond(myComment)
                    } else {
                        call.respond(
                            HttpStatusCode.OK,
                            GenericResponse(isSuccess = false, data = null)
                        )
                    }
                } else {
                    val noOfRowsAffected = db.insert(EntityMyComment)
                    {
                        set(it.my_comment_id, null)
                        set(it.user_id, userId)
                        set(it.video_id, videoIdInt)
                        set(it.comment_id, commentIdInt)
                        set(it.isLike, 0)
                    }

                    if (noOfRowsAffected > 0) {
                        val myComment = db.from(EntityMyComment)
                            .select()
                            .where(
                                EntityMyComment.user_id eq userId
                                        and (EntityMyComment.video_id eq videoIdInt)
                                        and (EntityMyComment.comment_id eq commentIdInt)
                            )
                            .map {
                                MyCommentStatus(
                                    my_comment_id = it[EntityMyComment.my_comment_id] ?: 0,
                                    user_id = it[EntityMyComment.user_id].orEmpty(),
                                    video_id = it[EntityMyComment.video_id] ?: 0,
                                    comment_id = it[EntityMyComment.comment_id] ?: 0,
                                    isLike = it[EntityMyComment.isLike] ?: 0
                                )
                            }.single()

                        if (myComment.my_comment_id!! > 0) {
                            call.respond(myComment)
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
                val myComment: MyCommentStatus = call.receive()
                val noOfRowsAffected = db.insert(EntityMyComment)
                {
                    set(it.my_comment_id, null)
                    set(it.user_id, myComment.user_id)
                    set(it.video_id, myComment.video_id)
                    set(it.comment_id, myComment.comment_id)
                    set(it.isLike, myComment.isLike)
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
                        GenericResponse(isSuccess = false, data = "Error to register ")
                    )
                }
            }

            put("/update/like") {
                val userId = call.request.queryParameters["user_id"].toString()

                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt() ?: -1

                val commentId = call.request.queryParameters["comment_id"].toString()
                val commentIdInt = commentId.toInt()

                val isLikeStr = call.request.queryParameters["is_like"].toString()
                val isLikeInt = isLikeStr.toInt()

                var myCommentId = 0
                val check = db.from(EntityMyComment)
                    .select(EntityMyComment.my_comment_id)
                    .where(
                        EntityMyComment.user_id eq userId
                                and (EntityMyComment.video_id eq videoIdInt)
                                and (EntityMyComment.comment_id eq commentIdInt)
                    )
                    .map {
                        myCommentId = it[EntityMyComment.my_comment_id] ?: 0
                    }

                if (!check.isNullOrEmpty()) {
                    val noOfRowsAffected = db.update(EntityMyComment)
                    {
                        set(it.isLike, isLikeInt)
                        where {
                            it.user_id eq userId and (it.video_id eq videoIdInt and (it.comment_id eq commentIdInt))
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
                            GenericResponse(isSuccess = false, data = "Error to update the ")
                        )
                    }
                } else {
                    val create = db.insert(EntityMyComment)
                    {
                        set(it.my_comment_id, null)
                        set(it.user_id, userId)
                        set(it.video_id, videoIdInt)
                        set(it.comment_id, commentIdInt)
                        set(it.isLike, 0)
                    }

                    if (create > 0) {
                        val myComment = db.from(EntityMyComment)
                            .select()
                            .where(
                                EntityMyComment.user_id eq userId
                                        and (EntityMyComment.video_id eq videoIdInt)
                                        and (EntityMyComment.comment_id eq commentIdInt)
                            )
                            .map {
                                MyCommentStatus(
                                    my_comment_id = it[EntityMyComment.my_comment_id] ?: 0,
                                    user_id = it[EntityMyComment.user_id].orEmpty(),
                                    video_id = it[EntityMyComment.video_id] ?: 0,
                                    comment_id = it[EntityMyComment.comment_id] ?: 0,
                                    isLike = it[EntityMyComment.isLike] ?: 0
                                )
                            }.single()

                        if (myComment.my_comment_id!! > 0) {
                            call.respond(myComment)
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
                val myCommentId = call.request.queryParameters["my_comment_id"].toString()
                val myCommentIdInt = myCommentId?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityMyComment)
                {
                    it.my_comment_id eq myCommentIdInt
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
                        GenericResponse(isSuccess = false, data = "Error to delete")
                    )
                }
            }
        }
    }
}