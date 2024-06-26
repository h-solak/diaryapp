package com.hasansolak.diaryapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hasansolak.diaryapp.repository.DiaryRepository

class DiaryViewModelFactory(val app: Application, private val diaryRepository: DiaryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiaryViewModel(app, diaryRepository) as T
    }
}