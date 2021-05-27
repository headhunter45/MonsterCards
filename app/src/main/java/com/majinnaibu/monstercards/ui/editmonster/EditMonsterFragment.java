package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.utils.Logger;

public class EditMonsterFragment extends Fragment {

    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    public static EditMonsterFragment newInstance() {
        return new EditMonsterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // TODO: Get our monster from the repo based on the id passed in.
        // TODO: Show a loading spinner until we have the monster loaded.
        // TODO: If there is an error loading the monster show the error.

        View root = inflater.inflate(R.layout.fragment_edit_monster, container, false);

        mHolder = new ViewHolder(root);

        mHolder.basicInfoButton.setOnClickListener(v -> {
            // TODO: Navigate to the EditBasicInfo fragment
            Logger.logDebug("Basic Info clicked");
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditMonsterViewModel.class);
        // TODO: Use the ViewModel
    }

    private static class ViewHolder {

        TextView basicInfoButton;

        ViewHolder(View root) {
            basicInfoButton = root.findViewById(R.id.basicInfo);
        }
    }
}