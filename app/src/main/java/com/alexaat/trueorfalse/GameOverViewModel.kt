package com.alexaat.trueorfalse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameOverViewModel(finalScore:Int): ViewModel(){

    private var _finalScore = MutableLiveData<Int>()
    val finalScore:LiveData<Int>
        get() = _finalScore

    init {
        _finalScore.value = finalScore
    }





}