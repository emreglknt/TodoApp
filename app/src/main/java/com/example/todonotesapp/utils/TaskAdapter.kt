package com.example.todonotesapp.utils

import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todonotesapp.databinding.FragmentAddEventBinding
import com.example.todonotesapp.databinding.TaskItemBinding

class TaskAdapter(private val list: MutableList<TaskData>):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var listener : ToDoAdapterClickInterface?=null

    fun setListener(listener:ToDoAdapterClickInterface){
        this.listener = listener
    }

    inner class TaskViewHolder(val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task
                binding.deleteTask.setOnClickListener{
                    listener?.onDeleteButtonClicked(this)
                }

                binding.editTask.setOnClickListener{
                    listener?.onEditButtonClicked(this)
                }

            }
        }
    }


    interface ToDoAdapterClickInterface{
        fun onDeleteButtonClicked(taskData: TaskData)
        fun onEditButtonClicked(taskData: TaskData)
    }




}