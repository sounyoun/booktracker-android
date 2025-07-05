package com.example.mybooktracker.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.mybooktracker.databinding.FragmentAddBookBinding
import com.example.mybooktracker.model.Book
import java.util.Calendar

class AddBookBottomSheet(
    private val onBookAdded: (Book) -> Unit,
    private val existingBook: Book? = null,
    private val onBookEdited: ((Book) -> Unit)? = null
) : BottomSheetDialogFragment() {

    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        existingBook?.let {
            binding.etTitle.setText(it.title)
            binding.etAuthor.setText(it.author)
            binding.etTotalPages.setText(it.totalPages.toString())
            binding.etCurrentPage.setText(it.currentPage.toString())
            binding.etMemo.setText(it.memo)
            binding.etStartDate.setText(it.startDate)
            binding.etEndDate.setText(it.endDate)
        }

        // 날짜 선택 리스너 설정
        val dateListener: (target: (String) -> Unit) -> DatePickerDialog.OnDateSetListener = { target ->
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val date = String.format("%04d-%02d-%02d", year, month + 1, day)
                target(date)
            }
        }

        binding.etStartDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                dateListener { selectedDate -> binding.etStartDate.setText(selectedDate) },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.etEndDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                dateListener { selectedDate -> binding.etEndDate.setText(selectedDate) },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val author = binding.etAuthor.text.toString()
            val total = binding.etTotalPages.text.toString().toIntOrNull() ?: 0
            val current = binding.etCurrentPage.text.toString().toIntOrNull() ?: 0
            val memo = binding.etMemo.text.toString()
            val start = binding.etStartDate.text.toString()
            val end = binding.etEndDate.text.toString()
            val newBook = Book(title, author, total, current, memo, start, end)
            if (existingBook == null) onBookAdded(newBook) else onBookEdited?.invoke(newBook)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
