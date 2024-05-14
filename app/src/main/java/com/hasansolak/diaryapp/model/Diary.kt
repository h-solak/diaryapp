package com.hasansolak.diaryapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "diaries")
@Parcelize
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val diaryDate: String,
    val diaryContent: String
): Parcelable
