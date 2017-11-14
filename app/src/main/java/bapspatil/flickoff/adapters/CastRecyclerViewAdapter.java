package bapspatil.flickoff.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bapspatil.flickoff.R;
import bapspatil.flickoff.model.Cast;
import bapspatil.flickoff.network.RetrofitAPI;
import bapspatil.flickoff.utils.GlideApp;

/**
 * Created by bapspatil
 */

public class CastRecyclerViewAdapter extends RecyclerView.Adapter<CastRecyclerViewAdapter.CastViewHolder> {
    private ArrayList<Cast> mCastList;
    private Context mContext;
    private OnActorClickHandler mActorClickHandler;

    public CastRecyclerViewAdapter(Context context, ArrayList<Cast> cast, OnActorClickHandler onActorClickHandler) {
        this.mContext = context;
        this.mCastList = cast;
        this.mActorClickHandler = onActorClickHandler;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_cast, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        holder.mCastTextView.setText(mCastList.get(position).getName());
        GlideApp.with(mContext)
                .load(RetrofitAPI.POSTER_BASE_URL + mCastList.get(position).getProfilePath())
                .placeholder(R.drawable.cast_placeholder)
                .error(R.drawable.cast_placeholder)
                .fallback(R.drawable.cast_placeholder)
                .into(holder.mCastImageView);
    }

    @Override
    public int getItemCount() {
        return mCastList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mCastTextView;
        public ImageView mCastImageView;

        public CastViewHolder(View itemView) {
            super(itemView);
            mCastImageView = itemView.findViewById(R.id.cast_iv);
            mCastTextView = itemView.findViewById(R.id.cast_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mActorClickHandler.onActorClicked(mCastTextView.getText().toString());
        }
    }

    public interface OnActorClickHandler {
        void onActorClicked(String actorName);
    }
}
