package com.alexaat.trueorfalse




import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GameViewModel: ViewModel() {

    companion object{
        private const val TIME = 60000L
        private const val ONE_SECOND = 1000L

        private val GAME_OVER_BUZZ_PATTERN =  longArrayOf(0,250,250,250,250,1000)
        private val LAST_CHANCE_BUZZ_PATTEN =  longArrayOf(0,500)

    }

    // list of questions
    private lateinit var list:MutableList<String>

    // encapsulated question LiveData
    private val _currentQuestion = MutableLiveData<String>()
    val currentQuestion: LiveData<String>
        get() = _currentQuestion

    // encapsulated question index LiveData
    private val _questionIndex = MutableLiveData<Int>()
    val questionIndex: LiveData<Int>
        get() = _questionIndex

    // encapsulated score LiveData
    private val _currentScore = MutableLiveData<Int>()
    val currentScore: LiveData<Int>
        get() = _currentScore

    // encapsulated navigate to gameOver fragment LiveData
    private val _isGameOver = MutableLiveData<Boolean>()
    val isGameOver: LiveData<Boolean>
        get() = _isGameOver


    // encapsulated timer LiveData
    private val _timer = MutableLiveData<String>()
    val timer: LiveData<String>
        get() = _timer


    // encapsulated buzzerPatten LiveData
    private val _buzzerPatten = MutableLiveData<LongArray>()
    val buzzerPatten: LiveData<LongArray>
        get() = _buzzerPatten

    private lateinit var currentAnswer: String

    init{
            setTimer()
            _questionIndex.value = 0
            _isGameOver.value = false
            _currentScore.value = 0

            setQuestionsList()
            getQuestion()
    }

    private fun getQuestion(){

        if(list.size<=0){
            setQuestionsList()
        }
        _currentQuestion.value = list[0].substringBefore("<")
        currentAnswer = list[0].substringAfter("<").substringBefore(">")
        list.removeAt(0)
        _questionIndex.value = _questionIndex.value?.plus(1)
    }



    fun onTruePressed(){
        if(currentAnswer=="true"){
        _currentScore.value =   _currentScore.value?.plus(10)
        }else{
            _currentScore.value =   _currentScore.value?.minus(10)
        }
        getQuestion()
    }
    fun onFalsePressed(){
        if(currentAnswer=="false"){
            _currentScore.value =   _currentScore.value?.plus(10)
        }else{
            _currentScore.value =   _currentScore.value?.minus(10)
        }
        getQuestion()
    }
    fun onSkipPressed(){
        _currentScore.value =   _currentScore.value?.minus(5)
        getQuestion()
    }

    private fun gameIsFinished(){
        _isGameOver.value = true
    }

    private fun buzz(pattern:LongArray){
        _buzzerPatten.value = pattern
    }
    fun buzzComplete(){
        _buzzerPatten.value = null
    }

    fun navigationFinished(){
        _isGameOver.value = false
    }

    private fun setTimer(){
        val timer = object: CountDownTimer(TIME+ONE_SECOND, ONE_SECOND){
            override fun onFinish() {
            }
            override fun onTick(p0: Long) {
                if(p0<ONE_SECOND){
                    gameIsFinished()
                    buzz(GAME_OVER_BUZZ_PATTERN)
                }
               _timer.value = formatTime(p0)
            }

        }
        timer.start()
    }

    private fun setQuestionsList(){
        list = mutableListOf<String>(
            "A man from the USA consumed his 26,000th Big Mac on 11th October 2012, after eating at least one a day for forty years.<true>"
            ,"The longest distance swam underwater in one breath is 200metres.<true>"
            ,"The fastest time to eat 15 Ferrero Rocher is 1 minute 10 seconds.<false>"
            ,"Albert Einstein was awarded the Nobel Prize in Physics.<true>"
            ,"A right triangle can never be equilateral.<true>"
            ,"No bird can fly backwards.<false>"
            ,"Homer Simpson's mother is called Maria.<false>"
            ,"Sound travels much slower through water than through air.<false>"
            ,"Some types of seaweed can grow 50 centimetres in a day.<true>"
            ,"Walt Disney’s full name was Walter Elias Disney.<true>"
            ,"Twitter was founded by Jeff Bezos?<false>"
            ,"Nicolas Cage and Michael Jackson both married the same woman?<true>"
            ,"Mercury is also referred to as “The Evening Star”<false>"
            ,"The smallest bone in the human body is about the size of a grain of rice.<true>"
            ,"The can-opener was not invented until 45 years after the tin can?<true>")

        list.shuffle()
    }

    private fun formatTime(time:Long):String{
        val t = (time/ ONE_SECOND).toInt()
        if(t in(1..5)){
            buzz(LAST_CHANCE_BUZZ_PATTEN)
        }
        return DateUtils.formatElapsedTime(time/ONE_SECOND)
    }


}