package com.nalldev.gent.ui.fragment.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.nalldev.gent.databinding.FragmentExploreBinding
import com.nalldev.gent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<ExploreViewModel>()

    private val exploreAdapter by lazy { ExploreAdapter() }

    private val snapHelper by lazy { CarouselSnapHelper() }

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
        viewModel.upcomingEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {

                }
                is UIState.Loading -> {

                }
                is UIState.Success -> {
                }
            }
        }

        viewModel.finishedEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {
                }
                is UIState.Loading -> {
                }
                is UIState.Success -> {
                    println("SIZE OF UPCOMING : ${state.data.size}")
                    exploreAdapter.submitList(state.data)
                }
            }
        }
    }

    private fun initView() {
        snapHelper.attachToRecyclerView(binding.rvUpcomingEvent)
        binding.rvUpcomingEvent.layoutManager = CarouselLayoutManager().apply {
            setCarouselStrategy(HeroCarouselStrategy())
            setCarouselAlignment(CarouselLayoutManager.ALIGNMENT_CENTER)
        }
        binding.rvUpcomingEvent.adapter = exploreAdapter

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