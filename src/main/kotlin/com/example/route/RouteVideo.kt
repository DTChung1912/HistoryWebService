package com.example.route

import com.example.models.Video
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityVideo
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeVideo() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/video") {
            get("/list") {
                val list = db.from(EntityVideo)
                    .select()
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
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
                val videoIdStr = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoIdStr.toInt() ?: 0

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }

                if (video.isNotEmpty()) {
                    call.respond(video)
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
                }
            }

            get("/next") {

                val videoIdStr = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoIdStr.toInt() ?: 0

                val list = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id notEq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
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

            get("/search") {
//                val formParameters = call.receiveParameters()

                val keyword = call.request.queryParameters["keyword"].toString()
                val list = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.title.like("%$keyword%"))
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
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

            get("/test") {
//                val formParameters = call.receiveParameters()
                val test1 = call.parameters["test1"].toString()
                val test2 = call.parameters["test2"].toString()

                if (test1.isNotEmpty()) {
                    call.respond("$test1,,,,,$test2 ")
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        GenericResponse(isSuccess = false, data = null)
                    )
                }
            }

            post("/create") {
                val video: Video = call.receive()
                val noOfRowsAffected = db.insert(EntityVideo)
                {
                    set(it.video_id, null)
                    set(it.title, video.title)
                    set(it.theme_id, video.theme_id)
                    set(it.creater_image, video.creater_image)
                    set(it.creater, video.creater)
                    set(it.platform, video.platform)
                    set(it.like_count, 0)
                    set(it.view_count, 0)
                    set(it.dislike_count, 0)
                    set(it.comment_count, 0)
                    set(it.download_count, 0)
                    set(it.video_url, video.video_url)
                    set(it.poster_image, video.poster_image)
                    set(it.date_submitted, video.date_submitted)
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
//                val videoId = call.parameters["video_id"].toString()
//                val videoIdInt = videoId.toInt()

                val video: Video = call.receive()

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.title, video.title)
                    set(it.theme_id, video.theme_id)
                    set(it.creater_image, video.creater_image)
                    set(it.creater, video.creater)
                    set(it.platform, video.platform)
                    set(it.view_count, video.view_count)
                    set(it.like_count, video.like_count)
                    set(it.dislike_count, video.dislike_count)
                    set(it.comment_count, video.comment_count)
                    set(it.download_count, video.download_count)
                    set(it.video_url, video.video_url)
                    set(it.poster_image, video.poster_image)
                    set(it.date_submitted, video.date_submitted)
                    where {
                        it.video_id eq video.video_id!!
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

            put("/update/view") {
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val viewCount = video.view_count!! + 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.view_count, viewCount)
                    where {
                        it.video_id eq videoIdInt
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
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val likeCount = video.like_count!! + 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.like_count, likeCount)
                    where {
                        it.video_id eq videoIdInt
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
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val dislikeCount = video.dislike_count!! + 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.dislike_count, dislikeCount)
                    where {
                        it.video_id eq videoIdInt
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
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val likeCount = video.like_count!! - 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.like_count, likeCount)
                    where {
                        it.video_id eq videoIdInt
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
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val dislikeCount = video.dislike_count!! - 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.dislike_count, dislikeCount)
                    where {
                        it.video_id eq videoIdInt
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

            put("/update/comment") {
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val commentCount = video.comment_count!! + 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.comment_count, commentCount)
                    where {
                        it.video_id eq videoIdInt
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

            put("/update/comment/cancel") {
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId.toInt()

                val video = db.from(EntityVideo)
                    .select()
                    .where(EntityVideo.video_id eq videoIdInt)
                    .map {
                        Video(
                            video_id = it[EntityVideo.video_id] ?: 0,
                            title = it[EntityVideo.title].orEmpty(),
                            theme_id = it[EntityVideo.theme_id] ?: 0,
                            creater_image = it[EntityVideo.creater_image].orEmpty(),
                            creater = it[EntityVideo.creater].orEmpty(),
                            platform = it[EntityVideo.platform].orEmpty(),
                            like_count = it[EntityVideo.like_count] ?: 0,
                            view_count = it[EntityVideo.view_count] ?: 0,
                            dislike_count = it[EntityVideo.dislike_count] ?: 0,
                            comment_count = it[EntityVideo.comment_count] ?: 0,
                            download_count = it[EntityVideo.download_count] ?: 0,
                            video_url = it[EntityVideo.video_url].orEmpty(),
                            poster_image = it[EntityVideo.poster_image].orEmpty(),
                            date_submitted = it[EntityVideo.date_submitted].orEmpty(),
                        )
                    }.first()

                val commentCount = video.comment_count!! - 1

                val noOfRowsAffected = db.update(EntityVideo)
                {
                    set(it.comment_count, commentCount)
                    where {
                        it.video_id eq videoIdInt
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
                val videoId = call.request.queryParameters["video_id"].toString()
                val videoIdInt = videoId?.toInt() ?: -1

                val noOfRowsAffected = db.delete(EntityVideo)
                {
                    it.video_id eq videoIdInt
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