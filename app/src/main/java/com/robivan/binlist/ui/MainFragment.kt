package com.robivan.binlist.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
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
    private var currentList = listOf<DetailsCard>()


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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                if (length == DIGITS_COUNT_BEFORE_SPACE) {
                    this.setText(currentText.plus(" "))
                    this.setSelection(length + ONE_CHAR_AMOUNT)
                }
                if (length == SIX_DIGITS_WITH_SPACE) {
                    this.setText(currentText.plus("**"))
                    this.setSelection(length + TWO_CHAR_AMOUNT)
                }
                searchButton.isEnabled = length > VALID_CHAR_AMOUNT
            }
            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    val currentText = searchEditText.text.toString()
                    val length = currentText.length
                    if (length > 0) {
                        when (currentText.last()) {
                            SPACE_CHAR -> {
                                searchEditText.apply {
                                    setText(currentText.substring(0, length - DIGIT_WITH_SPACE_AMOUNT))
                                    this.text?.length?.let { setSelection(it) }
                                }
                            }
                            STAR_CHAR -> {
                                searchEditText.apply {
                                    setText(currentText.substring(0, length - DIGIT_WITH_STARS_AMOUNT))
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
        searchButton.setOnClickListener {
            hideKeyboard(it)
            sendSearchRequest()
        }
    }

    private fun sendSearchRequest() = with(binding) {
        val currentText = searchEditText.text.toString().filter { it.isDigit() }
        if (currentText.length < VALID_CHAR_AMOUNT) {
            Toast.makeText(
                context,
                getString(R.string.toast_attention_short_request),
                Toast.LENGTH_LONG
            ).show()
        } else {
            searchEditText.text?.clear()
            viewModel.getCardInfo(currentText)
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
                    refreshRecycler(state.data)
                }
            }
            is AppState.Error -> {
                binding.recyclerLoader.hide()
                state.error.localizedMessage.let {
                    if (it == ERROR_404) {
                        showErrorDialog(
                            getString(R.string.error_message_404),
                            getString(R.string.dialog_attention_title),
                            R.drawable.ic_attention
                        )
                    } else {
                        showErrorDialog(
                            it,
                            getString(R.string.dialog_error_title),
                            R.drawable.ic_error
                        )
                    }
                }
            }
        }
    }

    private fun showErrorDialog(message: String?, title: String, iconResId: Int) {
        AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setIcon(iconResId)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .show()
    }

    private fun initRecycler(cardList: List<DetailsCard>) {
        if (cardList.isEmpty()) {
            binding.emptyList.show()
        } else {
            currentList = cardList
            recyclerAdapter.submitList(cardList)
        }
    }

    private fun refreshRecycler(card: DetailsCard) {
        currentList = currentList.toMutableList().also { it.add(card) }
        recyclerAdapter.submitList(currentList)
        binding.recycler.smoothScrollToPosition(currentList.size - 1)
    }

    private fun openDetailsFragment(card: DetailsCard) {
        view?.let { hideKeyboard(it) }
        DetailsDialogFragment(card, object : DetailsOnClickListener {
            override fun onCountryClicked(latitude: Double, longitude: Double) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude?z=5")))
            }

            override fun onWebsiteClicked(url: String) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }

            override fun onPhoneClicked(phoneNumber: String) {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
            }
        }).show(requireActivity().supportFragmentManager, DETAILS_DIALOG_KEY)
    }

    private fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val DETAILS_DIALOG_KEY = "details_dialog"
        private const val ERROR_404 = "HTTP 404 "
        private const val SPACE_CHAR = ' '
        private const val STAR_CHAR = '*'
        private const val DIGITS_COUNT_BEFORE_SPACE = 4
        private const val SIX_DIGITS_WITH_SPACE = 7
        private const val ONE_CHAR_AMOUNT = 1
        private const val TWO_CHAR_AMOUNT = 2
        private const val VALID_CHAR_AMOUNT = 6
        private const val DIGIT_WITH_SPACE_AMOUNT = 2
        private const val DIGIT_WITH_STARS_AMOUNT = 3

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}