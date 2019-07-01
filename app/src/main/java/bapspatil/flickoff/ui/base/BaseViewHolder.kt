package bapspatil.flickoff.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)
}
