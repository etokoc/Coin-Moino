package com.metoer.ceptedovizborsa.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.FragmentSplashBinding
import com.metoer.ceptedovizborsa.util.Constants
import com.metoer.ceptedovizborsa.util.FirebaseUtil
import com.metoer.ceptedovizborsa.util.SharedPrefencesUtil
import com.metoer.ceptedovizborsa.view.activity.MainActivity

class SplashFragment : Fragment() {
    var _binding: FragmentSplashBinding? = null
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
        val firebaseUtil = FirebaseUtil()
        firebaseUtil.initializeFirebase()
        Handler().postDelayed({
            val local = SharedPrefencesUtil(requireContext())
            firebaseUtil.readData(Constants.FIREBASE_COLLECTION_NAME)
                .observe(this) { isForcedUpdate ->
                    if (isForcedUpdate) {
                        Toast.makeText(requireContext(), "zorunlu güncelleme", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (local.getLocal(Constants.IS_FIRST_OPEN_APP, Boolean) == true) {
                            startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            findNavController().navigate(R.id.onBoardingFragment)
                        }
                    }
                }
        }, 1500)
    }
}