package com.hhk.formstatetest

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.hhk.formstatetest.databinding.FragmentCreateBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.days

class FragmentCreate:Fragment(R.layout.fragment_create) {

    private var _binding:FragmentCreateBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: AccCreateViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCreateBinding.bind(view)

        val datePicker by lazy {
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Your Date of Birth ")
                .build()
        }

        viewModel = ViewModelProvider(this).get(AccCreateViewModel::class.java)

        binding.maleOption.setOnClickListener {
            viewModel.gender.value = GENDER.Male
        }

        binding.femaleOption.setOnClickListener {
            viewModel.gender.value = GENDER.Female
        }

        binding.firstNameField.addTextChangedListener {
            viewModel.firstName.value = it.toString().trim()
        }

        binding.lastNameField.addTextChangedListener {
            viewModel.lastName.value = it.toString().trim()
        }

        binding.mailField.addTextChangedListener {
            viewModel.email.value = it.toString().trim()
        }

        binding.countryField.addTextChangedListener {
            viewModel.country.value = it.toString().trim()
        }

        binding.nationalityField.addTextChangedListener {
            viewModel.nationality.value = it.toString().trim()
        }

        binding.dobField.setOnClickListener {
            datePicker.show(parentFragmentManager,"TAG")
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        datePicker.addOnPositiveButtonClickListener { date:Long ->

            val da = Date(date)
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val fd = formatter.format(da.time)
            viewModel.date.value = fd.replace("-","/")

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.date.collectLatest {
                    binding.dobField.setText(it)
                }
            }
        }


        val typeValue = TypedValue()
        requireContext().theme.resolveAttribute(android.R.attr.selectableItemBackground,
            typeValue,true
        )

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.gender.collectLatest {
                    when(it){
                        GENDER.Male -> {
                            binding.maleOption.background = ResourcesCompat.getDrawable(resources,R.drawable.gender_btn_shape,requireContext().theme)
                            binding.femaleOption.setBackgroundResource(typeValue.resourceId)
                        }
                        GENDER.Female -> {
                            binding.maleOption.setBackgroundResource(typeValue.resourceId)
                            binding.femaleOption.background = ResourcesCompat.getDrawable(resources,R.drawable.gender_btn_shape,requireContext().theme)
                        }
                    }
                }
            }


        }

        binding.createAccBtn.setOnClickListener {

            viewModel.onCreateAcc(
                onSuccess = {
                    findNavController().popBackStack()
                },
                onError = {

                    Snackbar.make(requireView(),it,Snackbar.LENGTH_LONG).show()
                }
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}