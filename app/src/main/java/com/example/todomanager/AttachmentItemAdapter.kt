package com.example.todomanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanager.databinding.AttachmentItemCellBinding

class AttachmentItemAdapter(
    private val attachmentItems: List<AttachmentItem>,
    private val clickListener: AttachmentItemClickListener) :
    RecyclerView.Adapter<AttachmentItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = AttachmentItemCellBinding.inflate(from, parent, false)
        return AttachmentItemViewHolder(parent.context, binding, clickListener)
    }

    override fun onBindViewHolder(holder: AttachmentItemViewHolder, position: Int) {
        holder.bindAttachmentItem(attachmentItems[position])
    }

    override fun getItemCount(): Int = attachmentItems.size
}