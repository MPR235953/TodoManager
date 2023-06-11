package com.example.todomanager

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DataTimeConverter {
    companion object{
        fun string2DateTime(string: String): LocalDateTime{
            return LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"))
        }

        fun dateTime2String(localDateTime: LocalDateTime): String{
            return localDateTime.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"))
        }

        fun date2String(localDateTime: LocalDateTime): String{
            return localDateTime.format(DateTimeFormatter.ofPattern("yy/MM/dd"))
        }

        fun time2String(localDateTime: LocalDateTime): String{
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        fun toMilis(localDateTime: LocalDateTime): Long {
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }
}