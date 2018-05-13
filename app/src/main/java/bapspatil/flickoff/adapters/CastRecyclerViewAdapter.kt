package bapspatil.flickoff.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import bapspatil.flickoff.R
import bapspatil.flickoff.model.Cast
import bapspatil.flickoff.network.RetrofitAPI
import bapspatil.flickoff.utils.GlideApp
import kotlinx.android.synthetic.main.rv_cast.view.*

/**
 * Created by bapspatil
 */

class CastRecyclerViewAdapter(private val mContext: Context, private val mCastList: ArrayList<Cast>, private val mActorClickHandler: OnActorClickHandler) : RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.rv_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.castTextView.text = mCastList[position].name
        GlideApp.with(mContext)
                .load(RetrofitAPI.POSTER_BASE_URL + mCastList[position].profilePath)
                .placeholder(R.drawable.cast_placeholder)
                .error(R.drawable.cast_placeholder)
                .fallback(R.drawable.cast_placeholder)
                .into(holder.castImageView)
    }

    override fun getItemCount(): Int {
        return mCastList.size
    }

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val castTextView: TextView = itemView.castTextView
        val castImageView: ImageView = itemView.castImageView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mActorClickHandler.onActorClicked(castTextView.text.toString())
        }
    }

    interface OnActorClickHandler {
        fun onActorClicked(actorName: String)
    }

    fun clearItems() {
        mCastList.clear()
    }

    fun addItems(castList: ArrayList<Cast>) {
        mCastList.addAll(castList)
        notifyDataSetChanged()
    }
}
