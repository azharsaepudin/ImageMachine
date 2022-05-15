package com.azhar.imagemachine.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.imagemachine.Machine.ImageFullActivity;
import com.azhar.imagemachine.databinding.ListImageBinding;
import com.azhar.imagemachine.models.ImageModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ListImageAdapter  extends RecyclerView.Adapter<ListImageAdapter.ListImageViewHolder>{

    Activity activity;
    List<ImageModel> imgListData;

    public ListImageAdapter(Activity activity, List<ImageModel> imgListData){
        this.activity = activity;
        this.imgListData = imgListData;
    }

    @NonNull
    @Override
    public ListImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListImageAdapter.ListImageViewHolder(ListImageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListImageViewHolder holder, int position) {
        holder.bind(imgListData.get(position));
    }

    @Override
    public int getItemCount() {
        return imgListData != null ? imgListData.size() : 0;
    }

    public class ListImageViewHolder extends RecyclerView.ViewHolder {

        ListImageBinding listImageBinding;

        public ListImageViewHolder(ListImageBinding listImageBinding) {
            super(listImageBinding.getRoot());
            this.listImageBinding = listImageBinding;
        }

        public void bind(ImageModel imageModel) {


            listImageBinding.cvImgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), ImageFullActivity.class);
                    i.putExtra("MC_PHOTO", imageModel.photo_path);
                    i.putExtra("MC_ID_PHOTO", imageModel.photoId);
                    view.getContext().startActivity(i);


                }
            });


            Uri mImgUri = Uri.parse(imageModel.photo_path);

            Glide.with(listImageBinding.getRoot())
                    .load(mImgUri)
                    .into( listImageBinding.imgListMachine);

        }
    }


}
