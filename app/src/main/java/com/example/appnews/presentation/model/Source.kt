package com.example.appnews.presentation.model

import java.io.Serializable

data class SourceUI(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
) : Serializable