package com.majinnaibu.monstercards.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.majinnaibu.monstercards.MonsterCardsApplication;
import com.majinnaibu.monstercards.data.MonsterRepository;

public class MCFragment extends Fragment {
    public MonsterCardsApplication getApplication() {
        return (MonsterCardsApplication) this.getActivity().getApplication();
    }

    protected MonsterRepository getMonsterRepository() {
        return this.getApplication().getMonsterRepository();
    }

    public AppCompatActivity requireAppCompatActivity() {
        return (AppCompatActivity) requireActivity();
    }
}
