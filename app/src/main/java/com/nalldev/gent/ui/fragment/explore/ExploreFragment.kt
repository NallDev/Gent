package com.nalldev.gent.ui.fragment.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nalldev.gent.databinding.FragmentExploreBinding
import com.nalldev.gent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<ExploreViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()
        initListener()
    }

    private fun initObserver() {
        viewModel.finishedEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {
                    println("ERROR FINISHED : ${state.message}")
                }
                is UIState.Loading -> {
                    println("LOADING FINISHED EVENT")
                }
                is UIState.Success -> {
                    if (state.data.isEmpty()) {
                        viewModel.fetchEvent()
                    }
                    println("SUCCESS FINISHED : ${state.data}")
                }
            }
        }

        viewModel.upcomingEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {
                    println("ERROR UPCOMING : ${state.message}")
                }
                is UIState.Loading -> {
                    println("LOADING UPCOMING EVENT")
                }
                is UIState.Success -> {
                    if (state.data.isEmpty()) {
                        viewModel.fetchEvent()
                    }
                    println("SUCCESS UPCOMING : ${state.data}")
                }
            }
        }
    }

    private fun initView() {
        viewModel.getUpcomingEvent()
        viewModel.getFinishedEvent()
    }

    private fun initListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}