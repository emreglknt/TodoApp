package com.example.todonotesapp.Fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todonotesapp.R
import com.example.todonotesapp.databinding.FragmentHomeBinding
import com.example.todonotesapp.utils.TaskAdapter
import com.example.todonotesapp.utils.TaskData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), AddEventFragment.DialogNextBtnClickListener,
    TaskAdapter.ToDoAdapterClickInterface {



    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding : FragmentHomeBinding
    private  var popUpFragment : AddEventFragment?=null

    private lateinit var adapter : TaskAdapter
    private lateinit var mList : MutableList<TaskData>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)
        getDataFromFirebase()
        registerEvents()


    }



    private fun init(view: View){
        navController=Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        // hangi kullanıcı hangi taskları girmiş bunu child lar ile katagorize eder
        databaseReference = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())

        binding.recyclerViewTask.setHasFixedSize(true)
        binding.recyclerViewTask.layoutManager= LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = TaskAdapter(mList)
        adapter.setListener(this)
        binding.recyclerViewTask.adapter = adapter


    }



    private fun getDataFromFirebase(){

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val task = taskSnapshot.key?.let {
                        TaskData(it,taskSnapshot.value.toString())
                    }

                    if(task!= null){
                        mList.add(task)
                    }
                }

                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

        })

    }



    private fun registerEvents(){
        binding.addButton.setOnClickListener{
            if(AddEventFragment !=null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment = AddEventFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(
                childFragmentManager,AddEventFragment.TAG
            )
        }
    }




    override fun onSaveTask(task: String, TaskText: TextInputEditText) {

        databaseReference.push().setValue(task).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context,"Task saved successfully.",Toast.LENGTH_SHORT).show()
                TaskText.text=null
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            popUpFragment!!.dismiss()
        }

    }

    override fun onUpdateTask(taskData: TaskData, TaskText: TextInputEditText) {
        val map = HashMap<String,Any>()
        map[taskData.taskId]=taskData.task
        databaseReference.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context,"Task Updated Successfully.",Toast.LENGTH_SHORT)

            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            TaskText.text=null
            popUpFragment!!.dismiss()
        }
    }


    override fun onDeleteButtonClicked(taskData: TaskData) {
        databaseReference.child(taskData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context,"Task removed successfully.",Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditButtonClicked(taskData: TaskData) {
        if(popUpFragment != null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

        popUpFragment = AddEventFragment.newInstance(taskData.taskId,taskData.task)

        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager,AddEventFragment.TAG)

    }


}