package com.example.todomanager

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanager.databinding.AttachmentItemCellBinding

class AttachmentItemViewHolder(
    private val context: Context,
    private val binding: AttachmentItemCellBinding,
    private val clickListener: AttachmentItemClickListener):
RecyclerView.ViewHolder(binding.root) {
    fun bindAttachmentItem(attachmentItem: AttachmentItem){
        binding.tvPath.text = attachmentItem.path

        binding.ibtnDelAttachment.setOnClickListener{
            clickListener.delAttachment(attachmentItem)
        }

        binding.cvAttachmentCellContainer.setOnClickListener{
            clickListener.viewFile(attachmentItem)
        }
    }
}