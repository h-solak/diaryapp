package com.hasansolak.diaryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hasansolak.diaryapp.model.Diary
import com.hasansolak.diaryapp.repository.DiaryRepository
import kotlinx.coroutines.launch

class DiaryViewModel(app: Application, private val diaryRepository: DiaryRepository): AndroidViewModel(app) {

    fun createDiary(diary: Diary) =
        viewModelScope.launch {
            diaryRepository.insertDiary(diary)
        }

    fun deleteDiary(diary: Diary) =
        viewModelScope.launch {
            diaryRepository.deleteDiary(diary)
        }

    fun updateDiary(diary: Diary) =
        viewModelScope.launch {
            diaryRepository.updateDiary(diary)
        }

    fun getAllDiaries() = diaryRepository.getAllDiaries()

}