package com.example.mnt_android.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mnt_android.R
import com.example.mnt_android.databinding.FragmentCreateroom1Binding
import com.example.mnt_android.databinding.FragmentDoMission1Binding
import com.example.mnt_android.databinding.FragmentDoMission2Binding
import com.example.mnt_android.view.dialog.CustomAlertDialog
import com.example.mnt_android.viewmodel.CreateRoomViewModel
import com.example.mnt_android.viewmodel.DoMissionViewModel
import kotlinx.android.synthetic.main.fragment_do_mission2.*
import org.jetbrains.anko.image
import org.jetbrains.anko.imageBitmap

class DoMissionFragment2 : Fragment()
{
    lateinit var doMissionViewModel: DoMissionViewModel
    lateinit var binding: FragmentDoMission2Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_do_mission2,container,false)



        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let{
            doMissionViewModel=(activity as DoMissionActivity).doMissionViewModel
            binding.doMissionActivity = activity as DoMissionActivity
            binding.doMissionViewModel = doMissionViewModel
            iv_img_mission_do_mission2.imageBitmap = doMissionViewModel.bitmap


            binding.lifecycleOwner=this

            doMissionViewModel.isSended.observe(this, Observer {
                if(it==true)
                {
                    CustomAlertDialog("니또에게 미션을 보냈습니다.","확인"){
                        activity?.finish()
                    }.show((activity as DoMissionActivity).supportFragmentManager,"SendMission")
                }
            })

        }

    }

}