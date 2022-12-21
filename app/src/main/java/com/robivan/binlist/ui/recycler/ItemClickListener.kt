package com.robivan.binlist.ui.recycler

import com.robivan.binlist.domain.model.DetailsCard

interface ItemClickListener {
    fun onItemClick(card: DetailsCard)
}