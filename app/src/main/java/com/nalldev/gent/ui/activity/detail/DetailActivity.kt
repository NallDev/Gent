package com.nalldev.gent.ui.activity.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.nalldev.gent.R
import com.nalldev.gent.databinding.ActivityDetailBinding
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.utils.DateHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModel<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventData = getEventDataFromIntent()

        eventData?.let { event ->
            initView(event)
            initListener(event)
        }
    }

    private fun getEventDataFromIntent(): EventModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EVENT_DATA, EventModel::class.java)
        } else {
            intent.getParcelableExtra(EVENT_DATA)
        }
    }

    private fun initView(event: EventModel) {
        binding.ivImageEvent.load(event.image)
        binding.tvNameEvent.text = event.name
        binding.tvDateEvent.text =
            DateHelper.changeFormat("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy", event.beginTime)
        binding.tvLocationEvent.text = event.cityName
        binding.tvOrganizerEvent.text = event.ownerName
        binding.tvQuotaEvent.text = getString(R.string.seats_left, event.quota - event.registrants)
        binding.bookmarkToggleButton.isChecked = event.isBookmark
        binding.tvDescription.text =
            HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = event.link.toUri()
            startActivity(intent)
        }

        binding.bookmarkToggleButton.setOnClickListener {
            viewModel.updateEventBookmark(event)
        }

    }

    private fun initListener(event: EventModel) = with(binding) {
        btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, event.link.toUri())
            startActivity(intent)
        }

        bookmarkToggleButton.setOnClickListener {
            viewModel.updateEventBookmark(event)
        }
    }

    companion object {
        const val EVENT_DATA = "event_data"
    }
}