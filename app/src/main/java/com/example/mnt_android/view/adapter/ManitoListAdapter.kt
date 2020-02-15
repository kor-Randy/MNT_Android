package com.example.mnt_android.view.adapter

import android.view.View
import com.example.mnt_android.R
import com.example.mnt_android.base.BaseAdapter
import com.example.mnt_android.view.viewholder.ManitoListViewHolder

private val manitoList = arrayListOf(
    arrayOf(
        "진유진",
        "https://pbs.twimg.com/profile_images/1007457406031355904/zzE5Z1Zg_400x400.jpg",
        "지현우",
        "data:image/jpeg;base64,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"
        ),
    arrayOf(
        "지현우",
        "data:image/jpeg;base64,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",
        "진유진",
        "https://pbs.twimg.com/profile_images/1007457406031355904/zzE5Z1Zg_400x400.jpg"
    )
)

class ManitoListAdapter(private val isManager: Boolean) : BaseAdapter<Array<String>>() {
    override val layoutId: Int
        get() = R.layout.item_manito

    init {
        setList(manitoList)
    }

    override fun viewHolder(layout: Int, view: View) = ManitoListViewHolder(view, isManager)

}