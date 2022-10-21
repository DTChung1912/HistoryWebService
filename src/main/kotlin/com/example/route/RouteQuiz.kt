package com.example.route

import com.example.models.Quiz
import com.example.mysql.DbConnection
import com.example.mysql.entity.EntityAnswer
import com.example.mysql.entity.EntityQuiz
import com.example.util.GenericResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.routeQuiz() {
    val db: Database = DbConnection.getDatabaseInstance()
    routing {
        route("/quiz") {
            get("/random") {
                val list = db.from(EntityQuiz).innerJoin(EntityAnswer)
                    .select()
                    .where(EntityQuiz.answer_id eq EntityAnswer.answer_id)
                    .limit(40)
                    .map {
                        Quiz(
                            quiz_id = it[EntityQuiz.quiz_id] ?: 0,
                            question = it[EntityQuiz.question].orEmpty(),
                            theme_id = it[EntityQuiz.theme_id] ?: 0,
                            image = it[EntityQuiz.image].orEmpty(),
                            answer_id = it[EntityQuiz.answer_id] ?: 0,
                            correct = it[EntityAnswer.correct].orEmpty(),
                            incorrect_1 = it[EntityAnswer.incorrect_1].orEmpty(),
                            incorrect_2 = it[EntityAnswer.incorrect_2].orEmpty(),
                            incorrect_3 = it[EntityAnswer.incorrect_3].orEmpty()
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

            get("/theme") {
                val themeId = call.request.queryParameters["theme_id"].toString()
                val themeIdInt = themeId.toInt() ?: 0

                val list = db.from(EntityQuiz).innerJoin(EntityAnswer)
                    .select()
                    .where(
                        EntityQuiz.theme_id eq themeIdInt
                                and (EntityQuiz.answer_id eq EntityAnswer.answer_id)
                    )
                    .limit(40)
                    .map {
                        Quiz(
                            quiz_id = it[EntityQuiz.quiz_id] ?: 0,
                            question = it[EntityQuiz.question].orEmpty(),
                            theme_id = it[EntityQuiz.theme_id] ?: 0,
                            image = it[EntityQuiz.image].orEmpty(),
                            answer_id = it[EntityQuiz.answer_id] ?: 0,
                            correct = it[EntityAnswer.correct].orEmpty(),
                            incorrect_1 = it[EntityAnswer.incorrect_1].orEmpty(),
                            incorrect_2 = it[EntityAnswer.incorrect_2].orEmpty(),
                            incorrect_3 = it[EntityAnswer.incorrect_3].orEmpty()
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
                val quiz: Quiz = call.receive()
                val noOfRowsAffected = db.insert(EntityQuiz)
                {
                    set(it.quiz_id, null)
                    set(it.question, quiz.question)
                    set(it.theme_id, quiz.theme_id)
                    set(it.image, quiz.image)
                    set(it.answer_id, quiz.answer_id)
                }

                val noOfRowsAffected2 = db.insert(EntityAnswer)
                {
                    set(it.answer_id, null)
                    set(it.correct, quiz.correct)
                    set(it.incorrect_1, quiz.incorrect_1)
                    set(it.incorrect_2, quiz.incorrect_2)
                    set(it.incorrect_1, quiz.incorrect_3)
                }

                if (noOfRowsAffected > 0 && noOfRowsAffected2 > 0) {
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
//                val quizId = call.parameters["quiz_id"].toString()
//                val quizIdInt = quizId.toInt()

                val quiz: Quiz = call.receive()
                val answerId = quiz.answer_id!!.toInt()

                val noOfRowsAffected = db.update(EntityQuiz)
                {
                    set(it.question, quiz.question)
                    set(it.theme_id, quiz.theme_id)
                    set(it.image, quiz.image)
                    where {
                        it.quiz_id eq quiz.quiz_id!!
                    }
                }

                val noOfRowsAffected2 = db.update(EntityAnswer)
                {
                    set(it.correct, quiz.correct)
                    set(it.incorrect_1, quiz.incorrect_1)
                    set(it.incorrect_2, quiz.incorrect_2)
                    set(it.incorrect_3, quiz.incorrect_3)
                    where {
                        it.answer_id eq answerId
                    }
                }

                if (noOfRowsAffected > 0 && noOfRowsAffected2 > 0) {
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
                val quizId = call.request.queryParameters["quiz_id"].toString()
                val quizIdInt = quizId?.toInt() ?: -1

                val quiz = db.from(EntityQuiz)
                    .select()
                    .where(EntityQuiz.quiz_id eq quizIdInt)
                    .map {
                        Quiz(
                            quiz_id = it[EntityQuiz.quiz_id],
                            question = it[EntityQuiz.question],
                            theme_id = it[EntityQuiz.theme_id],
                            image = it[EntityQuiz.image],
                            answer_id = it[EntityQuiz.answer_id]
                        )
                    }
                    .firstOrNull()

                val answerId = quiz?.answer_id ?: -1
                if (answerId > -1) {
                    val noOfRowsAffected = db.delete(EntityAnswer)
                    {
                        it.answer_id eq answerId
                    }

                    val noOfRowsAffected2 = db.delete(EntityQuiz)
                    {
                        it.quiz_id eq quizIdInt
                    }

                    if (noOfRowsAffected > 0 && noOfRowsAffected2 > 0) {
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
}