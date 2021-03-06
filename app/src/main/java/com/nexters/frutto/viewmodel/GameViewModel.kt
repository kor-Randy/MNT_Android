package com.nexters.frutto.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.nexters.frutto.base.BaseViewModel
import com.nexters.frutto.extension.isTrue
import com.nexters.frutto.service.model.*
import com.nexters.frutto.service.repository.DBRepository
import com.nexters.frutto.util.SUCCESS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GameViewModel(private val repository: DBRepository) : BaseViewModel()
{
    var title : String=""
    var doneUserMissions = MutableLiveData<ArrayList<UserMissionResponse>>()
    var notDoneUserMissions = MutableLiveData<ArrayList<UserMissionResponse>>()
    var missionResponses = ArrayList<MissionResponse>()
    var missionManager = MutableLiveData<ArrayList<Pair<String,String>>>()
    var changeManagerList = MutableLiveData<Boolean>()
    var listIsDone = MutableLiveData<Boolean>(false)

    fun getUserMission(userId : String, roomId : Long)
    {
        listIsDone.value=false
        addDisposable(
            repository.getUserMission(userId,roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.run {
                        if(apiStatus.httpStatus == SUCCESS)
                        {
                            Log.d("wlgusdnzzz","미션 Size : "+this.data.size.toString())
                            val done = arrayListOf<UserMissionResponse>()
                            val notDone = arrayListOf<UserMissionResponse>()

                            this.data.forEach { mission ->
                                mission.userMission.userDone?.let { isDone ->
                                    if (isDone.isTrue) done.add(mission)
                                } ?: { notDone.add(mission) }()
                            }

                            doneUserMissions.value = done
                            notDoneUserMissions.value = notDone
                        }
                        listIsDone.value=true
                    }
                },{t ->
                    Log.d("wlgusdnzzz",t.toString())
                })

        )
    }

    fun groupByMission(roomId : Long)
    {
        changeManagerList.value=false
        missionManager.value?.clear()
        addDisposable(repository.groupByMission(roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({t ->
                missionResponses = ArrayList(t.missionResponses)
                var missionList = ArrayList<Pair<String,String>>()
                for(data in missionResponses)
                {
                    var total = data.userMissions.size
                    var done = 0
                    for(userData in data.userMissions)
                    {
                        if(userData.userMission.userDone==1)
                            done++
                    }
                    missionList.add(Pair(data.missionName,"$done/$total"))
                }
                missionManager.value = missionList
                changeManagerList.value=true
            },
                {

                }))
    }

}