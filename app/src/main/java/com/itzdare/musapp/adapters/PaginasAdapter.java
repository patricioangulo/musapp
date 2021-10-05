package com.itzdare.musapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itzdare.musapp.fragments.ChatFragment;
import com.itzdare.musapp.fragments.HomeFragment;
import com.itzdare.musapp.fragments.UsuariosFragment;

public class PaginasAdapter extends FragmentStateAdapter {



    public PaginasAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 :
                return new HomeFragment();
            case 1 :
                return new UsuariosFragment();
            case 2 :
                return new ChatFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
