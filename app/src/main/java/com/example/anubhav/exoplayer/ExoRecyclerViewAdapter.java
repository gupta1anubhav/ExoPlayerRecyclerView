package com.example.anubhav.exoplayer;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ExoRecyclerViewAdapter extends RecyclerView.Adapter<ParentViewHolder> {

    private static final int VIEW_TYPE_NORMAL = 1;

    private List<VideoPojo> mInfoList;

    public ExoRecyclerViewAdapter(List<VideoPojo> infoList) {
        mInfoList = infoList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
             default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (mInfoList != null && mInfoList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mInfoList != null && mInfoList.size() > 0) {
            return mInfoList.size();
        } else {
            return 1;
        }
    }

    public class ViewHolder extends ParentViewHolder {
        TextView textViewTitle;
        TextView userHandle;
        FrameLayout videoLayout;
        ImageView mCover;
        ProgressBar progressBar;
        final View parent;


        ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tvTitle);
            userHandle = itemView.findViewById(R.id.userName);
            videoLayout = itemView.findViewById(R.id.video_layout);
            progressBar = itemView.findViewById(R.id.progressBar);
            mCover = itemView.findViewById(R.id.cover);
            parent = itemView;
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            parent.setTag(this);
            VideoPojo videoInfo = mInfoList.get(position);
            textViewTitle.setText(videoInfo.getTitle());
            userHandle.setText(videoInfo.getUser());
            Glide.with(itemView.getContext())
                    .load(videoInfo.getThumbnail()).apply(new RequestOptions().optionalCenterCrop())
                    .into(mCover);
        }
    }

}