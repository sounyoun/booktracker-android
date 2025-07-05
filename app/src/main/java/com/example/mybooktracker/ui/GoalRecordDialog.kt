package com.example.mybooktracker.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.mybooktracker.databinding.DialogGoalRecordBinding
import com.example.mybooktracker.model.GoalRecord

class GoalRecordDialog(private val recordList: List<GoalRecord>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogGoalRecordBinding.inflate(LayoutInflater.from(requireContext()))

        val text = if (recordList.isEmpty()) "기록 없음"
        else recordList.joinToString("\n") {
            "${it.achievedDate}     ${it.goalCount}권 달성"
        }
        binding.tvRecords.text = text

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}
