package com.nexters.frutto.view.viewholder

import android.content.Intent
import android.view.View
import com.nexters.frutto.base.BaseViewHolder
import com.nexters.frutto.service.model.UserMissionResponse
import com.nexters.frutto.view.ui.DoMissionActivity
import kotlinx.android.synthetic.main.item_mission_notdone_applicant.view.*

class NotDoneMissionListViewHolder(view: View) : BaseViewHolder(view) {
    override fun onBind(position: Int, data: Any) {
        val content = data as UserMissionResponse
        content.run {
            itemView.run {
                tv_mission_name_not_done_applicant.text = content.missionName
                not_done_layout.setOnClickListener {

                    val intent = Intent(context,DoMissionActivity::class.java)
                    intent.putExtra("nowUserMission",content)
                   context.startActivity(intent)

                }
            }
        }
    }
}