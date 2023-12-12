package com.example.todonotesapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todonotesapp.R
import com.example.todonotesapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit  var navController: NavController
    private lateinit  var binding: FragmentRegisterBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }


    private fun init(view:View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents(){

        binding.loginText.setOnClickListener{
            //Alreay have account login e basılınca Intent işini yapıyor
            navController.navigate(R.id.action_registerFragment_to_signInFragment)
        }




        binding.registerButton.setOnClickListener{
            val email = binding.RegEmail.text.toString().trim()
            val pass = binding.RegPassword.text.toString().trim()
            val verifyPass= binding.RegRewritePassword.text.toString().trim()



            if(email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()){

                if(pass.equals(verifyPass)){
                    binding.progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT)
                                .show()
                            //navcontroller kullanarak navgraptan directionları alarak
                            // nerden nereye geciş yapacağımza karar veriyoruz
                            navController.navigate(R.id.action_registerFragment_to_signInFragment)
                        }
                        else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }else {
                    Toast.makeText(context, "Password does not match!", Toast.LENGTH_SHORT)
                        .show()
                }
            }else {
                Toast.makeText(context, "Empty Fields !!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}