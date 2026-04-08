package com.example.galleryapp_q4;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.galleryapp_q4.R;

import java.io.File;
import java.util.List;
public class ImageAdapter extends
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<File> imageList;
    public ImageAdapter(Context context, List<File> imageList) {
        this.context = context;
        this.imageList = imageList;
    }
    // ViewHolder holds reference to each grid item view
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// Inflate the item_image.xml layout for each grid item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        File imageFile = imageList.get(position);

        // Use Glide library to efficiently load and display image
        Glide.with(context)
                .load(imageFile)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageView);
// Open image detail screen when image is clicked
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.galleryapp_q4.ImageDetailActivity.class);
            intent.putExtra("imagePath", imageFile.getAbsolutePath());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
