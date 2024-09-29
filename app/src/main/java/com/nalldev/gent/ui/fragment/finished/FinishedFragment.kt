package com.nalldev.gent.ui.fragment.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nalldev.gent.R
import com.nalldev.gent.databinding.FragmentFinishedBinding
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.ui.adapter.EventAdapter
import com.nalldev.gent.utils.SpacingItemDecoration
import com.nalldev.gent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<FinishedViewModel>()

    private val eventAdapterListener by lazy {
        object : EventAdapter.EventListener {
            override fun onBookmarkClicked(eventData: EventModel) {
                viewModel.updateEventBookmark(eventData)
            }

            override fun onItemClicked(eventData: EventModel) {
            }
        }
    }

    private val finishedEventAdapter by lazy { EventAdapter(eventAdapterListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
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
                    binding.finishedLoading.visibility = View.GONE
                }
                is UIState.Loading -> {
                    binding.finishedLoading.visibility = View.VISIBLE
                    binding.tvFinishedNoData.visibility = View.GONE
                }
                is UIState.Success ->  {
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
        rvFinishedEvent.addItemDecoration(SpacingItemDecoration(1, 40, true))
        rvFinishedEvent.itemAnimator = null
        rvFinishedEvent.adapter = finishedEventAdapter
        svFinishedEvent.setupWithSearchBar(binding.sbFinishedEvent)

        viewModel.getFinishedEvent()
    }

    private fun initListener() = with(binding) {
        svFinishedEvent.editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH -> {
                    sbFinishedEvent.setText(svFinishedEvent.text.toString())
                    viewModel.fetchEvent(svFinishedEvent.text.toString())
                    svFinishedEvent.hide()
                    true
                }
                else -> false
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}