package com.indivar.common.adapters

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDate


data class JsonLocalDate(
    val date: String?,


    )

class LocalDateAdapter : JsonAdapter<LocalDate>() {

    @FromJson

    override fun fromJson(reader: JsonReader): LocalDate? =
        try {
            val readValue = reader.nextString()
            try {
                val jsonDateTime =
                    Gson().fromJson<JsonLocalDate>(readValue, JsonLocalDate::class.java)
                LocalDate.parse(jsonDateTime.date)

            } catch (e: JsonSyntaxException) {
                LocalDate.parse(readValue)
            }

        } catch (e: Exception) {
            null
        }

    @ToJson
    override fun toJson(writer: JsonWriter, input: LocalDate?) {
        val date: String = input.toString()


        writer.value(Gson().toJson(JsonLocalDate(date)))
    }
}