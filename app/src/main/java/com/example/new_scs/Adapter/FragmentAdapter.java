package com.example.new_scs.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.new_scs.WithoutClassTeacher.WCTProfile;
import com.example.new_scs.WithoutClassTeacher.WCTTodayHomework;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
//                return new WCTAllHomework();
//            case 2 :
                return new WCTProfile();
        }
        return new WCTTodayHomework();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
