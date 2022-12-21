package com.robivan.binlist.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.robivan.binlist.R
import com.robivan.binlist.databinding.FragmentMainBinding
import com.robivan.binlist.domain.AppState
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.ui.recycler.CardsRVAdapter
import com.robivan.binlist.ui.recycler.ItemClickListener
import com.robivan.binlist.utils.hide
import com.robivan.binlist.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MainViewModel>()

    private val recyclerAdapter = CardsRVAdapter(object : ItemClickListener {
        override fun onItemClick(card: DetailsCard) {
            openDetailsFragment(card)
        }
    })


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
        initSearch()
    }

    private fun initData() {
        binding.recycler.adapter = recyclerAdapter
        viewModel.apply {
            liveData.observe(viewLifecycleOwner) { renderData(it) }
            getRequestsHistory()
        }
    }

    private fun initSearch() = with(binding) {
        searchEditText.apply {
            addTextChangedListener { editable ->
                val currentText = editable.toString()
                val length = currentText.length
                if (length == 4) {
                    this.setText(currentText.plus(" "))
                    this.setSelection(length + 1)
                }
                if (length == 7) {
                    this.setText(currentText.plus("**"))
                    this.setSelection(length + 2)
                }
                searchButton.isEnabled = length > 6
            }
            setOnKeyListener { v, keyCode, event ->
                Log.d("TAG", "initSearch() called with:  keyCode = $keyCode, event = $event")
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d(
                        "TAG",
                        "initSearch() called with: v = $v, keyCode = $keyCode, event = $event"
                    )
                    val currentText = searchEditText.text.toString()
                    val length = currentText.length
                    if (length > 0) {
                        when (currentText.last()) {
                            ' ' -> {
                                searchEditText.apply {
                                    setText(currentText.substring(0, length - 2))
                                    this.text?.length?.let { setSelection(it) }
                                }
                            }
                            '*' -> {
                                searchEditText.apply {
                                    setText(currentText.substring(0, length - 3))
                                    this.text?.length?.let { setSelection(it) }
                                }
                            }
                            else -> {
                                return@setOnKeyListener false
                            }
                        }
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }

        searchInputLayout.setEndIconOnClickListener {
            hideKeyboard(it)
            sendSearchRequest()
        }
        searchButton.setOnClickListener { sendSearchRequest() }
    }

    private fun sendSearchRequest() = with(binding) {
        val currentText = searchEditText.text.toString().filter { it.isDigit() }
        searchEditText.text?.clear()
        viewModel.getCardInfo(currentText)
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
            recyclerAdapter.submitList(cardList)
        }
    }

    private fun openDetailsFragment(card: DetailsCard) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.container, DetailsFragment.newInstance(card))
            .commit()
    }

    private fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}