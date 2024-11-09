package com.manikandareas.stories.auth.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)

@Serializable
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
)


//"error": false,
//"message": "success",
//"loginResult": {
//    "userId": "user-yj5pc_LARC_AgK61",
//    "name": "Arif Faizin",
//    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
//}