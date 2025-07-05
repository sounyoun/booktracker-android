package com.example.mybooktracker.model

import java.io.Serializable

data class Book(
    val title: String,
    val author: String,
    val totalPages: Int,
    var currentPage: Int,
    val memo: String,
    val startDate: String,
    val endDate: String
) : Serializable