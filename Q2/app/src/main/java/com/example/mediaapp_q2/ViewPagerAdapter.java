package com.example.mediaapp_q2;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    @NonNull
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new audio();
        } else {
            return new video();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}