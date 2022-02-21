package com.hhk.formstatetest

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController

class FragmentMain: Fragment(R.layout.fragment_main) {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<Button>(R.id.createNewBtn)




        btn.setOnClickListener {
            val action = FragmentMainDirections.actionFragmentMainToFragmentRegister()
        findNavController().navigate(action)
        }




    }

}