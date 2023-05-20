package com.example.todomanager

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanager.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener):
RecyclerView.ViewHolder(binding.root) {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/DD")

    fun bindTaskItem(taskItem: TaskItem){
        binding.tvName.text = taskItem.name  // set task name

        // set strike on text when task was done
        if(!taskItem.isTodo){
            binding.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvDueDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvDueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        // set appropriate img and color on done button when task was done
        binding.ibtnComplete.setImageResource(taskItem.imageResource())
        binding.ibtnComplete.setColorFilter(taskItem.imageColor(context))

        binding.ibtnComplete.setOnClickListener{
            clickListener.completeTaskItem(taskItem)
        }

        binding.cvTaskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }

        // if there is due date set it in appropriate format
        if(taskItem.dueDateTime != null) {
            binding.tvDueDate.text = taskItem.dueDateTime!!.format(DateTimeFormatter.ofPattern("yy/MM/dd"))
            binding.tvDueTime.text = taskItem.dueDateTime!!.format(DateTimeFormatter.ofPattern("hh:mm"))
        }
        else{
            binding.tvDueDate.text = "--/--/--"
            binding.tvDueTime.text = "--:--"
        }
    }
}