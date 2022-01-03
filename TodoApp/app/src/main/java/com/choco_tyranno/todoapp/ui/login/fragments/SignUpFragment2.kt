package com.choco_tyranno.todoapp.ui.login.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.choco_tyranno.todoapp.R
import com.choco_tyranno.todoapp.databinding.FragmentSignup2Binding
import com.choco_tyranno.todoapp.ui.login.SignUpViewModel
import com.choco_tyranno.todoapp.ui.main.TAG_DEBUG
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment2 : Fragment() {
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        handler = Handler(Looper.getMainLooper())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignup2Binding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.signUpViewModel = signUpViewModel
        return binding.root
    }

}