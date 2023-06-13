package com.example.todomanager

interface AttachmentItemClickListener {
    fun viewFile(attachmentItem: AttachmentItem)
    fun delAttachment(attachmentItem: AttachmentItem)
}