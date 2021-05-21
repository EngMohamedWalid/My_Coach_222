package com.example.my_coach.sport.SportsProperties;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public  class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AboutSportFragment();
            case 1:

                return new  CoachesFragment();
            case 2:
            default:
                return new  ExerciseFragment();
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "AboutSport";
            case 1:

                return "Coaches";
            case 2:
            default:
                return "Exercise";
        }
    }
}
