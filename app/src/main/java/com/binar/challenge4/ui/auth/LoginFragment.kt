package com.binar.challenge4.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.binar.challenge4.MainActivity
import com.binar.challenge4.database.MyDatabase
import com.binar.challenge4.databinding.FragmentLoginBinding
import com.binar.challenge4.utils.AESEncyption
import com.binar.challenge4.utils.ValidationForm.isValid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private var myDatabase: MyDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        myDatabase = MyDatabase.getInstance(requireContext())
        val authRepository = AuthRepository(requireContext())
        val sharedPreference = requireContext()
            .getSharedPreferences(MainActivity.SHARED_FILE, Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {

            if (binding.etEmail.isValid() and binding.etPassword.isValid()){
                val email = binding.etEmail.text.toString()
                val rawPassword = binding.etPassword.text.toString()
                val password = AESEncyption.encrypt(rawPassword).toString()


                lifecycleScope.launch(Dispatchers.IO) {
//                    val isLogin = myDatabase?.userDao()?.login(email, password)
                    val isLogin = authRepository.login(email, password)

                    activity?.runOnUiThread {
                        if (isLogin == null){
                            Toast.makeText(context, "Pastikan email dan password benar", Toast.LENGTH_SHORT).show()
                        }else{
                            val editor = sharedPreference.edit()
                            editor.putString("islogin",email)
                            editor.putString("name",isLogin.name)
                            editor.apply()
                            val action = LoginFragmentDirections
                                .actionLoginFragmentToListScheduleFragment()
                            it.findNavController().navigate(action)

                        }
                    }

                }
            }


        }

        binding.btnRegister.setOnClickListener {
            val action = LoginFragmentDirections
                .actionLoginFragmentToRegisterFragment()
            it.findNavController().navigate(action)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}