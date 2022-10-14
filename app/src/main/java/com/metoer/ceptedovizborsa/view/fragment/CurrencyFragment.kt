package com.metoer.ceptedovizborsa.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.databinding.FragmentCurrencyBinding

class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrencyBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }
}