package com.hasansolak.diaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.hasansolak.diaryapp.database.DiaryDatabase
import com.hasansolak.diaryapp.repository.DiaryRepository
import com.hasansolak.diaryapp.viewmodel.DiaryViewModel
import com.hasansolak.diaryapp.viewmodel.DiaryViewModelFactory



//import android.graphics.drawable.Drawable
//import androidx.core.content.ContextCompat
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var diaryViewModel: DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

    }

    //start database
    private fun setupViewModel(){
        val diaryRepository = DiaryRepository(DiaryDatabase(this))
        val viewModelProviderFactory = DiaryViewModelFactory(application, diaryRepository)
        diaryViewModel = ViewModelProvider(this, viewModelProviderFactory)[DiaryViewModel::class.java]
    }



}