package com.example.appnews.presentation.headlines

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.FragmentFullArticleHeadlinesBinding
import com.example.appnews.presentation.FullArticleFragmentWeb
import com.example.appnews.presentation.dataclasses.FullArticleState
import com.example.appnews.presentation.navigation.OnBackPressedListener
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class FullArticleHeadlinesFragment : Fragment(), OnBackPressedListener {


    private var _binding: FragmentFullArticleHeadlinesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FullArticleViewModel> { FullArticleViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullArticleHeadlinesBinding.inflate(inflater, container, false)
        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backArrowToolBar()


        binding.ivSaveSign.setOnClickListener {

            viewModel.onSaveOrRemoveArticle()

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.stateIconSaved.collect(::stateIcon)
                }
                launch {
                    viewModel.contentState.collect(::setContent)
                }

            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setContent(state: FullArticleState) = with(binding) {
        Glide.with(collapsingIv.context).load(state.urlImage).into(collapsingIv)
        tvSourceFullArticle.text = state.nameSource
        tvDateFullArticle.text = state.date
        tvTitleFullArticle.text = state.title
        tvFullArticle.text = state.description
    }

    private fun stateIcon(state: Boolean) {

      val drawable =  if (state) {
            R.drawable.icon_saved_filled

        } else {
            R.drawable.icon_saved
        }

        binding.ivSaveSign.setImageResource(drawable)

    }

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }

    fun backArrowToolBar() {
        binding.imageButtonBackFullArticle.setOnClickListener {
            (requireActivity().application as App).router.exit()
        }
    }


    companion object {

        const val ARG = "ARG"

        @JvmStatic
        fun newInstance(article: ArticlesUI.Article) = FullArticleHeadlinesFragment().apply {
            arguments = Bundle().apply {

                putSerializable(ARG, article)

            }
        }
    }

}

