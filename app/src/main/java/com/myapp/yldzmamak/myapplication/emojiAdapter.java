package com.myapp.yldzmamak.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by yldzmamak on 10.10.2018.
 */
public class emojiAdapter  extends RecyclerView.Adapter<emojiAdapter.ViewHolder> {
    private ArrayList<emojiItem> mValues;
    private Context mContext;
    protected ItemListener mListener;
    private static final String TAG = KisiekleActivity.class.getSimpleName();

    public emojiAdapter(Context context, ArrayList<emojiItem> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.emoji_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private LinearLayout relativeLayout;
        private emojiItem item;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            relativeLayout = (LinearLayout) v.findViewById(R.id.relativeLayout);
        }

        public void setData(emojiItem item) {
            this.item = item;
            imageView.setImageResource(item.drawable);
            relativeLayout.setBackgroundColor(Color.parseColor(item.color));
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
                //Toast.makeText(mContext, kisi_item.text + " is clicked", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.setData(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(emojiItem item);
    }
}
