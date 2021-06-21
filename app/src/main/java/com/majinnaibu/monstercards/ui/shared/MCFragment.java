package com.majinnaibu.monstercards.ui.shared;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.data.MonsterRepository;

public class MCFragment extends Fragment {
    public MonsterCardsApplication getApplication() {
        return (MonsterCardsApplication) requireActivity().getApplication();
    }

    protected MonsterRepository getMonsterRepository() {
        return this.getApplication().getMonsterRepository();
    }

    public AppCompatActivity requireAppCompatActivity() {
        return (AppCompatActivity) requireActivity();
    }

    public void setTitle(CharSequence title) {
        Activity activity = requireActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            ActionBar supportActionBar = appCompatActivity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(title);
            }
        }
    }
}
