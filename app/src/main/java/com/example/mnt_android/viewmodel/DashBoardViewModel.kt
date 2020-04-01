package com.example.mnt_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mnt_android.base.BaseViewModel
import com.example.mnt_android.service.model.DashBoard
import com.example.mnt_android.service.repository.DBRepository
import com.example.mnt_android.service.repository.PreferencesRepository
import com.example.mnt_android.util.SUCCESS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DashBoardViewModel(
    private val dbRepository: DBRepository,
    private val pr: PreferencesRepository
) : BaseViewModel() {
    companion object {
        private const val ADMIN = "admin"
        private const val USER = "user"
    }

    private val _dashBoard = MutableLiveData<DashBoard>()
    val dashBoard: LiveData<DashBoard> = _dashBoard

    fun loadDashBoard() {
        val roll = when(pr.getIsManager()) {
            true -> ADMIN
            false -> USER
        }
        addDisposable(
            dbRepository.getDashBoardData(roll, pr.getRoomId(), pr.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.run {
                        if (apiStatus.httpStatus == SUCCESS) {
                            _dashBoard.value = this.data
                        }
                    }
                }, {})
        )
    }

    fun endRoom() {
        addDisposable(
            dbRepository.endRoom(pr.getRoomId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {})
        )
    }

    fun setOnNotification(on: Boolean) {
        pr.setOnNotification(on)
    }

    fun getUserId() = pr.getUserId()
    fun getUserNm() = pr.getUserNm()
    fun getUserFruttoId() = pr.getFruttoId()
    fun getManittoNm() = pr.getManitoNm()
    fun getManitoFruttoId() = pr.getManitoFruttoId()
    fun getCheckNaeto() = pr.getCheckNaeto()
    fun getOnNotification() = pr.getOnNotification()
}