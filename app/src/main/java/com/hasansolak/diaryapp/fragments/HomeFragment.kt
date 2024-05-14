package com.hasansolak.diaryapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hasansolak.diaryapp.MainActivity
import com.hasansolak.diaryapp.R
import com.hasansolak.diaryapp.adapter.DiaryAdapter
import com.hasansolak.diaryapp.databinding.FragmentHomeBinding
import com.hasansolak.diaryapp.model.Diary
import com.hasansolak.diaryapp.viewmodel.DiaryViewModel

class HomeFragment : Fragment(R.layout.fragment_home), MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var diariesViewModel : DiaryViewModel
    private lateinit var diaryAdapter: DiaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        diariesViewModel = (activity as MainActivity).diaryViewModel
        setupHomeRecyclerView()

        binding.editDiaryBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addDiaryFragment)
        }
    }

    private fun updateUI(diary: List<Diary>?){
        if (diary != null){
            if (diary.isNotEmpty()){
                binding.emptyDiaryImageContainer.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyDiaryImageContainer.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView(){
        diaryAdapter = DiaryAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = diaryAdapter
        }

        activity?.let {
            diariesViewModel.getAllDiaries().observe(viewLifecycleOwner){ diary ->
                diaryAdapter.differ.submitList(diary)
                updateUI(diary)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}