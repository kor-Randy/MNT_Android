package com.nexters.frutto.view.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nexters.frutto.base.BaseViewHolder
import com.nexters.frutto.binding.setFaceProfileSrc
import com.nexters.frutto.binding.setFruitChatProfileSrc
import com.nexters.frutto.extension.checkUploadDate
import com.nexters.frutto.service.model.UserMissionResponse
import com.nexters.frutto.util.getFruttoData
import com.nexters.frutto.view.ui.MissionDetailActivity
import kotlinx.android.synthetic.main.item_content.view.*

class ContentListViewHolder(view: View, private val isPublic: Boolean) : BaseViewHolder(view) {
    override fun onBind(position: Int, data: Any) {
        val content = data as UserMissionResponse
        content.run {
            itemView.run {
                if (userMission.missionImg != null) {
                    image_iv.visibility = View.VISIBLE
                    setContentImg(context, userMission.missionImg, image_iv)
                } else {
                    image_iv.visibility = View.GONE
                }

                if(!userMission.content.isNullOrEmpty()) {
                    content_tv.visibility = View.VISIBLE
                    content_tv.text = userMission.content
                }  else {
                    content_tv.visibility = View.GONE
                }

                setFruitChatProfileSrc(naeto_iv, userFruttoId)
                setFaceProfileSrc(nito_iv, manitto?.fruttoId)

                naeto_tv.text = if(isPublic) userMission.user.name else getFruttoData(context, userFruttoId)?.koreanNickName
                nito_tv.text = manitto?.name
                mission_type_tv.text = missionName
                day_tv.text = userMission.userDoneTime?.checkUploadDate
                setOnClickListener {
                    val intent = Intent(context, MissionDetailActivity::class.java)
                    intent.putExtra(MissionDetailActivity.TAG_MISSION, content)
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun setContentImg(context: Context, url: String?, iv: ImageView) {
        Glide.with(context)
            .load(url)
            .into(iv)
    }
}