package com.nalldev.gent.ui.fragment.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nalldev.gent.R
import com.nalldev.gent.databinding.FragmentUpcomingBinding
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.ui.activity.detail.DetailActivity
import com.nalldev.gent.ui.adapter.EventAdapter
import com.nalldev.gent.utils.SpacingItemDecoration
import com.nalldev.gent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class UpcomingFragment : Fragment() {
    private var _binding : FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<UpcomingViewModel>()

    private val eventAdapterListener by lazy {
        object : EventAdapter.EventListener {
            override fun onBookmarkClicked(eventData: EventModel) {
                viewModel.updateEventBookmark(eventData)
            }

            override fun onItemClicked(eventData: EventModel) {
                activity?.let {
                    val intent = Intent(it, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EVENT_DATA, eventData)
                    it.startActivity(intent)
                }
            }
        }
    }
    private val upcomingEventAdapter by lazy { EventAdapter(eventAdapterListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUpcomingEvent()
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
                is UIState.Success ->  {
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

        viewModel.toastEvent.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() = with(binding) {
        rvUpcomingEvent.addItemDecoration(SpacingItemDecoration(1, 40, true))
        rvUpcomingEvent.itemAnimator = null
        rvUpcomingEvent.adapter = upcomingEventAdapter
        svUpcomingEvent.setupWithSearchBar(binding.sbUpcomingEvent)
    }

    private fun initListener() = with(binding) {
        svUpcomingEvent.editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH -> {
                    sbUpcomingEvent.setText(svUpcomingEvent.text.toString())
                    viewModel.fetchEvent(svUpcomingEvent.text.toString())
                    svUpcomingEvent.hide()
                    true
                }
                else -> false
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}