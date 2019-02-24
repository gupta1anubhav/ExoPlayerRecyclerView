package com.example.anubhav.exoplayer;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

abstract class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mPosition: Int = 0

    protected abstract fun clear()

    open fun onBind(position: Int) {
        mPosition = position
        clear()
    }

}
