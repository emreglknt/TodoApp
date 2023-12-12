package com.example.todonotesapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todonotesapp.R
import com.example.todonotesapp.databinding.FragmentRegisterBinding
import com.example.todonotesapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit  var navController: NavController
    private lateinit  var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return  binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        LoginEvents()

    }

    private fun init (view:View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }


    private fun LoginEvents() {


        binding.registerText.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_registerFragment)
        }


        binding.loginButton.setOnClickListener{
            Log.d("ButtonClicked", "Button clicked!")
            val email = binding.RegEmail.text.toString().trim()
            val pass = binding.RegPassword.text.toString().trim()

            if(email.isNotEmpty() && pass.isNotEmpty()) {


                binding.progressBar.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener( {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_SHORT)
                            .show()
                        //navcontroller kullanarak navgraptan directionları alarak
                        // nerden nereye geciş yapacağımza karar veriyoruz
                        navController.navigate(R.id.action_signInFragment_to_homeFragment)
                    }
                    else {
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                })

            } else {
                Toast.makeText(context, "Email or password can not be empty!", Toast.LENGTH_SHORT)
                    .show()
            }


        }




    }




}