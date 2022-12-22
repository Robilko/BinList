package com.robivan.binlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robivan.binlist.domain.AppState
import com.robivan.binlist.domain.model.DetailsCard
import com.robivan.binlist.domain.repository.ILocalRepository
import com.robivan.binlist.domain.repository.IRemoteRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel(
    private val remoteRepo: IRemoteRepository,
    private val cache: ILocalRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> = _liveData

    fun getRequestsHistory() {
        _liveData.postValue(AppState.Loading)
        compositeDisposable.addAll(
            cache.getAllData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _liveData.postValue(AppState.Success(it)) },
                    { _liveData.postValue(AppState.Error(it)) }
                )
        )
    }

    fun getCardInfo(number: String) {
        val validResponse = number.substring(0..5)
        _liveData.postValue(AppState.Loading)
        compositeDisposable.addAll(
            remoteRepo.getCardInfo(validResponse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        saveResponseToCache(it)
                        _liveData.postValue(AppState.Success(it))
                    }, { _liveData.postValue(AppState.Error(it)) }
                )
        )

    }

    private fun saveResponseToCache(card: DetailsCard) {
        _liveData.postValue(AppState.Loading)
        compositeDisposable.addAll(
            cache.saveCardInfo(card)
                .subscribe()
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}