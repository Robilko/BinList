package com.robivan.binlist.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.robivan.binlist.R
import com.robivan.binlist.databinding.FragmentDetailsBinding
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.utils.hide

class DetailsDialogFragment(
    private val card: DetailsCard,
    private val detailsListener: DetailsOnClickListener
) : DialogFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog)
            .setView(binding.root).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        initCloseButton()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initCloseButton() {
        binding.backButton.setOnClickListener { dismiss() }
    }

    private fun initData() = with(binding) {
        cardNumber.text = card.number
        cardSchema.text = card.scheme.orEmpty()
        cardType.text = card.type.orEmpty()
        initRow(card.countryName, detailsCountryValue, detailsCountry)
        initRow(card.currency, detailsCurrencyValue, detailsCurrency)
        initRow(card.bankName, detailsBankNameValue, detailsBankName)
        initRow(card.bankCity, detailsBankCityValue, detailsBankCity)
        initRow(card.bankUrl, detailsBankWebsiteValue, detailsBankWebsite)
        card.bankPhone?.let {
            when {
                it.size == 1 -> {
                    initRow(it[0], detailsBankPhoneValue1, detailsBankPhone1)
                    detailsBankPhone2.hide()
                }
                it.size >= 2 -> initRow(it[1], detailsBankPhoneValue2, detailsBankPhone2)
                else -> {}
            }
        }
    }

    private fun initRow(data: String?, textView: TextView, row: TableRow) {
        if (data.isNullOrEmpty()) {
            row.hide()
        } else {
            textView.text = data
        }
    }
}