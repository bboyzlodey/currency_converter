package com.example.currencyconverter.ui.convertion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentConvertCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment() {

    private val viewModel: ConvertCurrencyViewModel by viewModels()

    private lateinit var binding: FragmentConvertCurrencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConvertCurrencyBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    private fun initObservers() {

    }

    private fun initListeners() {
        binding.swap.setOnClickListener {
            viewModel.onSwapClicked()
        }
        binding.sourceCurrency.setOnClickListener {
            viewModel.onCurrencyButtonClicked(CurrencyMode.INPUT)
        }
        binding.targetCurrency.setOnClickListener {
            viewModel.onCurrencyButtonClicked(CurrencyMode.OUTPUT)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConvertCurrencyFragment()
    }
}