package com.example.mybooktracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mybooktracker.R
import com.example.mybooktracker.model.Book

class BookAdapter(
    private val bookList: List<Book>,
    private val onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val cardView: CardView = itemView.findViewById(R.id.cardView) // ✅ 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.tvBookTitle.text = book.title
        val percent = if (book.totalPages == 0) 0 else
            (book.currentPage.toFloat() / book.totalPages * 100).toInt()
        holder.progressBar.progress = percent

        // ✅ 리스트가 최신순이므로, 색은 뒤에서부터 계산 (0이 항상 같은 색 안되도록)
        val colorList = listOf(
            R.color.block1,
            R.color.block2,
            R.color.block3,
            R.color.block4
        )
        val reversePosition = bookList.size - 1 - position
        val colorRes = colorList[reversePosition % colorList.size]
        val color = androidx.core.content.ContextCompat.getColor(holder.itemView.context, colorRes)
        holder.cardView.setCardBackgroundColor(color)

        holder.itemView.setOnClickListener { onItemClick(book) }
    }

    override fun getItemCount(): Int = bookList.size
}
