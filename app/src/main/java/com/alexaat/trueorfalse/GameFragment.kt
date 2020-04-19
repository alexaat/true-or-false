package com.alexaat.trueorfalse

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.alexaat.trueorfalse.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        val binding: FragmentGameBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_game, container, false)

        val viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        //data binding to game_fragment.xml
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setObservers(viewModel,binding)

        setListeners(viewModel,binding)

        return binding.root
    }

    private fun setObservers(viewModel:GameViewModel, binding:FragmentGameBinding){
        viewModel.currentScore.observe(viewLifecycleOwner, Observer {
            binding.textScore.text = it.toString()
        })

        viewModel.isGameOver.observe(viewLifecycleOwner, Observer {
            if(it){
                //navigate to GameOverFragment
                val action = GameFragmentDirections.actionGameFragmentToGameOverFragment(viewModel.currentScore.value?:0)
                findNavController().navigate(action)
                viewModel.navigationFinished()
            }
        })

        viewModel.questionIndex.observe(viewLifecycleOwner, Observer{
            (activity as AppCompatActivity).supportActionBar?.title  = getString(R.string.game_fragment_title,it)
        })

        viewModel.buzzerPatten.observe(viewLifecycleOwner,Observer{
            if(it!=null) {
                buzz(it)
                viewModel.buzzComplete()
            }
        })
    }

    private fun setListeners(viewModel:GameViewModel, binding:FragmentGameBinding){
        binding.buttonSkip.setOnClickListener{
            viewModel.onSkipPressed()
        }
    }

    private fun buzz(patten:LongArray){
        val vibrator = activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        vibrator.let {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                it.vibrate(VibrationEffect.createWaveform(patten,-1))
            }else{
                it.vibrate(patten,-1)

            }
        }
    }

}
