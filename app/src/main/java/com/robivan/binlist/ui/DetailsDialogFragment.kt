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
import com.robivan.binlist.utils.validateUrl
import org.apache.commons.lang3.StringEscapeUtils

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
        binding.backButton.setOnClickListener { dismiss() }
        initData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initData() = with(binding) {
        cardNumber.text = card.number
        cardSchema.text = card.scheme.orEmpty()
        cardTypeValue.text = card.type ?: "-"
        cardBrandValue.text = card.brand ?: "-"
        emoji.text = StringEscapeUtils.unescapeJava(card.countryEmoji)
        initRow(card.countryName, detailsCountryValue, detailsCountry)
        initRow(card.currency, detailsCurrencyValue, detailsCurrency)
        initRow(card.bankName, detailsBankNameValue, detailsBankName)
        initRow(card.bankCity, detailsBankCityValue, detailsBankCity)
        initRow(card.bankUrl, detailsBankWebsiteValue, detailsBankWebsite)
        initPhoneNumbers(card.bankPhone)
    }

    private fun initPhoneNumbers(list: List<String>?) = with(binding) {
        if (list.isNullOrEmpty()) {
            detailsBankPhone1.hide()
            detailsBankPhone2.hide()
        } else {
            when {
                list.size == 1 -> {
                    initRow(
                        list[0].filter { it.isDigit() },
                        detailsBankPhoneValue1,
                        detailsBankPhone1
                    )
                    detailsBankPhone2.hide()
                }
                list.size >= 2 -> initRow(
                    list[1].filter { it.isDigit() },
                    detailsBankPhoneValue2,
                    detailsBankPhone2
                )
                else -> {}
            }
        }
    }

    private fun initRow(data: String?, textView: TextView, row: TableRow) {
        if (data.isNullOrEmpty()) {
            row.hide()
        } else {
            textView.text = data
            when (row.id) {
                R.id.details_country -> {
                    binding.detailsCountryValue.setOnClickListener {
                        detailsListener.onCountryClicked(
                            card.countryLatitude,
                            card.countryLongitude
                        )
                    }
                }
                R.id.details_bank_website -> {
                    binding.detailsBankWebsiteValue.setOnClickListener {
                        detailsListener.onWebsiteClicked(validateUrl(data))
                    }
                }
                R.id.details_bank_phone1 -> {
                    binding.detailsBankPhoneValue1.setOnClickListener {
                        detailsListener.onPhoneClicked(data)
                    }
                }
                R.id.details_bank_phone2 -> {
                    binding.detailsBankPhoneValue2.setOnClickListener {
                        detailsListener.onPhoneClicked(data)
                    }
                }
            }
        }
    }
}