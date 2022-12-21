package com.robivan.binlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robivan.binlist.App
import com.robivan.binlist.R
import com.robivan.binlist.databinding.FragmentMainBinding
import com.robivan.binlist.domain.AppState
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.utils.hide
import com.robivan.binlist.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        //        binding.recycler.adapter =
        viewModel.apply {
            liveData.observe(viewLifecycleOwner) { renderData(it) }
            getRequestsHistory()
        }
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                binding.recyclerLoader.show()
                binding.emptyList.hide()
            }
            is AppState.Success<*> -> {
                binding.recyclerLoader.hide()
                if (state.data is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    initRecycler(state.data as List<DetailsCard>)
                } else if (state.data is DetailsCard) {
                    openDetailsFragment(state.data)
                }
            }
            is AppState.Error -> {
                binding.recyclerLoader.hide()
                Toast.makeText(context, state.error.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initRecycler(cardList: List<DetailsCard>) {
        if (cardList.isEmpty()) {
            binding.emptyList.show()
        } else {
//            recyclerAdapter.submitList(cardList)
        }
    }

    private fun openDetailsFragment(card: DetailsCard) {
        childFragmentManager.beginTransaction()
            .add(R.id.container, DetailsFragment.newInstance(card))
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}