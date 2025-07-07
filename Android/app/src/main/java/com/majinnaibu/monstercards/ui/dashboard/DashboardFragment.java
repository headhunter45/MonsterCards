package com.majinnaibu.monstercards.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardFragment extends MCFragment {
    private static final String MODIFIER_FORMAT = "%+d";
    private DashboardViewModel mViewModel;
    private ViewHolder mHolder;
    private DashboardRecyclerViewAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mHolder = new ViewHolder(root);

        setupRecyclerView(mHolder.list);

        getMonsterRepository()
                .getMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    mViewModel.setMonsters(monsters);
                });

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int columnCount = Math.max(1, (int) Math.floor(getResources().getConfiguration().screenWidthDp / 396));
        Logger.logWTF(String.format("Setting column count to %d", columnCount));
        Context context = requireContext();
        GridLayoutManager layoutManager = new GridLayoutManager(context, columnCount);
        recyclerView.setLayoutManager(layoutManager);

        LiveData<List<Monster>> monsterData = mViewModel.getMonsters();
        mAdapter = new DashboardRecyclerViewAdapter(monster -> {
            if (monster != null) {
                navigateToMonsterDetail(monster);
            } else {
                Logger.logError("Can't navigate to MonsterDetailFragment with a null monster");
            }
        });
        if (monsterData != null) {
            monsterData.observe(getViewLifecycleOwner(), monsters -> mAdapter.submitList(monsters));
        }
        recyclerView.setAdapter(mAdapter);
    }

    private void navigateToMonsterDetail(Monster monster) {
        NavDirections action = DashboardFragmentDirections.actionNavigationDashboardToNavigationMonster(monster.id.toString());
        Navigation.findNavController(requireView()).navigate(action);
    }

    private static class ViewHolder {
        final RecyclerView list;

        ViewHolder(View root) {
            list = root.findViewById(R.id.list);
        }
    }

}
