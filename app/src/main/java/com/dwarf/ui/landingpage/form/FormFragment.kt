package com.dwarf.ui.landingpage.form

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dwarf.ui.R
import com.dwarf.ui.databinding.FragmentFormBinding
import com.google.android.material.snackbar.Snackbar

class FormFragment : Fragment() {

    private lateinit var binding : FragmentFormBinding

    private var listener: OnNameSubmittedListener? = null

    fun setNameSubmittedListener(listener: OnNameSubmittedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener(){
        binding.btnName.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            if (name.isEmpty()){
                Snackbar.make(binding.root,getString(R.string.warning_empty_name), Snackbar.LENGTH_SHORT).show()
            }else{
                listener?.onNameSubmitted(name)
            }


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FormFragment()
    }
}

interface OnNameSubmittedListener {
    fun onNameSubmitted(name: String)
}