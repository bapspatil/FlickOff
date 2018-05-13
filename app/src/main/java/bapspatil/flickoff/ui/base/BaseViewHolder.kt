package bapspatil.flickoff.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)
}
