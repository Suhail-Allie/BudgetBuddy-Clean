package com.example.budgetbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val onItemClick: (Expense) -> Unit,
    private val onDeleteClick: (Expense) -> Unit,
    private val onPhotoClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView    = itemView.findViewById(R.id.tvExpenseTitle)
        private val tvAmount: TextView   = itemView.findViewById(R.id.tvExpenseAmount)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvExpenseCategory)
        private val tvDate: TextView     = itemView.findViewById(R.id.tvExpenseDate)
        private val btnViewPhoto: ImageButton  = itemView.findViewById(R.id.btnViewPhoto)
        private val btnDelete: ImageButton     = itemView.findViewById(R.id.btnDeleteExpense)

        fun bind(expense: Expense) {
            tvTitle.text    = expense.title
            tvAmount.text   = "R${String.format("%.2f", expense.amount)}"
            tvCategory.text = expense.category
            tvDate.text     = dateFormat.format(expense.date)

            btnViewPhoto.visibility = if (expense.receiptPhotoPath != null) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onItemClick(expense) }
            btnViewPhoto.setOnClickListener { onPhotoClick(expense) }
            btnDelete.setOnClickListener { onDeleteClick(expense) }
        }
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}
