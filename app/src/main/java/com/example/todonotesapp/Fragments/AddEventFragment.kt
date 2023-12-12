package com.example.todonotesapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todonotesapp.R
import com.example.todonotesapp.databinding.FragmentAddEventBinding
import com.example.todonotesapp.databinding.FragmentHomeBinding
import com.example.todonotesapp.utils.TaskData
import com.google.android.material.textfield.TextInputEditText


class AddEventFragment :  DialogFragment() {

    private lateinit var binding : FragmentAddEventBinding
    private lateinit var listener : DialogNextBtnClickListener
    private var taskData : TaskData?= null


    fun setListener(listener:DialogNextBtnClickListener){
        this.listener = listener
    }


    companion object{
        const val TAG = "AddEventFragment"

        @JvmStatic
        fun newInstance(taskId:String , task:String) = AddEventFragment().apply {
            arguments = Bundle().apply {
                putString("taskId",taskId)
                putString("task",task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddEventBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments != null){
            taskData = TaskData(arguments?.getString("taskId").toString()
                ,arguments?.getString("task").toString())

                binding.TaskText.setText(taskData?.task)
        }
        binding.saveTodoBtn.setOnClickListener{
            val todoTask = binding.TaskText.text.toString()

            if (todoTask.isNotEmpty()){
                if(taskData ==null){
                    listener.onSaveTask(todoTask,binding.TaskText)
                }else{
                    taskData?.task = todoTask
                    listener.onUpdateTask(taskData!! , binding.TaskText)
                }

            }
            else{
                Toast.makeText(context,"Please type some notes/tasks",Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener{
            dismiss()
        }


    }


    interface DialogNextBtnClickListener{
        fun onSaveTask(task:String,TaskText:TextInputEditText)
        fun onUpdateTask(taskData: TaskData,TaskText: TextInputEditText)
    }





}