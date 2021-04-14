package com.example.currencyconverter.ui.convertion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentConvertCurrencyBinding
import com.example.currencyconverter.utils.DialogFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment() {

    private val viewModel: ConvertCurrencyViewModel by viewModels()

    private lateinit var binding: FragmentConvertCurrencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
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
        binding.sourceCurrencyEditText.setText("1", TextView.BufferType.NORMAL)
    }

    private fun initObservers() {
        viewModel.dialog.observe(viewLifecycleOwner) {
            DialogFactory.showDialog(requireContext(), it)
        }
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

        binding.sourceCurrencyEditText.doAfterTextChanged { editable ->
            if (editable != null) {
                viewModel.onSourceInputTextChanged(editable.toString())
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConvertCurrencyFragment()
    }
}