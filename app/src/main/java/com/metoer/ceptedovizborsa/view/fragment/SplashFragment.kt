package com.metoer.ceptedovizborsa.view.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.FragmentSplashBinding
import com.metoer.ceptedovizborsa.view.activity.MainActivity
import com.metoer.ceptedovizborsa.viewmodel.fragment.SplashViewModel

class SplashFragment : Fragment() {
    var _binding : FragmentSplashBinding?  = null
    val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding!!.root
    }


    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
//            Toast.makeText(requireContext(), "fsdf", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(context, MainActivity::class.java))
            findNavController().navigate(R.id.onBoardingFragment)
        },1500)
    }
}