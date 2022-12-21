package com.robivan.binlist.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.robivan.binlist.domain.model.DetailsCard

class CardListDiffUtilCallback : DiffUtil.ItemCallback<DetailsCard>() {

    override fun areItemsTheSame(oldItem: DetailsCard, newItem: DetailsCard): Boolean =
        oldItem.number == newItem.number

    override fun areContentsTheSame(oldItem: DetailsCard, newItem: DetailsCard): Boolean =
        oldItem == newItem
}