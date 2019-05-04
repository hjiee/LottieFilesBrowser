package com.googry.lottiefilesbrowser.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dino.library.ui.BaseFragment
import com.dino.library.ui.SimpleRecyclerView
import com.dino.library.util.EndlessRecyclerViewScrollListener
import com.googry.lottiefilesbrowser.BR
import com.googry.lottiefilesbrowser.R
import com.googry.lottiefilesbrowser.data.enums.LottieUrl
import com.googry.lottiefilesbrowser.databinding.HomeFragmentBinding
import com.googry.lottiefilesbrowser.databinding.LottieFileItemBinding
import com.googry.lottiefilesbrowser.network.model.LottieInfoResponse
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val homeViewModel by viewModel<HomeViewModel>()

    private val endlessScrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(binding.rvLottieFile.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                homeViewModel.onLoad(page)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            vm = homeViewModel
            this.view = this@HomeFragment
            rvLottieFile.run {
                adapter =
                    object : SimpleRecyclerView.Adapter<LottieInfoResponse, LottieFileItemBinding>(
                        R.layout.lottie_file_item,
                        BR.item
                    ) {

                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): SimpleRecyclerView.ViewHolder<LottieFileItemBinding> {
                            return super.onCreateViewHolder(parent, viewType).apply {
                                binding.lavContent.imageAssetsFolder = "images/"
                                binding.lavContent.addLottieOnCompositionLoadedListener {
                                    binding.item?.lottieComposition = it
                                }
                            }
                        }

                    }

                addOnScrollListener(endlessScrollListener)
                itemAnimator = null
            }
            refreshLayout.setOnRefreshListener {
                homeViewModel.refresh()
            }
        }
    }

    fun onLottieUrlClick() {
        AlertDialog.Builder(context)
            .setItems(
                LottieUrl.values().map { it.name }.toTypedArray()
            ) { _, i ->
                run {
                    homeViewModel.liveLottieUrl.value = LottieUrl.values()[i]
                    homeViewModel.refresh()
                }
            }
            .setTitle("title")
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

    companion object {
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

}