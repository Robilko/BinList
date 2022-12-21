package com.robivan.binlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.robivan.binlist.databinding.FragmentDetailsBinding
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.utils.hide

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val card: DetailsCard? by lazy {
        arguments?.getParcelable(CARD_ARG, DetailsCard::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card?.let {
            renderData(it)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getParcelable(CARD_ARG, DetailsCard::class.java)?.let { renderData(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        card?.let {
            outState.putParcelable(CARD_ARG, it)
        }
    }

    private fun renderData(card: DetailsCard) = with(binding) {
        cardNumber.text = card.number
        cardSchema.text = card.scheme.orEmpty()
        cardType.text = card.type.orEmpty()
        initRow(card.countryName, detailsCountryValue, detailsCountry)
        initRow(card.currency, detailsCurrencyValue, detailsCurrency)
        initRow(card.bankName, detailsBankNameValue, detailsBankName)
        initRow(card.bankCity, detailsBankCityValue, detailsBankCity)
        initRow(card.bankUrl, detailsBankWebsiteValue, detailsBankWebsite)
        card.bankPhone?.let {
            initRow(it[0], detailsBankPhoneValue1, detailsBankPhone1)
            initRow(it[1], detailsBankPhoneValue2, detailsBankPhone2)
        }
    }

    private fun initRow(data: String?, textView: TextView, row: TableRow) {
        if (data.isNullOrEmpty()) {
            row.hide()
        } else {
            textView.text = data
        }
    }

    companion object {
        private const val CARD_ARG = "card_arg"

        @JvmStatic
        fun newInstance(card: DetailsCard) =
            DetailsFragment().apply {
                arguments = bundleOf(CARD_ARG to card)
            }
    }
}