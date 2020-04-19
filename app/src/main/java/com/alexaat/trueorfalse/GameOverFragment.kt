package com.alexaat.trueorfalse

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.alexaat.trueorfalse.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {

   var score = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentGameOverBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_game_over, container, false)

        //Using ViewModelFactory to create ViewModel and pass score into constructor
        val gameOverViewModelFactory = GameOverViewModelFactory(GameOverFragmentArgs.fromBundle(arguments!!).score)
        val viewModel = ViewModelProviders.of(this,gameOverViewModelFactory).get(GameOverViewModel::class.java)

        viewModel.finalScore.observe(viewLifecycleOwner, Observer {
            score = it
            binding.textYourScore.text = getString(R.string.score_format, score)
        })

        binding.buttonPlayAgain.setOnClickListener{
            val action = GameOverFragmentDirections.actionGameOverFragmentToGameFragment()
            findNavController().navigate(action)
        }

        //menu
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.game_over_title)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu,menu)
        setShareItemVisibility(menu.findItem(R.id.share))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareSuccess(){

        val mimeType = "text/plain"
        val text = getString(R.string.share_success, score)

        val shareIntent = ShareCompat.IntentBuilder.from(activity!!)
            .setType(mimeType)
            .setChooserTitle(getString(R.string.select_share_option))
            .setText(text)
            .createChooserIntent()
             startActivity(shareIntent)
    }

    private fun setShareItemVisibility(item: MenuItem){
        val intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        val packageManager = activity!!.packageManager
        if(intent.resolveActivity(packageManager)==null){
            item.isVisible = false
        }
    }
}
