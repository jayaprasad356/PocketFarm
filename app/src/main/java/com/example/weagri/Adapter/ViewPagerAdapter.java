package com.example.weagri.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.weagri.Fragment.ActiveplanFragment;
import com.example.weagri.Fragment.PlanFragment;
import com.example.weagri.Fragment.PlansFragment;

public class ViewPagerAdapter
        extends FragmentPagerAdapter {

    public ViewPagerAdapter(
            @NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new PlansFragment();
        else if (position == 1)
            fragment = new ActiveplanFragment();


        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Plan";
        else if (position == 1)
            title = "Active Plan";

        return title;
    }
}