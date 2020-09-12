package com.example.camera.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val name: String?,
    val email: String?,
    val token: String?,
    val icon: String?
) {
    override fun toString(): String {
        return String.format("id: %s, name: %s, email: %s, token: %s, icon: %s",
            id, name, email, token, icon)
    }

    companion object{
        fun helper(user: User): User?{
            return User(id = user.id, token = user.token, email = user.email,
                name = user.name, icon = user.icon)
        }
    }
}
