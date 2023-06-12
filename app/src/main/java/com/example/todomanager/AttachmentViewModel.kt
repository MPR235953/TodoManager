package com.example.todomanager

import androidx.lifecycle.MutableLiveData

class AttachmentViewModel {
    companion object {
        var attachmentItems = MutableLiveData<MutableList<AttachmentItem>>()  // list of tasks

        init{
            attachmentItems.value = mutableListOf()

            // Dummy content
            for (i in 1..10){
                addAttachmentItem(AttachmentItem("/path/to/file/$i"))
            }
        }

        fun addAttachmentItem(newAttachment: AttachmentItem){
            val list = attachmentItems.value
            list!!.add(newAttachment)
            attachmentItems.postValue(list)
        }

    }
}