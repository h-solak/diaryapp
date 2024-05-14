package com.hasansolak.diaryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hasansolak.diaryapp.databinding.DiaryItemLayoutBinding
import com.hasansolak.diaryapp.fragments.HomeFragmentDirections
import com.hasansolak.diaryapp.model.Diary

class DiaryAdapter : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(val itemBinding: DiaryItemLayoutBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Diary>(){
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.diaryContent == newItem.diaryContent &&
                    oldItem.diaryDate == newItem.diaryDate
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        return DiaryViewHolder(
            DiaryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val currentDiary = differ.currentList[position]

        holder.itemBinding.diaryDate.text = currentDiary.diaryDate
        holder.itemBinding.diaryContent.text = currentDiary.diaryContent

        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditDiaryFragment(currentDiary)
            it.findNavController().navigate(direction)
        }
    }
}