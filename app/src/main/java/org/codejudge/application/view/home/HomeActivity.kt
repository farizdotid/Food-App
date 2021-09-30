package org.codejudge.application.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.codejudge.application.R
import org.codejudge.application.databinding.ActivityHomeBinding
import org.codejudge.application.utils.network.Result
import org.codejudge.application.utils.snack
import org.codejudge.application.utils.visibleOrGone
import org.codejudge.application.view.adapter.NearbyPlaceAdapter

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var nearbyPlaceAdapter: NearbyPlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initObserveViewModel()
        initNearbyPlaceAdapter()
        initAction()
    }

    private fun initObserveViewModel() {
        binding.editSearch.doAfterTextChanged { text -> viewModel.keyword = text.toString() }

        viewModel.nearbyPlaceResponse.observe(this, { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    hideLoading()
                    result.data?.let {
                        nearbyPlaceAdapter.setData(it)
                    }
                }

                Result.Status.ERROR -> {
                    hideLoading()
                    result.message?.let {
                        binding.root.snack(it, length = Snackbar.LENGTH_SHORT)
                    }
                }

                Result.Status.LOADING -> {
                    showLoading()
                }
            }
        })

        viewModel.isSearchValid.observe(this, Observer { valid ->
            binding.ivClose.visibleOrGone(valid)
        })
    }

    private fun initNearbyPlaceAdapter() {
        nearbyPlaceAdapter = NearbyPlaceAdapter(arrayListOf())
        binding.rvNearbyPlaces.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = nearbyPlaceAdapter
        }
    }

    private fun initAction() {
        binding.ivClose.setOnClickListener { binding.editSearch.setText("") }
    }

    private fun showLoading() {
        binding.pbLoading.visibleOrGone(visible = true)
        binding.rvNearbyPlaces.visibleOrGone(visible = false)
    }

    private fun hideLoading() {
        binding.pbLoading.visibleOrGone(visible = false)
        binding.rvNearbyPlaces.visibleOrGone(visible = true)
    }

    companion object {
        private const val EXTRA_IS_CLEAR_BACK_STACK = "extra_is_clear_back_stack"

        fun start(context: Context, isClearBackStack: Boolean) {
            val starter = Intent(context, HomeActivity::class.java)
            starter.putExtra(EXTRA_IS_CLEAR_BACK_STACK, isClearBackStack)

            if (isClearBackStack) {
                starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            context.startActivity(starter)
        }
    }
}