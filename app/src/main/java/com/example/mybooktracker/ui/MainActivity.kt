package com.example.mybooktracker.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybooktracker.R
import com.example.mybooktracker.databinding.ActivityMainBinding
import com.example.mybooktracker.model.Book
import com.example.mybooktracker.model.GoalRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private val bookList = mutableListOf<Book>()
    private var bookGoal = 0 // 목표 권수
    private val goalRecords = mutableListOf<GoalRecord>()
    private var goalReached = false
    private var lastAchievedGoal = 0  // ✅ 마지막으로 기록한 목표 권수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookAdapter = BookAdapter(bookList) { selectedBook ->
            val dialog = BookDetailDialog(
                selectedBook,
                onEdit = { bookToEdit ->
                    val index = bookList.indexOf(bookToEdit)
                    val editSheet = AddBookBottomSheet(
                        existingBook = bookToEdit,
                        onBookAdded = {},
                        onBookEdited = { updatedBook ->
                            bookList[index] = updatedBook
                            bookAdapter.notifyItemChanged(index)
                            updateGoalProgress()
                        }
                    )
                    editSheet.show(supportFragmentManager, "EditBookSheet")
                },
                onDelete = { deletedBook ->
                    val index = bookList.indexOf(deletedBook)
                    bookList.removeAt(index)
                    bookAdapter.notifyItemRemoved(index)
                    updateGoalProgress()
                }
            )
            dialog.show(supportFragmentManager, "BookDetailDialog")
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }

        binding.btnAddBook.setOnClickListener {
            val bottomSheet = AddBookBottomSheet(
                onBookAdded = { newBook ->
                    bookList.add(0, newBook)
                    bookAdapter.notifyItemInserted(0)
                    binding.recyclerView.scrollToPosition(0)
                    updateGoalProgress()
                }
            )
            bottomSheet.show(supportFragmentManager, "AddBookBottomSheet")
        }

        binding.btnSetGoal.setOnClickListener {
            showGoalDialog()
        }

        binding.btnViewRecords.setOnClickListener {
            val dialog = GoalRecordDialog(goalRecords)
            dialog.show(supportFragmentManager, "GoalRecordDialog")
        }

    }

    private fun showGoalDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_set_goal, null)
        val etGoal = dialogView.findViewById<EditText>(R.id.etGoalCount)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val input = etGoal.text.toString().toIntOrNull()
            if (input != null && input > 0) {
                bookGoal = input
                updateGoalProgress()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "숫자를 올바르게 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    private fun updateGoalProgress() {
        if (bookGoal <= 0) {
            binding.layoutGoalProgress.visibility = View.GONE
            return
        }

        val percent = (bookList.size.toFloat() / bookGoal * 100).toInt().coerceAtMost(100)
        binding.layoutGoalProgress.visibility = View.VISIBLE
        binding.progressBarGoal.progress = percent
        binding.tvGoalStatus.text = "${bookList.size} / $bookGoal"


        // 목표 달성 알림
        if (bookList.size >= bookGoal && bookGoal != lastAchievedGoal) {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            goalRecords.add(GoalRecord(bookGoal, date))
            lastAchievedGoal = bookGoal  // ✅ 중복 기록 방지
            AlertDialog.Builder(this)
                .setTitle("🎉 목표 달성!")
                .setMessage("목표 ${bookGoal}권 달성! $date")
                .setPositiveButton("확인", null)
                .show()
        }

    }
}
