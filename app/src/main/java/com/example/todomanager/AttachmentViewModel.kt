package com.example.todomanager

import androidx.lifecycle.MutableLiveData

class AttachmentViewModel {
    companion object {
        var attachmentItems = MutableLiveData<MutableList<AttachmentItem>>()  // list of tasks

        init{
            attachmentItems.value = mutableListOf()
        }

        fun addAttachmentItem(newAttachment: AttachmentItem){
            val list = attachmentItems.value
            list!!.add(newAttachment)
            attachmentItems.postValue(list)
        }

        fun delAttachmentItem(toDel: AttachmentItem){
            val list = attachmentItems.value
            list!!.remove(toDel)
            attachmentItems.postValue(list)
        }

    }
}