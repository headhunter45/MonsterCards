package com.majinnaibu.monstercards;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.helpers.CommonMarkHelper;
import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.monster.MonsterDetailViewModel;
import com.majinnaibu.monstercards.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImportMonsterActivity extends AppCompatActivity {

    private ViewHolder mHolder;

    private MonsterDetailViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_monster);
        mHolder = new ViewHolder(findViewById(android.R.id.content));
        mViewModel = new ViewModelProvider(this).get(MonsterDetailViewModel.class);

        mViewModel.getName().observe(this, mHolder.name::setText);
        mViewModel.getMeta().observe(this, mHolder.meta::setText);
        mViewModel.getArmorClass().observe(this, armorText -> setupLabeledTextView(mHolder.armorClass, armorText, R.string.label_armor_class));
        mViewModel.getHitPoints().observe(this, hitPoints -> setupLabeledTextView(mHolder.hitPoints, hitPoints, R.string.label_hit_points));
        mViewModel.getSpeed().observe(this, speed -> setupLabeledTextView(mHolder.speed, speed, R.string.label_speed));
        mViewModel.getStrength().observe(this, mHolder.strength::setText);
        mViewModel.getDexterity().observe(this, mHolder.dexterity::setText);
        mViewModel.getConstitution().observe(this, mHolder.constitution::setText);
        mViewModel.getIntelligence().observe(this, mHolder.intelligence::setText);
        mViewModel.getWisdom().observe(this, mHolder.wisdom::setText);
        mViewModel.getCharisma().observe(this, mHolder.charisma::setText);
        mViewModel.getSavingThrows().observe(this, savingThrows -> setupOptionalTextView(mHolder.savingThrows, savingThrows, R.string.label_saving_throws));
        mViewModel.getSkills().observe(this, skills -> setupOptionalTextView(mHolder.skills, skills, R.string.label_skills));
        mViewModel.getDamageVulnerabilities().observe(this, damageTypes -> setupOptionalTextView(mHolder.damageVulnerabilities, damageTypes, R.string.label_damage_vulnerabilities));
        mViewModel.getDamageResistances().observe(this, damageTypes -> setupOptionalTextView(mHolder.damageResistances, damageTypes, R.string.label_damage_resistances));
        mViewModel.getDamageImmunities().observe(this, damageTypes -> setupOptionalTextView(mHolder.damageImmunities, damageTypes, R.string.label_damage_immunities));
        mViewModel.getConditionImmunities().observe(this, conditionImmunities -> setupOptionalTextView(mHolder.conditionImmunities, conditionImmunities, R.string.label_condition_immunities));
        mViewModel.getSenses().observe(this, senses -> setupOptionalTextView(mHolder.senses, senses, R.string.label_senses));
        mViewModel.getLanguages().observe(this, languages -> setupOptionalTextView(mHolder.languages, languages, R.string.label_languages));
        mViewModel.getChallenge().observe(this, challengeRating -> setupLabeledTextView(mHolder.challenge, challengeRating, R.string.label_challenge_rating));
        mViewModel.getAbilities().observe(this, abilities -> setupTraitList(mHolder.abilities, abilities));
        mViewModel.getActions().observe(this, actions -> setupTraitList(mHolder.actions, actions, mHolder.actions_label, mHolder.actions_divider));
        mViewModel.getReactions().observe(this, reactions -> setupTraitList(mHolder.reactions, reactions, mHolder.reactions_label, mHolder.reactions_divider));
        mViewModel.getRegionalEffects().observe(this, regionalEffects -> setupTraitList(mHolder.regionalEffects, regionalEffects, mHolder.regionalEffects_label, mHolder.regionalEffects_divider));
        mViewModel.getLairActions().observe(this, lairActions -> setupTraitList(mHolder.lairActions, lairActions, mHolder.lairActions_label, mHolder.lairActions_divider));
        mViewModel.getLegendaryActions().observe(this, legendaryActions -> setupTraitList(mHolder.legendaryActions, legendaryActions, mHolder.legendaryActions_label, mHolder.legendaryActions_divider));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.logDebug("onCreateView");
        Monster monster = readMonsterFromIntent(getIntent());
        if (monster != null) {
            mViewModel.setMonster(monster);
        }
    }

    private Monster readMonsterFromIntent(Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        String type = intent.getType();
        String json;
        Uri uri = null;
        if ("android.intent.action.SEND".equals(action) && "text/plain".equals(type)) {
            uri = extras.getParcelable("android.intent.extra.STREAM");
        } else if ("android.intent.action.VIEW".equals(action) && ("text/plain".equals(type) || "application/octet-stream".equals(type))) {
            uri = intent.getData();
        } else {
            Logger.logError(String.format("unexpected launch configuration action: %s, type: %s, uri: %s", action, type, uri));
        }
        if (uri == null) {
            return null;
        }
        json = readContentsOfUri(uri);
        if (StringHelper.isNullOrEmpty(json)) {
            return null;
        }
        return MonsterImportHelper.fromJSON(json);
    }

    private String readContentsOfUri(Uri uri) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Logger.logError("error reading file", e);
            return null;
        }
        return builder.toString();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void setupLabeledTextView(TextView view, String text, int titleId) {
        String title = getString(titleId);
        String fullText = String.format("<b>%s</b> %s", title, text);
        view.setText(Html.fromHtml(fullText));
    }

    private void setupOptionalTextView(TextView root, String text, int titleId) {
        String title = getString(titleId);
        if (StringHelper.isNullOrEmpty(text)) {
            root.setVisibility(View.GONE);
        } else {
            root.setVisibility(View.VISIBLE);
        }
        Spanned formatted;
        if (StringHelper.isNullOrEmpty(title)) {
            formatted = Html.fromHtml(text);
        } else {
            formatted = Html.fromHtml(String.format("<b>%s</b> %s", title, text));
        }
        root.setText(formatted);
    }

    private void setupTraitList(@NonNull LinearLayout root, @NonNull List<String> traits) {
        setupTraitList(root, traits, null, null);
    }

    private void setupTraitList(@NonNull LinearLayout root, @NonNull List<String> traits, View label, View divider) {
        int visibility = traits.size() > 0 ? View.VISIBLE : View.GONE;
        DisplayMetrics displayMetrics = null;
        Resources resources = getResources();
        if (resources != null) {
            displayMetrics = resources.getDisplayMetrics();
        }
        root.removeAllViews();
        for (String action : traits) {
            TextView tvAction = new TextView(this);
            // TODO: Handle multiline block quotes specially so they stay multiline.
            // TODO: Replace QuoteSpans in the result of fromHtml with something like this https://stackoverflow.com/questions/7717567/how-to-style-blockquotes-in-android-textviews to make them indent as expected
            tvAction.setText(Html.fromHtml(CommonMarkHelper.toHtml(action)));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
            tvAction.setLayoutParams(layoutParams);
            root.addView(tvAction);
        }
        root.setVisibility(visibility);
        if (label != null) {
            label.setVisibility(visibility);
        }
        if (divider != null) {
            divider.setVisibility(visibility);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.import_monster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_import_monster) {
            Logger.logDebug("Menu Item Selected");
            Monster monster = mViewModel.getMonster();
            if (monster != null) {
                monster.id = UUID.randomUUID();
                MonsterCardsApplication application = (MonsterCardsApplication) getApplication();
                MonsterRepository repository = application.getMonsterRepository();
                repository.addMonster(monster).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Snackbar.make(
                                mHolder.root,
                                getString(R.string.snackbar_monster_created, monster.name),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", (_view) -> navigateToEditMonster(monster.id))
                                .show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Logger.logError("Error creating monster", e);
                        Snackbar.make(mHolder.root, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG).show();
                    }
                });
            } else {
                Logger.logWTF("monsterId cannot be null.");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToEditMonster(UUID monsterId) {
        Logger.logUnimplementedFeature(String.format("navigate to editing the monster %s", monsterId));
    }

    private static class ViewHolder {
        final View root;
        final TextView name;
        final TextView meta;
        final TextView armorClass;
        final TextView hitPoints;
        final TextView speed;
        final TextView strength;
        final TextView dexterity;
        final TextView constitution;
        final TextView intelligence;
        final TextView wisdom;
        final TextView charisma;
        final TextView savingThrows;
        final TextView skills;
        final TextView damageVulnerabilities;
        final TextView damageResistances;
        final TextView damageImmunities;
        final TextView conditionImmunities;
        final TextView senses;
        final TextView languages;
        final TextView challenge;
        final LinearLayout abilities;
        final LinearLayout actions;
        final TextView actions_label;
        final ImageView actions_divider;
        final LinearLayout reactions;
        final TextView reactions_label;
        final ImageView reactions_divider;
        final LinearLayout legendaryActions;
        final TextView legendaryActions_label;
        final ImageView legendaryActions_divider;
        final LinearLayout lairActions;
        final TextView lairActions_label;
        final ImageView lairActions_divider;
        final LinearLayout regionalEffects;
        final TextView regionalEffects_label;
        final ImageView regionalEffects_divider;

        ViewHolder(View root) {
            this.root = root;
            name = root.findViewById(R.id.name);
            meta = root.findViewById(R.id.meta);
            armorClass = root.findViewById(R.id.armorClass);
            hitPoints = root.findViewById(R.id.hitPoints);
            speed = root.findViewById(R.id.speed);
            strength = root.findViewById(R.id.strength);
            dexterity = root.findViewById(R.id.dexterity);
            constitution = root.findViewById(R.id.constitution);
            intelligence = root.findViewById(R.id.intelligence);
            wisdom = root.findViewById(R.id.wisdom);
            charisma = root.findViewById(R.id.charisma);
            savingThrows = root.findViewById(R.id.savingThrows);
            skills = root.findViewById(R.id.skills);
            damageVulnerabilities = root.findViewById(R.id.damageVulnerabilities);
            damageResistances = root.findViewById(R.id.damageResistances);
            damageImmunities = root.findViewById(R.id.damageImmunities);
            conditionImmunities = root.findViewById(R.id.conditionImmunities);
            senses = root.findViewById(R.id.senses);
            languages = root.findViewById(R.id.languages);
            challenge = root.findViewById(R.id.challenge);
            abilities = root.findViewById(R.id.abilities);
            actions = root.findViewById(R.id.actions);
            actions_divider = root.findViewById(R.id.actions_divider);
            actions_label = root.findViewById(R.id.actions_label);
            reactions = root.findViewById(R.id.reactions);
            reactions_divider = root.findViewById(R.id.reactions_divider);
            reactions_label = root.findViewById(R.id.reactions_label);
            legendaryActions = root.findViewById(R.id.legendaryActions);
            legendaryActions_divider = root.findViewById(R.id.legendaryActions_divider);
            legendaryActions_label = root.findViewById(R.id.legendaryActions_label);
            lairActions = root.findViewById(R.id.lairActions);
            lairActions_divider = root.findViewById(R.id.lairActions_divider);
            lairActions_label = root.findViewById(R.id.lairActions_label);
            regionalEffects = root.findViewById(R.id.regionalEffects);
            regionalEffects_divider = root.findViewById(R.id.regionalEffects_divider);
            regionalEffects_label = root.findViewById(R.id.regionalEffects_label);
        }
    }
}
