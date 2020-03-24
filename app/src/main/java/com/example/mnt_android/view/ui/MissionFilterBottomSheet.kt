package com.example.mnt_android.view.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mnt_android.R
import com.example.mnt_android.bus.*
import com.example.mnt_android.service.model.Applicant
import com.example.mnt_android.vo.MissionVO
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_mission_filter.*


class MissionFilterBottomSheet(private val userId: String) : BottomSheetDialogFragment() {
    var missionList: ArrayList<MissionVO> = arrayListOf()
    var participantList: ArrayList<Applicant> = arrayListOf()

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_mission_filter, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEventListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mission_filter, container, false)
    }

    private fun setEventListener() {
        order_mission_list_btn.setOnClickListener {
            sendFilteringEvent(arrayOf(MISSION_LIST_ALL))
            dismiss()
        }
        mission_to_me_btn.setOnClickListener {
            sendFilteringEvent(arrayOf(MISSION_LIST_TO_ME, userId))
            dismiss()
        }
        mission_from_me_btn.setOnClickListener {
            sendFilteringEvent(arrayOf(MISSION_LIST_FROM_ME, userId))
            dismiss()
        }
        view_by_mission_btn.setOnClickListener {
            view_by_mission_layout.run {
                if (visibility == View.VISIBLE) {
                    removeAllViews()
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    missionList.forEach {
                        addView(getMissionItem(it))
                    }
                }
            }
        }
        view_by_participant_btn.setOnClickListener {
            view_by_participant_layout.run {
                if (visibility == View.VISIBLE) {
                    removeAllViews()
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    missionList.forEach {
                        addView(getUserItem(userId, 1, "이름암거나"))
                        // TODO : 참여자 리스트로 수정 필요
                    }
                }
            }
        }
        close_btn.setOnClickListener { dismiss() }
    }

    private fun getMissionItem(mission: MissionVO): LinearLayout {
        val inflater = LayoutInflater.from(context)
        return (inflater.inflate(R.layout.item_filter_list, null, false) as LinearLayout).apply {
            findViewById<ImageView>(R.id.iv).visibility = View.GONE
            findViewById<TextView>(R.id.tv).text = mission.missionName
            setOnClickListener {
                sendFilteringEvent(
                    arrayOf(
                        MISSION_LIST_MISSION_TYPE,
                        mission.missionId.toString()
                    )
                )
                dismiss()
            }
        }
    }

    private fun getUserItem(userId: String, imgId: Int, title: String = ""): LinearLayout {
        val inflater = LayoutInflater.from(context)
        return (inflater.inflate(R.layout.item_filter_list, null, false) as LinearLayout).apply {
            val imgRes = resources.getIdentifier(
                "img_profile_face_${"%02d".format(imgId)}",
                "drawable",
                context?.packageName
            )
            findViewById<ImageView>(R.id.iv).setImageResource(imgRes)
            findViewById<TextView>(R.id.tv).text = title
            setOnClickListener {
                sendFilteringEvent(
                    arrayOf(
                        MISSION_LIST_PARTICIPANT,
                        userId
                    )
                )
                dismiss()
            }
        }
    }
}