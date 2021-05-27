package com.majinnaibu.monstercards.ui.editmonster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.ui.monster.MonsterDetailFragmentArgs;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.UUID;

import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class EditMonsterFragment extends MCFragment {

    private EditMonsterViewModel mViewModel;
    private ViewHolder mHolder;

    public static EditMonsterFragment newInstance() {
        return new EditMonsterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        MonsterRepository repository = getMonsterRepository();
        Bundle arguments = getArguments();
        assert arguments != null;
        UUID monsterId = UUID.fromString(MonsterDetailFragmentArgs.fromBundle(arguments).getMonsterId());

        View root = inflater.inflate(R.layout.fragment_edit_monster, container, false);
        mHolder = new ViewHolder(root);
        requireAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.title_edit_monster, getString(R.string.default_monster_name)));

        // TODO: Show a loading spinner until we have the monster loaded.
        repository.getMonster(monsterId).toObservable()
                .firstOrError()
                .subscribe(new DisposableSingleObserver<Monster>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Monster monster) {
                        Logger.logDebug(String.format("Monster loaded: %s", monster.name));
                        mViewModel.copyFromMonster(monster);
                        requireAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.title_edit_monster, monster.name));
                        dispose();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // TODO: Show an error state.
                        Logger.logError(e);
                        dispose();
                    }
                });

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