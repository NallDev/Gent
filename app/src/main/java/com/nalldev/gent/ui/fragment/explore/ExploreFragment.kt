package com.nalldev.gent.ui.fragment.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.nalldev.gent.R
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
                    binding.upcomingLoading.visibility = View.GONE
                }
                is UIState.Loading -> {
                    binding.upcomingLoading.visibility = View.VISIBLE
                    binding.tvUpcomingNoData.visibility = View.GONE
                }
                is UIState.Success -> {
                    binding.upcomingLoading.visibility = View.GONE
                    if (state.data.isEmpty()) {
                        binding.rvUpcomingEvent.visibility = View.INVISIBLE
                        binding.tvUpcomingNoData.visibility = View.VISIBLE
                        binding.tvUpcomingNoData.text = getString(R.string.upcoming_event_empty_msg)
                    } else {
                        binding.rvUpcomingEvent.visibility = View.VISIBLE
                        upcomingEventAdapter.submitList(state.data)
                    }
                }
            }
        }

        viewModel.finishedEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {
                    binding.finishedLoading.visibility = View.GONE
                }
                is UIState.Loading -> {
                    binding.finishedLoading.visibility = View.VISIBLE
                    binding.tvFinishedNoData.visibility = View.GONE
                }
                is UIState.Success -> {
                    binding.finishedLoading.visibility = View.GONE
                    if (state.data.isEmpty()) {
                        binding.rvFinishedEvent.visibility = View.INVISIBLE
                        binding.tvFinishedNoData.visibility = View.VISIBLE
                        binding.tvFinishedNoData.text = getString(R.string.finished_event_empty_msg)
                    } else {
                        binding.rvFinishedEvent.visibility = View.VISIBLE
                        finishedEventAdapter.submitList(state.data)
                    }
                }
            }
        }

        viewModel.toastEvent.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() = with(binding) {
        snapHelper.attachToRecyclerView(rvUpcomingEvent)
        rvUpcomingEvent.layoutManager = CarouselLayoutManager().apply {
            setCarouselStrategy(HeroCarouselStrategy())
            setCarouselAlignment(CarouselLayoutManager.ALIGNMENT_CENTER)
        }
        rvUpcomingEvent.adapter = upcomingEventAdapter

        finishedEventAdapter.hideBookmark()
        rvFinishedEvent.addItemDecoration(SpacingItemDecoration(1, 40, true))
        rvFinishedEvent.adapter = finishedEventAdapter

        viewModel.getUpcomingEvent()
        viewModel.getFinishedEvent()
    }

    private fun initListener() = with(binding) {
        tvSeeAllUpcoming.setOnClickListener {
            findNavController().navigate(R.id.action_menu_explore_to_menu_upcoming)
        }

        tvSeeAllFinished.setOnClickListener {
            findNavController().navigate(R.id.action_menu_explore_to_menu_finished)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.fetchEvent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}