package com.example.galleryapp_q4;
import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;
public class ImageAdapter
        extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<File> imageList;
    public ImageAdapter(Context context, List<File> imageList) {
        this.context = context;
        this.imageList = imageList;
    }
    // ViewHolder holds the thumbnail ImageView for each grid cell
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        File imageFile = imageList.get(position);
// Glide handles efficient image loading and caching
        Glide.with(context)
                .load(imageFile)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageView);
// Tap thumbnail to open the detail screen
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.putExtra("imagePath", imageFile.getAbsolutePath());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() { return imageList.size(); }
}