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
import com.nalldev.gent.ui.adapter.ExploreAdapter
import com.nalldev.gent.ui.adapter.EventAdapter
import com.nalldev.gent.utils.SpacingItemDecoration
import com.nalldev.gent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<ExploreViewModel>()

    private val upcomingEventAdapter by lazy { ExploreAdapter() }

    private val finishedEventAdapter by lazy { EventAdapter() }

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
                    upcomingEventAdapter.submitList(state.data)
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
                    finishedEventAdapter.submitList(state.data)
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
        binding.rvUpcomingEvent.adapter = upcomingEventAdapter

        finishedEventAdapter.hideBookmark()
        binding.rvFinishedEvent.addItemDecoration(SpacingItemDecoration(1, 16, true))
        binding.rvFinishedEvent.adapter = finishedEventAdapter

        viewModel.getUpcomingEvent()
        viewModel.getFinishedEvent()
    }

    private fun initListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.fetchEvent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}