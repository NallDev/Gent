package com.nalldev.gent.ui.fragment.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nalldev.gent.R
import com.nalldev.gent.databinding.FragmentBookmarkBinding
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.ui.adapter.EventAdapter
import com.nalldev.gent.utils.SpacingItemDecoration
import com.nalldev.gent.utils.UIState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class BookmarkFragment : Fragment() {
    private var _binding : FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<BookmarkViewModel>()

    private val eventAdapterListener by lazy {
        object : EventAdapter.EventListener {
            override fun onBookmarkClicked(eventData: EventModel) {
                viewModel.updateEventBookmark(eventData)
            }

            override fun onItemClicked(eventData: EventModel) {
            }
        }
    }
    private val bookmarkAdapter by lazy { EventAdapter(eventAdapterListener) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getBookmarkEvent()
    }

    private fun initObserver() {
        viewModel.bookmarkEvent.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.Error -> {
                    binding.bookmarkLoading.visibility = View.GONE
                }
                UIState.Loading -> {
                    binding.bookmarkLoading.visibility = View.VISIBLE
                    binding.tvBookmarkNoData.visibility = View.GONE
                }
                is UIState.Success -> {
                    binding.bookmarkLoading.visibility = View.GONE
                    if (state.data.isEmpty()) {
                        binding.rvBookmarkEvent.visibility = View.INVISIBLE
                        binding.tvBookmarkNoData.visibility = View.VISIBLE
                        binding.tvBookmarkNoData.text = getString(R.string.your_bookmark_is_empty)
                    } else {
                        binding.rvBookmarkEvent.visibility = View.VISIBLE
                        bookmarkAdapter.submitList(state.data)
                    }
                }
            }
        }

        viewModel.toastEvent.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() = with(binding) {
        rvBookmarkEvent.addItemDecoration(SpacingItemDecoration(1, 40, true))
        rvBookmarkEvent.itemAnimator = null
        rvBookmarkEvent.adapter = bookmarkAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}