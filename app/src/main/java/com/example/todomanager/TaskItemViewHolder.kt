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
    fun bindTaskItem(taskItem: TaskItem){
        binding.tvName.text = taskItem.name  // set task name

        binding.tvCategory.text = taskItem.category

        // set strike on text when task was done
        if(taskItem.isDone == 1){
            binding.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvDueDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvDueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvCategory.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        // set appropriate img and color on done button when task was done
        binding.ibtnComplete.setImageResource(taskItem.imageResource())
        binding.ibtnComplete.setColorFilter(taskItem.imageColor(context))

        binding.ibtnComplete.setOnClickListener{
            clickListener.changeTaskItemState(taskItem)
        }

        binding.cvTaskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }

        // if there is due date set it in appropriate format
        if(taskItem.dueDateTime != null) {
            binding.tvDueDate.text = DataTimeConverter.date2String(taskItem.dueDateTime!!)
            binding.tvDueTime.text = DataTimeConverter.time2String(taskItem.dueDateTime!!)
        }
        else{
            binding.tvDueDate.text = "--/--/--"
            binding.tvDueTime.text = "--:--"
        }
    }
}