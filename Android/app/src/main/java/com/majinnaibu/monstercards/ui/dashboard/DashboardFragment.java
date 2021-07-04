package com.majinnaibu.monstercards.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardFragment extends MCFragment {
    private static final String MODIFIER_FORMAT = "%+d";
    private ViewHolder mHolder;
    private DashboardViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mHolder = new ViewHolder(root);

        mViewModel.getMonsters().observe(getViewLifecycleOwner(), monsters -> {
            setupMonsterCards(mHolder.monsterCards, R.layout.card_monster, monsters);
            setupMonsterCards(mHolder.monsterShortCards, R.layout.card_monster_short, monsters);
            setupMonsterCards(mHolder.monsterTiles, R.layout.tile_monster, monsters);
            setupMonsterCards(mHolder.monsterShortTiles, R.layout.tile_monster_short, monsters);
        });

        getMonsterRepository()
                .getMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    mViewModel.setMonsters(monsters);
                });

        return root;
    }

    private void setupMonsterCards(GridLayout parent, int layout, List<Monster> monsters) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        parent.removeAllViews();
        for (Monster monster : monsters) {
            if (monster != null) {
                View root = inflater.inflate(layout, parent, false);
                setupCardOrTile(root, monster, layout);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(root.getLayoutParams());
                ViewGroup.LayoutParams lp2 = root.getLayoutParams();
                layoutParams.width = 0;
                layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 01.0f);
                root.setLayoutParams(layoutParams);
                parent.addView(root);
            }
        }
    }

    private void setupCardOrTile(View root, Monster monster, int layout) {
        if (layout == R.layout.card_monster || layout == R.layout.card_monster_short) {
            MonsterCardViewHolder holder = new MonsterCardViewHolder(root);
            int numActions = monster.actions.size();
            Trait action1 = numActions > 0 ? monster.actions.get(0) : null;
            Trait action2 = numActions > 1 ? monster.actions.get(1) : null;
            Trait action3 = numActions > 2 ? monster.actions.get(2) : null;
            setupAction(holder.action1, action1);
            setupAction(holder.action2, action2);
            setupAction(holder.action3, action3);
            int acValue = monster.getArmorClassValue();
            holder.armorClass.value.setText(acValue <= 0 ? "*" : String.format("%d", acValue));
            holder.challengeRating.setText(String.format("CR %s", getChallengeRatingAbbreviation(monster.challengeRating)));
            setupAttribute(holder.strength, monster, AbilityScore.STRENGTH);
            setupAttribute(holder.dexterity, monster, AbilityScore.DEXTERITY);
            setupAttribute(holder.constitution, monster, AbilityScore.CONSTITUTION);
            setupAttribute(holder.intelligence, monster, AbilityScore.INTELLIGENCE);
            setupAttribute(holder.wisdom, monster, AbilityScore.WISDOM);
            setupAttribute(holder.charisma, monster, AbilityScore.CHARISMA);
            int hpValue = monster.getHitPointsValue();
            holder.hitPoints.value.setText(hpValue <= 0 ? "*" : String.format("%d", hpValue));
            holder.meta.setText(monster.getMeta());
            holder.name.setText(monster.name);
        } else {
            MonsterTileViewHolder holder = new MonsterTileViewHolder(root);
            holder.name.setText(monster.name);
            holder.meta.setText(monster.getMeta());
            int acValue = monster.getArmorClassValue();
            holder.armorClass.value.setText(acValue <= 0 ? "*" : String.format("%d", acValue));
            holder.challengeRating.value.setText(getChallengeRatingAbbreviation(monster.challengeRating));
            int hpValue = monster.getHitPointsValue();
            holder.hitPoints.value.setText(hpValue <= 0 ? "*" : String.format("%d", hpValue));
            holder.initiative.value.setText(String.format(MODIFIER_FORMAT, monster.getDexterityModifier()));
            setupAction(holder.action, monster.actions.size() > 0 ? monster.actions.get(0) : null);
        }
    }

    private void setupAttribute(AttributeViewHolder holder, Monster monster, AbilityScore abilityScore) {
        holder.name.setText(getAbilityScoreAbbreviation(abilityScore));
        holder.modifier.setText(String.format(MODIFIER_FORMAT, monster.getAbilityModifier(abilityScore)));
        holder.proficiency.setText(getProficiencyAbbreviation(monster.getSavingThrowProficiencyType(abilityScore)));
        holder.advantage.setText(getAdvantageAbbreviation(monster.getSavingThrowAdvantageType(abilityScore)));
    }

    private String getAbilityScoreAbbreviation(AbilityScore abilityScore) {
        switch (abilityScore) {
            case STRENGTH:
                return "S";
            case DEXTERITY:
                return "D";
            case CONSTITUTION:
                return "C";
            case INTELLIGENCE:
                return "I";
            case WISDOM:
                return "W";
            case CHARISMA:
                return "Ch";
            default:
                Logger.logUnimplementedFeature(String.format("Get an abbreviation for AbilityScore value %s", abilityScore));
                return "";
        }
    }

    private String getChallengeRatingAbbreviation(ChallengeRating challengeRating) {
        Logger.logUnimplementedMethod();
        switch (challengeRating) {
            case CUSTOM:
                return "*";
            case ZERO:
                return "0";
            case ONE_EIGHTH:
                return "1/8";
            case ONE_QUARTER:
                return "1/4";
            case ONE_HALF:
                return "1/2";
            case ONE:
                return "1";
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case FIVE:
                return "5";
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            case ELEVEN:
                return "11";
            case TWELVE:
                return "12";
            case THIRTEEN:
                return "13";
            case FOURTEEN:
                return "14";
            case FIFTEEN:
                return "15";
            case SIXTEEN:
                return "16";
            case SEVENTEEN:
                return "17";
            case EIGHTEEN:
                return "18";
            case NINETEEN:
                return "19";
            case TWENTY:
                return "20";
            case TWENTY_ONE:
                return "21";
            case TWENTY_TWO:
                return "22";
            case TWENTY_THREE:
                return "23";
            case TWENTY_FOUR:
                return "24";
            case TWENTY_FIVE:
                return "25";
            case TWENTY_SIX:
                return "26";
            case TWENTY_SEVEN:
                return "27";
            case TWENTY_EIGHT:
                return "28";
            case TWENTY_NINE:
                return "29";
            case THIRTY:
                return "30";
            default:
                Logger.logUnimplementedFeature(String.format("Get an abbreviation for ChallengeRating value %s", challengeRating));
                return "";
        }
    }

    private String getProficiencyAbbreviation(ProficiencyType proficiency) {
        switch (proficiency) {
            case NONE:
                return "";
            case EXPERTISE:
                return "E";
            case PROFICIENT:
                return "P";
            default:
                Logger.logUnimplementedFeature(String.format("Get an abbreviation for ProficiencyType value %s", proficiency));
                return "";
        }
    }

    private String getAdvantageAbbreviation(AdvantageType advantage) {
        switch (advantage) {
            case NONE:
                return "";
            case ADVANTAGE:
                return "A";
            case DISADVANTAGE:
                return "D";
            default:
                Logger.logUnimplementedFeature(String.format("Get an abbreviation for AdvantageType value %s", advantage));
                return "";
        }
    }

    private void setupAction(ActionViewHolder holder, Trait action) {
        if (action == null) {
            if (holder.root != null) {
                holder.root.setVisibility(View.INVISIBLE);
            }
        } else {
            if (holder.name != null) {
                holder.name.setText(action.name);
            }
            if (holder.description != null) {
                holder.description.setText(action.description);
            }
        }
    }

    private static class ViewHolder {
        final GridLayout monsterCards;
        final GridLayout monsterShortCards;
        final GridLayout monsterTiles;
        final GridLayout monsterShortTiles;

        ViewHolder(View root) {
            monsterCards = root.findViewById(R.id.monsterCards);
            monsterShortCards = root.findViewById(R.id.monsterShortCards);
            monsterTiles = root.findViewById(R.id.monsterTiles);
            monsterShortTiles = root.findViewById(R.id.monsterShortTiles);
        }
    }

    private static class MonsterCardViewHolder {
        final TextView name;
        final TextView meta;
        final TextView challengeRating;
        final AttributeViewHolder strength;
        final AttributeViewHolder dexterity;
        final AttributeViewHolder constitution;
        final AttributeViewHolder intelligence;
        final AttributeViewHolder wisdom;
        final AttributeViewHolder charisma;
        final ArmorClassViewHolder armorClass;
        final HitPointsViewHolder hitPoints;
        final ActionViewHolder action1;
        final ActionViewHolder action2;
        final ActionViewHolder action3;

        MonsterCardViewHolder(View root) {
            name = root.findViewById(R.id.name);
            meta = root.findViewById(R.id.meta);
            challengeRating = root.findViewById(R.id.challengeRating);
            strength = new AttributeViewHolder(root.findViewById(R.id.strength));
            dexterity = new AttributeViewHolder(root.findViewById(R.id.dexterity));
            constitution = new AttributeViewHolder(root.findViewById(R.id.constitution));
            intelligence = new AttributeViewHolder(root.findViewById(R.id.intelligence));
            wisdom = new AttributeViewHolder(root.findViewById(R.id.wisdom));
            charisma = new AttributeViewHolder(root.findViewById(R.id.charisma));
            armorClass = new ArmorClassViewHolder(root.findViewById(R.id.armorClass));
            hitPoints = new HitPointsViewHolder(root.findViewById(R.id.hitPoints));
            action1 = new ActionViewHolder(root.findViewById(R.id.action1));
            action2 = new ActionViewHolder(root.findViewById(R.id.action2));
            action3 = new ActionViewHolder(root.findViewById(R.id.action3));
        }
    }

    private static class MonsterTileViewHolder {
        final TextView name;
        final TextView meta;
        final ArmorClassViewHolder armorClass;
        final HitPointsViewHolder hitPoints;
        final InitiativeViewHolder initiative;
        final ChallengeRatingViewHolder challengeRating;
        final ActionViewHolder action;

        MonsterTileViewHolder(View root) {
            name = root.findViewById(R.id.name);
            meta = root.findViewById(R.id.meta);
            armorClass = new ArmorClassViewHolder(root.findViewById(R.id.armorClass));
            hitPoints = new HitPointsViewHolder(root.findViewById(R.id.hitPoints));
            initiative = new InitiativeViewHolder(root.findViewById(R.id.initiative));
            challengeRating = new ChallengeRatingViewHolder(root.findViewById(R.id.challengeRating));
            action = new ActionViewHolder(root.findViewById(R.id.action));
        }
    }

    private static class ArmorClassViewHolder {
        final TextView value;

        ArmorClassViewHolder(View root) {
            if (root == null) {
                value = null;
            } else {
                value = root.findViewById(R.id.value);
            }

        }
    }

    private static class HitPointsViewHolder {
        final TextView value;

        HitPointsViewHolder(View root) {
            if (root == null) {
                value = null;
            } else {
                value = root.findViewById(R.id.value);
            }
        }
    }

    private static class InitiativeViewHolder {
        final TextView value;

        InitiativeViewHolder(View root) {
            if (root == null) {
                value = null;
            } else {
                value = root.findViewById(R.id.value);
            }
        }
    }

    private static class ChallengeRatingViewHolder {
        final TextView value;

        ChallengeRatingViewHolder(View root) {
            if (root == null) {
                value = null;
            } else {
                value = root.findViewById(R.id.value);
            }
        }
    }

    private static class AttributeViewHolder {
        final TextView name;
        final TextView advantage;
        final TextView proficiency;
        final TextView modifier;

        AttributeViewHolder(View root) {
            if (root == null) {
                name = null;
                advantage = null;
                proficiency = null;
                modifier = null;
            } else {
                name = root.findViewById(R.id.name);
                advantage = root.findViewById(R.id.advantage);
                proficiency = root.findViewById(R.id.proficiency);
                modifier = root.findViewById(R.id.modifier);
            }
        }
    }

    private static class ActionViewHolder {
        final TextView name;
        final TextView description;
        final View root;

        ActionViewHolder(View root) {
            this.root = root;
            if (root == null) {
                name = null;
                description = null;
            } else {
                name = root.findViewById(R.id.name);
                description = root.findViewById(R.id.description);
            }
        }
    }
}
