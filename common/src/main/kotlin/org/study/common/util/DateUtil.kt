package org.study.common.util

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

const val datePattern = "yyyy-MM-dd"
const val datetimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

private fun toDateFormatter() = DateTimeFormat.forPattern(datePattern)
private fun toDatetimeFormatter() = DateTimeFormat.forPattern(datetimePattern)

fun String.toDateTime() = DateTime.parse(this, toDatetimeFormatter())
fun LocalDate.toDateTime() = this.toDateTime(LocalTime(0, 0, 0, 0))
fun String.toDate() = LocalDate.parse(this, toDateFormatter()).toDateTime()