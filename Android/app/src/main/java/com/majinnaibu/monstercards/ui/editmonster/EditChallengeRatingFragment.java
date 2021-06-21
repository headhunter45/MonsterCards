package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.helpers.ArrayHelper;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.TextChangedListener;

public class EditChallengeRatingFragment extends MCFragment {
    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.edit_monster_navigation);
        mViewModel = new ViewModelProvider(backStackEntry).get(EditMonsterViewModel.class);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_challenge_rating, container, false);
        mHolder = new ViewHolder(root);

        mHolder.challengeRating.setAdapter(new ArrayAdapter<ChallengeRating>(requireContext(), R.layout.dropdown_list_item, ChallengeRating.values()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ChallengeRating item = getItem(position);
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(item.displayName);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ChallengeRating item = getItem(position);
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setText(item.displayName);
                return view;
            }
        });
        mHolder.challengeRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChallengeRating selectedItem = (ChallengeRating) parent.getItemAtPosition(position);
                mViewModel.setChallengeRating(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mViewModel.setChallengeRating(ChallengeRating.CUSTOM);
            }
        });
        mHolder.challengeRating.setSelection(ArrayHelper.indexOf(ChallengeRating.values(), mViewModel.getChallengeRating().getValue()));

        mHolder.customChallengeRatingDescription.setText(mViewModel.getCustomChallengeRatingDescription().getValue());
        mHolder.customChallengeRatingDescription.addTextChangedListener((new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setCustomChallengeRatingDescription(s.toString()))));

        mHolder.customProficiencyBonus.setText(mViewModel.getCustomProficiencyBonusValueAsString());
        mHolder.customProficiencyBonus.addTextChangedListener((new TextChangedListener((TextChangedListener.OnTextChangedCallback) (s, start, before, count) -> mViewModel.setCustomProficiencyBonus(s.toString()))));

        return root;
    }

    private static class ViewHolder {
        final Spinner challengeRating;
        final EditText customChallengeRatingDescription;
        final EditText customProficiencyBonus;

        ViewHolder(View root) {
            challengeRating = root.findViewById(R.id.challengeRating);
            customChallengeRatingDescription = root.findViewById(R.id.customChallengeRatingDescription);
            customProficiencyBonus = root.findViewById(R.id.customProficiencyBonus);
        }
    }
}