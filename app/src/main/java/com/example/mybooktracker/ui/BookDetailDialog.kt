package com.example.mybooktracker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.mybooktracker.databinding.DialogBookDetailBinding
import com.example.mybooktracker.model.Book

class BookDetailDialog(
    private val book: Book,
    private val onEdit: (Book) -> Unit,
    private val onDelete: (Book) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogBookDetailBinding.inflate(LayoutInflater.from(context))
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = book.author
        binding.tvTotalPages.text = "${book.totalPages} 페이지"
        binding.tvCurrentPage.text = "${book.currentPage} 페이지"
        binding.tvMemo.text = book.memo
        binding.tvStartDate.text = book.startDate
        binding.tvEndDate.text = book.endDate.ifEmpty { "미입력" }

        binding.btnEdit.setOnClickListener {
            onEdit(book)
            dismiss()
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("삭제 확인")
                .setMessage("정말 이 책을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    onDelete(book)
                    dismiss()
                }
                .setNegativeButton("취소", null)
                .show()
        }


        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }
}