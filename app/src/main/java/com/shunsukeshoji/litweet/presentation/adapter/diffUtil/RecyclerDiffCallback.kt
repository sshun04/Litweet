package com.shunsukeshoji.litweet.presentation.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.shunsukeshoji.litweet.domain.model.DataHolder

class RecyclerDiffCallback(private val old: List<DataHolder>, private val new: List<DataHolder>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] == new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition].dataId == new[newItemPosition].dataId

}