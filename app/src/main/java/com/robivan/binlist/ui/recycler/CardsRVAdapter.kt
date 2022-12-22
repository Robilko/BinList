package com.robivan.binlist.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.robivan.binlist.databinding.ItemRecyclerBinding
import com.robivan.binlist.domain.model.DetailsCard
import org.apache.commons.lang3.StringEscapeUtils

class CardsRVAdapter(private val listener: ItemClickListener) :
    RecyclerView.Adapter<CardsRVAdapter.CardViewHolder>() {

    private val cardsDiffer = AsyncListDiffer(this, CardListDiffUtilCallback())
    fun submitList(list: List<DetailsCard>) = cardsDiffer.submitList(list)

    inner class CardViewHolder(private val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card: DetailsCard) = with(binding) {
            requestNumber.text = card.number
            schema.text = card.scheme.orEmpty()
            currencyText.text = card.currency ?: "-"
            countryText.text = card.countryName ?: "-"
            countryEmoji.text = StringEscapeUtils.unescapeJava(card.countryEmoji)
            timestamp.text = card.timestamp
            itemView.setOnClickListener { listener.onItemClick(card) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerBinding.inflate(inflater, parent, false)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int = cardsDiffer.currentList.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardsDiffer.currentList[position]
        holder.bind(card)
    }
}