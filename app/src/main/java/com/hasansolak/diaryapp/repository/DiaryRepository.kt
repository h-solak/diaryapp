package com.hasansolak.diaryapp.repository

import com.hasansolak.diaryapp.database.DiaryDatabase
import com.hasansolak.diaryapp.model.Diary

class DiaryRepository(private val db: DiaryDatabase) {

    suspend fun insertDiary(diary: Diary) = db.getDiaryDao().insertDiary(diary)
    suspend fun deleteDiary(diary: Diary) = db.getDiaryDao().deleteDiary(diary)
    suspend fun updateDiary(diary: Diary) = db.getDiaryDao().updateDiary(diary)

    fun getAllDiaries() = db.getDiaryDao().getAllDiaries()

}