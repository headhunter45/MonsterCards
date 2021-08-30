package com.majinnaibu.monstercards.ui.dashboard;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.data.enums.AdvantageType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.ProficiencyType;
import com.majinnaibu.monstercards.databinding.CardMonsterBinding;
import com.majinnaibu.monstercards.helpers.CommonMarkHelper;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.Locale;

public class DashboardRecyclerViewAdapter extends ListAdapter<Monster, DashboardRecyclerViewAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Monster> DIFF_CALLBACK = new DiffUtil.ItemCallback<Monster>() {
        @Override
        public boolean areItemsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
            return Monster.areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
            return Monster.areContentsTheSame(oldItem, newItem);
        }
    };
    private final ItemCallback mOnClick;

    protected DashboardRecyclerViewAdapter(ItemCallback onClick) {
        super(DIFF_CALLBACK);
        mOnClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardMonsterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Monster monster = getItem(position);
        holder.monster = monster;
        holder.name.setText(monster.name);
        holder.meta.setText(monster.getMeta());
        holder.strengthAdvantage.setText(Helpers.getAdvantageAbbreviation(monster.strengthSavingThrowAdvantage));
        holder.strengthModifier.setText(Helpers.getModifierString(monster.getStrengthModifier()));
        holder.strengthName.setText(Helpers.getAbilityScoreAbbreviation(AbilityScore.STRENGTH));
        holder.strengthProficiency.setText(Helpers.getProficiencyAbbreviation(monster.strengthSavingThrowProficiency));

        holder.dexterityAdvantage.setText(Helpers.getAdvantageAbbreviation(monster.dexteritySavingThrowAdvantage));
        holder.dexterityModifier.setText(Helpers.getModifierString(monster.getDexterityModifier()));
        holder.dexterityName.setText(Helpers.getAbilityScoreAbbreviation(AbilityScore.DEXTERITY));
        holder.dexterityProficiency.setText(Helpers.getProficiencyAbbreviation(monster.dexteritySavingThrowProficiency));

        holder.constitutionAdvantage.setText(Helpers.getAdvantageAbbreviation(monster.constitutionSavingThrowAdvantage));
        holder.constitutionModifier.setText(Helpers.getModifierString(monster.getConstitutionModifier()));
        holder.constitutionName.setText(Helpers.getAbilityScoreAbbreviation(AbilityScore.CONSTITUTION));
        holder.constitutionProficiency.setText(Helpers.getProficiencyAbbreviation(monster.constitutionSavingThrowProficiency));

        holder.intelligenceAdvantage.setText(Helpers.getAdvantageAbbreviation(monster.intelligenceSavingThrowAdvantage));
        holder.intelligenceModifier.setText(Helpers.getModifierString(monster.getIntelligenceModifier()));
        holder.intelligenceName.setText(Helpers.getAbilityScoreAbbreviation(AbilityScore.INTELLIGENCE));
        holder.intelligenceProficiency.setText(Helpers.getProficiencyAbbreviation(monster.intelligenceSavingThrowProficiency));

        holder.wisdomAdvantage.setText(Helpers.getAdvantageAbbreviation(monster.wisdomSavingThrowAdvantage));
        holder.wisdomModifier.setText(Helpers.getModifierString(monster.getWisdomModifier()));
        holder.wisdomName.setText(Helpers.getAbilityScoreAbbreviation(AbilityScore.WISDOM));
        holder.wisdomProficiency.setText(Helpers.getProficiencyAbbreviation(monster.wisdomSavingThrowProficiency));

        holder.charismaAdvantage.setText(Helpers.getAdvantageAbbreviation(monster.charismaSavingThrowAdvantage));
        holder.charismaModifier.setText(Helpers.getModifierString(monster.getCharismaModifier()));
        holder.charismaName.setText(Helpers.getAbilityScoreAbbreviation(AbilityScore.CHARISMA));
        holder.charismaProficiency.setText(Helpers.getProficiencyAbbreviation(monster.charismaSavingThrowProficiency));

        holder.armorClass.setText(String.valueOf(monster.getArmorClassValue()));
        holder.hitPoints.setText(String.valueOf(monster.getHitPointsValue()));
        holder.challengeRating.setText(holder.challengeRating.getResources().getString(R.string.label_challenge_rating_with_value, Helpers.getChallengeRatingAbbreviation(monster.challengeRating)));

        int numActions = monster.actions.size();
        if (numActions > 0) {
            holder.action1Group.setVisibility(View.VISIBLE);
            Trait action = monster.actions.get(0);
            holder.action1Description.setText(Html.fromHtml(CommonMarkHelper.toHtml(action.description)));
            holder.action1Name.setText(Html.fromHtml(CommonMarkHelper.toHtml(action.name)));
        } else {
            holder.action1Group.setVisibility(View.GONE);
        }

        if (numActions > 1) {
            holder.action2Group.setVisibility(View.VISIBLE);
            Trait action = monster.actions.get(1);
            holder.action2Description.setText(Html.fromHtml(CommonMarkHelper.toHtml(action.description)));
            holder.action2Name.setText(Html.fromHtml(CommonMarkHelper.toHtml(action.name)));
        } else {
            holder.action2Group.setVisibility(View.GONE);
        }

        if (numActions > 2) {
            holder.action3Group.setVisibility(View.VISIBLE);
            Trait action = monster.actions.get(2);
            holder.action3Description.setText(Html.fromHtml(CommonMarkHelper.toHtml(action.description)));
            holder.action3Name.setText(Html.fromHtml(CommonMarkHelper.toHtml(action.name)));
        } else {
            holder.action3Group.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mOnClick != null) {
                mOnClick.onItemCallback(holder.monster);
            }
        });
    }

    public interface ItemCallback {
        void onItemCallback(Monster monster);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView meta;
        public final View action1Group;
        public final TextView action1Name;
        public final TextView action1Description;
        public final View action2Group;
        public final TextView action2Name;
        public final TextView action2Description;
        public final View action3Group;
        public final TextView action3Name;
        public final TextView action3Description;
        public final TextView strengthName;
        public final TextView strengthModifier;
        public final TextView strengthProficiency;
        public final TextView strengthAdvantage;
        public final TextView dexterityName;
        public final TextView dexterityModifier;
        public final TextView dexterityProficiency;
        public final TextView dexterityAdvantage;
        public final TextView constitutionName;
        public final TextView constitutionModifier;
        public final TextView constitutionProficiency;
        public final TextView constitutionAdvantage;
        public final TextView intelligenceName;
        public final TextView intelligenceModifier;
        public final TextView intelligenceProficiency;
        public final TextView intelligenceAdvantage;
        public final TextView wisdomName;
        public final TextView wisdomModifier;
        public final TextView wisdomProficiency;
        public final TextView wisdomAdvantage;
        public final TextView charismaName;
        public final TextView charismaModifier;
        public final TextView charismaProficiency;
        public final TextView charismaAdvantage;
        public final TextView armorClass;
        public final TextView hitPoints;
        public final TextView challengeRating;
        public Monster monster;

        public ViewHolder(@NonNull CardMonsterBinding binding) {
            super(binding.getRoot());
            name = binding.name;
            meta = binding.meta;
            action1Group = binding.action1.getRoot();
            action1Name = binding.action1.name;
            action1Description = binding.action1.description;
            action2Group = binding.action2.getRoot();
            action2Name = binding.action2.name;
            action2Description = binding.action2.description;
            action3Group = binding.action3.getRoot();
            action3Name = binding.action3.name;
            action3Description = binding.action3.description;
            strengthName = binding.strength.name;
            strengthModifier = binding.strength.modifier;
            strengthProficiency = binding.strength.proficiency;
            strengthAdvantage = binding.strength.advantage;
            dexterityName = binding.dexterity.name;
            dexterityModifier = binding.dexterity.modifier;
            dexterityProficiency = binding.dexterity.proficiency;
            dexterityAdvantage = binding.dexterity.advantage;
            constitutionName = binding.constitution.name;
            constitutionModifier = binding.constitution.modifier;
            constitutionProficiency = binding.constitution.proficiency;
            constitutionAdvantage = binding.constitution.advantage;
            intelligenceName = binding.intelligence.name;
            intelligenceModifier = binding.intelligence.modifier;
            intelligenceProficiency = binding.intelligence.proficiency;
            intelligenceAdvantage = binding.intelligence.advantage;
            wisdomName = binding.wisdom.name;
            wisdomModifier = binding.wisdom.modifier;
            wisdomProficiency = binding.wisdom.proficiency;
            wisdomAdvantage = binding.wisdom.advantage;
            charismaName = binding.charisma.name;
            charismaModifier = binding.charisma.modifier;
            charismaProficiency = binding.charisma.proficiency;
            charismaAdvantage = binding.charisma.advantage;
            armorClass = binding.armorClass.value;
            hitPoints = binding.hitPoints.value;
            challengeRating = binding.challengeRating;
        }
    }

    public static class Helpers {
        @NonNull
        public static String getModifierString(int value) {
            return String.format(Locale.getDefault(), "%+d", value);
        }

        @NonNull
        public static String getAbilityScoreAbbreviation(@NonNull AbilityScore abilityScore) {
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

        @NonNull
        public static String getChallengeRatingAbbreviation(@NonNull ChallengeRating challengeRating) {
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

        @NonNull
        public static String getProficiencyAbbreviation(@NonNull ProficiencyType proficiency) {
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

        @NonNull
        public static String getAdvantageAbbreviation(@NonNull AdvantageType advantage) {
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
    }
}
