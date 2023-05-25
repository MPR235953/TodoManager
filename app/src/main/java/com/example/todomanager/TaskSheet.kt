package com.example.todomanager

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todomanager.databinding.FragmentTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TaskSheet(context: Context, var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTaskSheetBinding
    private var dueDateTime: LocalDateTime? = null
    private var isDone: Int = 0
    private var isNotification: Int = 0
    private var isAttachment: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
        return dialog
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // decide what will be seen when add button was clicked
        if(taskItem != null){
            // set styles
            binding.ibtnIsDone.setImageResource(taskItem!!.imageResourceForIsDoneButton())
            binding.ibtnIsDone.setColorFilter(taskItem!!.imageColorForIsDoneButton(requireContext()))
            binding.ibtnIsNotification.setColorFilter(taskItem!!.imageColorForIsNotificationButton(requireContext()))
            binding.ibtnIsAttachment.setColorFilter(taskItem!!.imageColorForIsAttachmentButton(requireContext()))

            // set proper view
            binding.tvTaskTitle.text = "Edit Task"
            binding.btnDelete.visibility = View.VISIBLE
            binding.llDateTimes.visibility = View.VISIBLE

            // enable editting form fields
            val editable = Editable.Factory.getInstance()
            binding.tieName.text = editable.newEditable(taskItem!!.name)
            binding.tieDescription.text = editable.newEditable(taskItem!!.description)
            binding.tieCategory.text = editable.newEditable(taskItem!!.category)
            binding.tvCreatedDateTime.text = DataTimeConverter.dateTime2String(taskItem!!.createDateTime)
            binding.tvDueDateTime.text = DataTimeConverter.dateTime2String(taskItem!!.dueDateTime!!)

            // get data from clicked task
            if(taskItem!!.dueDateTime != null){
                dueDateTime = taskItem!!.dueDateTime!!
            }
            this.isDone = taskItem!!.isDone
            this.isNotification = taskItem!!.isNotification
            this.isAttachment = taskItem!!.isAttachment
        }
        else{
            // set proper view
            binding.tvTaskTitle.text = "New Task"
            binding.btnDelete.visibility = View.GONE
            //binding.llDateTimes.visibility = View.GONE
            binding.ibtnIsDone.visibility = View.GONE
            //binding.llAtachments.visibility = View.GONE
        }

        // top button listeners
        binding.ibtnDateTime.setOnClickListener{ openDateTimePicker() }
        binding.ibtnIsDone.setOnClickListener{ showChangedTaskItemState() }
        binding.ibtnIsNotification.setOnClickListener{ showChangedTaskItemNotification() }
        binding.ibtnIsAttachment.setOnClickListener{ showChangedTaskItemAttachment() }  // change to attach method

        // bottom button listeners
        binding.btnSave.setOnClickListener{ saveAction() }
        binding.btnDelete.setOnClickListener{ deleteAction() }
        binding.btnClose.setOnClickListener{ dismiss() }
    }

    private fun showChangedDateTime(){
        binding.tvDueDateTime.text = DataTimeConverter.dateTime2String(this.dueDateTime!!)
    }
    private fun showChangedTaskItemState(){
        this.isDone = if(this.isDone == 1) 0 else 1
        binding.ibtnIsDone.setImageResource(TaskItem.previewImageResource(this.isDone))
        binding.ibtnIsDone.setColorFilter(TaskItem.previewImageColor(requireContext(), this.isDone))
    }
    private fun showChangedTaskItemNotification(){
        this.isNotification = if(this.isNotification == 1) 0 else 1
        binding.ibtnIsNotification.setColorFilter(TaskItem.previewImageColor(requireContext(), this.isNotification))
    }
    private fun showChangedTaskItemAttachment(){
        this.isAttachment = if(this.isAttachment == 1) 0 else 1
        binding.ibtnIsAttachment.setColorFilter(TaskItem.previewImageColor(requireContext(), this.isAttachment))
    }


    private fun openDateTimePicker(){
        if(this.dueDateTime == null) this.dueDateTime = LocalDateTime.now()

        // Time Picker stuff
        val timeListener = TimePickerDialog.OnTimeSetListener{ _, h, m ->
            this.dueDateTime = this.dueDateTime!!.toLocalDate().atTime(LocalTime.of(h, m))
            showChangedDateTime()
        }
        val timeDialog = TimePickerDialog(activity as Context, timeListener, this.dueDateTime!!.hour, this.dueDateTime!!.minute, true)
        timeDialog.setTitle("Task Due Time")
        timeDialog.show()

        // Date Picker stuff
        val dateListener = DatePickerDialog.OnDateSetListener{ _, y, m, d ->
            this.dueDateTime = this.dueDateTime!!.toLocalTime().atDate(LocalDate.of(y, m, d))
            showChangedDateTime()
            binding.ibtnDateTime.setColorFilter(TaskItem.previewImageColor(requireContext(), active=1))
        }
        val dateDialog = DatePickerDialog(activity as Context, dateListener, this.dueDateTime!!.year, this.dueDateTime!!.month.value, this.dueDateTime!!.dayOfMonth)
        dateDialog.setTitle("Task Due Date")
        dateDialog.show()
    }

    private fun saveAction(){

        // get data from layout
        val name = binding.tieName.text.toString()
        val description = binding.tieDescription.text.toString()
        val category = binding.tieCategory.text.toString()

        // create new task item or update existing
        if(taskItem == null){
            val newTask = TaskItem(name, description, this.dueDateTime, category, isDone=this.isDone, isNotification=this.isNotification, isAttachment=this.isAttachment)
            //TaskViewModel.addTaskItem(newTask)
            MainActivity.sqLiteManager?.addTaskItem(newTask)
        }
        else{
            MainActivity.sqLiteManager?.updateTaskItem(taskItem!!.id, name, description, this.dueDateTime, category, this.isDone, this.isNotification, this.isAttachment)
            //TaskViewModel.updateTaskItem(taskItem!!.id, name, description, this.dueDateTime, category)
        }
        MainActivity.sqLiteManager?.loadToLocalMemory()

        // clear data on view
        binding.tieName.setText("")
        binding.tieDescription.setText("")
        binding.tieCategory.setText("")
        dismiss()
    }

    private fun deleteAction(){
        MainActivity.sqLiteManager?.deleteTaskItem(taskItem!!.id)
        MainActivity.sqLiteManager?.loadToLocalMemory()
        dismiss()
    }

}