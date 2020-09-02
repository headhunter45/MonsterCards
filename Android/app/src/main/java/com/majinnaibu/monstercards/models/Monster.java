package com.majinnaibu.monstercards.models;

import com.majinnaibu.monstercards.helpers.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Monster {

    public Monster() {
        mAbilities = new ArrayList<>();
        mConditionImmunities = new HashSet<>();
        mDamageTypes = new HashSet<>();
        mLanguages = new HashSet<>();
        mSavingThrows = new HashSet<>();
        mSkills = new HashSet<>();
    }

    private String mName;
    public String getName() {
        return mName;
    }
    public void setName(String value) {
        mName = value;
    }

    private String mSize;
    public String getSize() {
        return mSize;
    }
    public void setSize(String value) {
        mSize = value;
    }

    private String mType;
    public String getType() {
        return mType;
    }
    public void setType(String value) {
        mType = value;
    }

    private String mTag;
    public String getTag() {
        return mTag;
    }
    public void setTag(String value) {
        mTag = value;
    }

    private String mAlignment;
    public String getAlignment() {
        return mAlignment;
    }
    public void setAlignment(String value) {
        mAlignment = value;
    }

    public String getMeta() {
        StringBuilder sb = new StringBuilder();
        boolean isFirstOutput = true;
        String size = getSize();
        if (!StringHelper.isNullOrEmpty(size)) {
            sb.append(size);
            isFirstOutput = false;
        }

        String type = getType();
        if (!StringHelper.isNullOrEmpty(type)) {
            if (!isFirstOutput) {
                sb.append(" ");
            }
            sb.append(type);
            isFirstOutput = false;
        }

        String tag = getTag();
        if (!StringHelper.isNullOrEmpty(tag)) {
            if (!isFirstOutput) {
                sb.append(" ");
            }
            sb.append("(");
            sb.append(tag);
            sb.append(")");
            isFirstOutput = false;
        }

        String alignment = getAlignment();
        if (!StringHelper.isNullOrEmpty(alignment)) {
            if (!isFirstOutput) {
                sb.append(", ");
            }
            sb.append(alignment);
        }

        return sb.toString();
    }

    public int getAbilityScore(String abilityScoreName) {
        if ("strength".equals(abilityScoreName) || "str".equals(abilityScoreName)) {
            return getStrengthScore();
        } else if ("dexterity".equals(abilityScoreName) || "dex".equals(abilityScoreName)) {
            return getDexterityScore();
        } else if ("constitution".equals(abilityScoreName) || "con".equals(abilityScoreName)) {
            return getConstitutionScore();
        } else if ("intelligence".equals(abilityScoreName) || "int".equals(abilityScoreName)) {
            return getIntelligenceScore();
        } else if ("wisdom".equals(abilityScoreName) || "wis".equals(abilityScoreName)) {
            return getWisdomScore();
        } else if ("charisma".equals(abilityScoreName) || "cha".equals(abilityScoreName)) {
            return getCharismaScore();
        } else {
            return 10;
        }
    }

    public static int getAbilityModifierForScore(int score) {
        return (int)Math.floor((score-10)/2.0);
    }

    public int getAbilityModifier(String abilityScoreName) {
        int score = getAbilityScore(abilityScoreName);
        return getAbilityModifierForScore(score);
    }

    private int mStrengthScore;
    public int getStrengthScore() {
        return mStrengthScore;
    }
    public void setStrengthScore(int value) {
        mStrengthScore = value;
    }
    public int getStrengthModifier() {
        return getAbilityModifierForScore(getStrengthScore());
    }

    private int mDexterityScore;
    public int getDexterityScore() {
        return mDexterityScore;
    }
    public void setDexterityScore(int value) {
        mDexterityScore = value;
    }
    public int getDexterityModifier() {
        return getAbilityModifierForScore(getDexterityScore());
    }

    private int mConstitutionScore;
    public int getConstitutionScore() {
        return mConstitutionScore;
    }
    public void setConstitutionScore(int value) {
        mConstitutionScore = value;
    }
    public int getConstitutionModifier() {
        return getAbilityModifierForScore(getConstitutionScore());
    }

    private int mIntelligenceScore;
    public int getIntelligenceScore() {
        return mIntelligenceScore;
    }
    public void setIntelligenceScore(int value) {
        mIntelligenceScore = value;
    }
    public int getIntelligenceModifier() {
        return getAbilityModifierForScore(getIntelligenceScore());
    }

    private int mWisdomScore;
    public int getWisdomScore() {
        return mWisdomScore;
    }
    public void setWisdomScore(int value) {
        mWisdomScore = value;
    }
    public int getWisdomModifier() {
        return getAbilityModifierForScore(getWisdomScore());
    }

    private int mCharismaScore;
    public int getCharismaScore() {
        return mCharismaScore;
    }
    public void setCharismaScore(int value) {
        mCharismaScore = value;
    }
    public int getCharismaModifier() {
        return getAbilityModifierForScore(getCharismaScore());
    }

    private String mArmorName;
    public String getArmorName() {
        return mArmorName;
    }
    public void setArmorName(String value) {
        mArmorName = value;
    }

    private int mShieldBonus;
    public int getShieldBonus() {
        return mShieldBonus;
    }
    public void setShieldBonus(int value) {
        mShieldBonus = value;
    }

    private int mNaturalArmorBonus;
    public int getNaturalArmorBonus() {
        return mNaturalArmorBonus;
    }
    public void setNaturalArmorBonus(int value) {
        mNaturalArmorBonus = value;
    }

    private String mOtherArmorDescription;
    public String getOtherArmorDescription() {
        return mOtherArmorDescription;
    }
    public void setOtherArmorDescription(String value) {
        mOtherArmorDescription = value;
    }

    public String getArmorClass() {
        boolean hasShield = getShieldBonus() != 0;
        String armorName = getArmorName();
        if (StringHelper.isNullOrEmpty(armorName) || "none".equals(armorName)) {
            // 10 + dexMod + 2 for shieldBonus "15" or "17 (shield)"
            return String.format(Locale.US, "%d%s", BASE_ARMOR_CLASS + getDexterityModifier() + getShieldBonus(), hasShield ? " (shield)" : "");
        } else if("natural armor".equals(armorName)) {
            // 10 + dexMod + naturalArmorBonus + 2 for shieldBonus "16 (natural armor)" or "18 (natural armor, shield)"
            return String.format(Locale.US, "%d (natural armor%s)", BASE_ARMOR_CLASS + getDexterityModifier() + getNaturalArmorBonus() + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("mage armor".equals(armorName)) {
            // 10 + dexMod + 2 for shield + 3 for mage armor "15 (18 with mage armor)" or 17 (shield, 20 with mage armor)
            return String.format(Locale.US, "%d (%s%d with mage armor)", BASE_ARMOR_CLASS + getDexterityModifier() + getShieldBonus(), hasShield ? "shield, " : "", MAGE_ARMOR_ARMOR_CLASS + getDexterityModifier() + getShieldBonus());
        } else if ("padded".equals(armorName)) {
            // 11 + dexMod + 2 for shield "18 (padded armor, shield)"
            return String.format(Locale.US, "%d (padded%s)", PADDED_ARMOR_ARMOR_CLASS + getDexterityModifier() + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("leather".equals(armorName)) {
            // 11 + dexMod + 2 for shield "18 (leather, shield)"
            return String.format(Locale.US, "%d (leather%s)", LEATHER_ARMOR_CLASS + getDexterityModifier() + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("studded".equals(armorName)) {
            // 12 + dexMod +2 for shield "17 (studded leather)"
            return String.format(Locale.US, "%d (studded leather%s)", STUDDED_LEATHER_ARMOR_CLASS + getDexterityModifier() + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("hide".equals(armorName)) {
            // 12 + Min(2, dexMod) + 2 for shield "12 (hide armor)"
            return String.format(Locale.US, "%d (hide%s)", HIDE_ARMOR_CLASS + Math.min(2, getDexterityModifier()) + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("chain shirt".equals(armorName)) {
            // 13 + Min(2, dexMod) + 2 for shield "12 (chain shirt)"
            return String.format(Locale.US, "%d (chain shirt%s)", CHAIN_SHIRT_ARMOR_CLASS + Math.min(2, getDexterityModifier()) + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("scale mail".equals(armorName)) {
            // 14 + Min(2, dexMod) + 2 for shield "14 (scale mail)"
            return String.format(Locale.US, "%d (scale mail%s)", SCALE_MAIL_ARMOR_CLASS + Math.min(2, getDexterityModifier()) + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("breastplate".equals(armorName)) {
            // 14 + Min(2, dexMod) + 2 for shield "16 (breastplate)"
            return String.format(Locale.US, "%d (breastplate%s)", BREASTPLATE_ARMOR_CLASS +Math.min(2, getDexterityModifier()) + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("half plate".equals(armorName)) {
            // 15 + Min(2, dexMod) + 2 for shield "17 (half plate)"
            return String.format(Locale.US, "%d (half plate%s)", HALF_PLATE_ARMOR_CLASS + Math.min(2, getDexterityModifier()) + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("ring mail".equals(armorName)) {
            // 14 + 2 for shield "14 (ring mail)
            return String.format(Locale.US, "%d (ring mail%s)", RING_MAIL_ARMOR_CLASS + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("chain mail".equals(armorName)) {
            // 16 + 2 for shield "16 (chain mail)"
            return String.format(Locale.US, "%d (chain mail%s)", CHAIN_MAIL_ARMOR_CLASS + getShieldBonus(), hasShield ? ", shield" : "");
        } else if ("splint".equals(armorName)) {
            // 17 + 2 for shield "17 (splint)"
            return String.format(Locale.US, "%d (splint%s)", SPLINT_ARMOR_CLASS + getShieldBonus(), hasShield ? ", shield": "");
        } else if ("plate".equals(armorName)) {
            // 18 + 2 for shield "18 (plate)"
            return String.format(Locale.US, "%d (plate%s)", PLATE_ARMOR_CLASS + getShieldBonus(), hasShield ? ", shield": "");
        } else if ("other".equals(armorName)) {
            // pure string value shield check does nothing just copies the string from otherArmorDesc
            return getOtherArmorDescription();
        } else {
            return "";
        }
    }

    private static final int BASE_ARMOR_CLASS = 10;
    private static final int MAGE_ARMOR_ARMOR_CLASS = BASE_ARMOR_CLASS + 3;
    private static final int PADDED_ARMOR_ARMOR_CLASS = BASE_ARMOR_CLASS + 1;
    private static final int LEATHER_ARMOR_CLASS = BASE_ARMOR_CLASS + 1;
    private static final int STUDDED_LEATHER_ARMOR_CLASS = BASE_ARMOR_CLASS + 2;
    private static final int HIDE_ARMOR_CLASS = BASE_ARMOR_CLASS + 2;
    private static final int CHAIN_SHIRT_ARMOR_CLASS = BASE_ARMOR_CLASS + 3;
    private static final int SCALE_MAIL_ARMOR_CLASS = BASE_ARMOR_CLASS + 4;
    private static final int BREASTPLATE_ARMOR_CLASS = BASE_ARMOR_CLASS + 4;
    private static final int HALF_PLATE_ARMOR_CLASS = BASE_ARMOR_CLASS + 5;
    private static final int RING_MAIL_ARMOR_CLASS = BASE_ARMOR_CLASS + 4;
    private static final int CHAIN_MAIL_ARMOR_CLASS = BASE_ARMOR_CLASS + 6;
    private static final int SPLINT_ARMOR_CLASS = BASE_ARMOR_CLASS + 7;
    private static final int PLATE_ARMOR_CLASS = BASE_ARMOR_CLASS + 8;

    private int mHitDice;
    public int getHitDice() {
        return mHitDice;
    }
    public void setHitDice(int value) {
        mHitDice = value;
    }

    private boolean mCustomHP;
    public boolean getCustomHP() {
        return mCustomHP;
    }
    public void setCustomHP(boolean value) {
        mCustomHP = value;
    }

    private String mHPText;
    public String getHPText() {
        return mHPText;
    }
    public void setHPText(String value) {
        mHPText = value;
    }

    public String getHitPoints() {
        if (getCustomHP()) {
            return getHPText();
        } else {
            int hitDice = getHitDice();
            int dieSize = getHitDieForSize(getSize());
            int conMod = getConstitutionModifier();
            int hpTotal = (int) Math.max(1, Math.ceil(hitDice * ((dieSize + 1) / 2.0 + conMod)));
            return String.format(Locale.US, "%d (%dd%d %+d)", hpTotal, hitDice, dieSize, conMod * hitDice);
        }
    }

    private static int getHitDieForSize(String size) {
        if ("tiny".equals(size)) {
            return 4;
        } else if ("small".equals(size)) {
            return 6;
        } else if ("medium".equals(size)) {
            return 8;
        } else if ("large".equals(size)) {
            return 10;
        } else if ("huge".equals(size)) {
            return 12;
        } else if ("gargantuan".equals(size)) {
            return 20;
        } else {
            return 8;
        }
    }

    private String mSpeed;
    public String getSpeed() {
        return mSpeed;
    }
    public void setSpeed(String value) {
        mSpeed = value;
    }

    private String mBurrowSpeed;
    public String getBurrowSpeed() {
        return mBurrowSpeed;
    }
    public void setBurrowSpeed(String value) {
        mBurrowSpeed = value;
    }

    private String mClimbSpeed;
    public String getClimbSpeed() {
        return mClimbSpeed;
    }
    public void setClimbSpeed(String value) {
        mClimbSpeed = value;
    }

    private String mFlySpeed;
    public String getFlySpeed() {
        return mFlySpeed;
    }
    public void setFlySpeed(String value) {
        mFlySpeed = value;
    }

    private boolean mHover;
    public boolean getHover() {
        return mHover;
    }
    public void setHover(boolean value) {
        mHover = value;
    }

    private String mSwimSpeed;
    public String getSwimSpeed() {
        return mSwimSpeed;
    }
    public void setSwimSpeed(String value) {
        mSwimSpeed = value;
    }

    private boolean mCustomSpeed;
    public boolean getCustomSpeed() {
        return mCustomSpeed;
    }
    public void setCustomSpeed(boolean value) {
        mCustomSpeed = value;
    }

    private String mSpeedDescription;
    public String getSpeedDescription() {
        return mSpeedDescription;
    }
    public void setSpeedDescription(String value) {
        mSpeedDescription = value;
    }

    public String getSpeedText() {
        if (getCustomSpeed()) {
            return getSpeedDescription();
        } else {
            ArrayList<String> speedParts = new ArrayList<>();
            speedParts.add(String.format("%s ft.", getSpeed()));
            String burrowSpeed = getBurrowSpeed();
            if (!StringHelper.isNullOrEmpty(burrowSpeed) && !"0".equals(burrowSpeed)) {
                speedParts.add(String.format("burrow %s ft.", burrowSpeed));
            }

            String climbSpeed = getClimbSpeed();
            if (!StringHelper.isNullOrEmpty(climbSpeed) && !"0".equals(climbSpeed)) {
                speedParts.add(String.format("climb %s ft.", climbSpeed));
            }

            String flySpeed = getFlySpeed();
            if (!StringHelper.isNullOrEmpty(flySpeed) && !"0".equals(flySpeed)) {
                speedParts.add(String.format("fly %s ft.%s", flySpeed, getHover() ? " (hover)" : ""));
            }

            String swimSpeed = getSwimSpeed();
            if (!StringHelper.isNullOrEmpty(swimSpeed) && !"0".equals(swimSpeed)) {
                speedParts.add(String.format("swim %s ft.", swimSpeed));
            }

            return StringHelper.join(", ", speedParts);
        }
    }

    public String getStrengthDescription() {
        return String.format(Locale.US, "%d (%+d)", getStrengthScore(), getStrengthModifier());
    }

    public String getDexterityDescription() {
        return String.format(Locale.US, "%d (%+d)", getDexterityScore(), getDexterityModifier());
    }

    public String getConstitutionDescription() {
        return String.format(Locale.US, "%d (%+d)", getConstitutionScore(), getConstitutionModifier());
    }

    public String getIntelligenceDescription() {
        return String.format(Locale.US, "%d (%+d)", getIntelligenceScore(), getIntelligenceModifier());
    }

    public String getWisdomDescription() {
        return String.format(Locale.US, "%d (%+d)", getWisdomScore(), getWisdomModifier());
    }

    public String getCharismaDescription() {
        return String.format(Locale.US, "%d (%+d)", getCharismaScore(), getCharismaModifier());
    }

    private HashSet<SavingThrow> mSavingThrows;
    public Set<SavingThrow> getSavingThrows() {
        return mSavingThrows;
    }
    public void addSavingThrow(SavingThrow savingThrow) {
        mSavingThrows.add(savingThrow);
    }
    public void removeSavingThrow(SavingThrow savingThrow) {
        mSavingThrows.remove(savingThrow);
    }
    public void clearSavingThrows() {
        mSavingThrows.clear();
    }

    public String getSavingThrowsDescription() {
        SavingThrow[] elements = new SavingThrow[mSavingThrows.size()];
        elements = mSavingThrows.toArray(elements);
        Arrays.sort(elements);
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (SavingThrow st : elements) {
            if (!isFirst) {
                sb.append(", ");
            }
            String name = st.getName();

            sb.append(String.format(Locale.US, "%s%s %+d", name.substring(0,1).toUpperCase(Locale.US), name.substring(1), getAbilityModifier(name) + getProficiencyBonus()));
            isFirst = false;
        }
        return sb.toString();
    }

    public int getProficiencyBonus() {
        String challengeRating = getChallengeRating();
        if ("*".equals(challengeRating)) {
            return getCustomProficiencyBonus();
        } else if (
            "0".equals(challengeRating) ||
            "1/8".equals(challengeRating) ||
            "1/4".equals(challengeRating) ||
            "1/2".equals(challengeRating) ||
            "1".equals(challengeRating) ||
            "2".equals(challengeRating) ||
            "3".equals(challengeRating) ||
            "4".equals(challengeRating)
        ) {
            return 2;
        } else if (
            "5".equals(challengeRating) ||
            "6".equals(challengeRating) ||
            "7".equals(challengeRating) ||
            "8".equals(challengeRating)
        ) {
            return 3;
        } else if (
            "9".equals(challengeRating) ||
            "10".equals(challengeRating) ||
            "11".equals(challengeRating) ||
            "12".equals(challengeRating)
        ) {
            return 4;
        } else if (
            "13".equals(challengeRating) ||
            "14".equals(challengeRating) ||
            "15".equals(challengeRating) ||
            "16".equals(challengeRating)
        ) {
            return 5;
        } else if (
            "17".equals(challengeRating) ||
            "18".equals(challengeRating) ||
            "19".equals(challengeRating) ||
            "20".equals(challengeRating)
        ) {
            return 6;
        } else if (
            "21".equals(challengeRating) ||
            "22".equals(challengeRating) ||
            "23".equals(challengeRating) ||
            "24".equals(challengeRating)
        ) {
            return 7;
        } else if (
            "25".equals(challengeRating) ||
            "26".equals(challengeRating) ||
            "27".equals(challengeRating) ||
            "28".equals(challengeRating)
        ) {
            return 8;
        } else if (
            "29".equals(challengeRating) ||
            "30".equals(challengeRating)
        ) {
            return 9;
        } else {
            return 0;
        }
    }

    private String mChallengeRating;
    public String getChallengeRating() {
        return mChallengeRating;
    }
    public void setChallengeRating(String challengeRating) {
        mChallengeRating = challengeRating;
        // TODO: update proficiency bonus based on CR
    }

    private String mCustomChallengeRating;
    public String getCustomChallengeRating() {
        return mCustomChallengeRating;
    }
    public void setCustomChallengeRating(String challengeRating) {
        mCustomChallengeRating = challengeRating;
    }

    private int mCustomProficiencyBonus;
    public int getCustomProficiencyBonus() {
        return mCustomProficiencyBonus;
    }
    public void setCustomProficiencyBonus(int proficiencyBonus) {
        mCustomProficiencyBonus = proficiencyBonus;
    }

    private HashSet<Skill> mSkills;
    public Set<Skill> getSkills() {
        return mSkills;
    }
    public void addSkill(Skill skill) {
        mSkills.add(skill);
    }
    public void removeSkill(Skill skill) {
        mSkills.remove(skill);
    }
    public void clearSkill(Skill skill) {
        mSkills.clear();
    }

    public String getSkillsDescription() {
        Skill[] elements = new Skill[mSkills.size()];
        elements = mSkills.toArray(elements);
        Arrays.sort(elements);
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Skill skill : elements) {
            if (!isFirst) {
                sb.append(", ");
            }
            String name = skill.getName();
            sb.append(skill.getText(this));
            isFirst = false;
        }
        return sb.toString();
    }

    private HashSet<DamageType> mDamageTypes;
    public Set<DamageType> getDamageTypes() {
        return mDamageTypes;
    }
    public void addDamageType(DamageType damageType) {
        // TODO: make this remove the damage type with the same name if it exists first
        mDamageTypes.add(damageType);
    }
    public void removeDamageType(DamageType damageType) {
        mDamageTypes.remove(damageType);
    }
    public void clearDamageTypes() {
        mDamageTypes.clear();
    }

    public String getDamageVulnerabilitiesDescription() {
        ArrayList<String> vulnerabilities = new ArrayList<>();
        for (DamageType damageType : mDamageTypes) {
            if (damageType != null && "v".equals(damageType.getType()) && !StringHelper.isNullOrEmpty(damageType.getName())) {
                vulnerabilities.add(damageType.getName());
            }
        }
        Collections.sort(vulnerabilities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", vulnerabilities);
    }
    public String getDamageResistancesDescription() {
        ArrayList<String> vulnerabilities = new ArrayList<>();
        for (DamageType damageType : mDamageTypes) {
            if (damageType != null && "r".equals(damageType.getType()) && !StringHelper.isNullOrEmpty(damageType.getName())) {
                vulnerabilities.add(damageType.getName());
            }
        }
        Collections.sort(vulnerabilities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", vulnerabilities);
    }
    public String getDamageImmunitiesDescription() {
        ArrayList<String> vulnerabilities = new ArrayList<>();
        for (DamageType damageType : mDamageTypes) {
            if (damageType != null && "i".equals(damageType.getType()) && !StringHelper.isNullOrEmpty(damageType.getName())) {
                vulnerabilities.add(damageType.getName());
            }
        }
        Collections.sort(vulnerabilities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", vulnerabilities);
    }

    private HashSet<String> mConditionImmunities;
    public Set<String> getConditionImmunities() {
        return mConditionImmunities;
    }
    public void addConditionImmunity(String condition) {
        // TODO: filter out duplicates
        mConditionImmunities.add(condition);
    }
    public void removeConditionImmunity(String condition) {
        // TODO: make sure this works even though we're using strings
        mConditionImmunities.remove(condition);
    }
    public void clearConditionImmunities() {
        mConditionImmunities.clear();
    }

    public String getConditionImmunitiesDescription() {
        ArrayList<String> immunities = new ArrayList<>(getConditionImmunities());
        Collections.sort(immunities);
        return StringHelper.oxfordJoin(", ", ", and ", " and ", immunities);
    }

    private String mBlindsight;
    public String getBlindsight() {
        return mBlindsight;
    }
    public void setBlindsight(String value) {
        mBlindsight = value;
    }

    private boolean mIsBlind;
    public boolean getIsBlind() {
        return mIsBlind;
    }
    public void setIsBlind(boolean value) {
        mIsBlind = value;
    }

    private String mDarkvision;
    public String getDarkvision() {
        return mDarkvision;
    }
    public void setDarkvision(String value) {
        mDarkvision = value;
    }

    private String mTremorsense;
    public String getTremorsense() {
        return mTremorsense;
    }
    public void setTremorsense(String value) {
        mTremorsense = value;
    }

    private String mTruesight;
    public String getTruesight() {
        return mTruesight;
    }
    public void setTruesight(String value) {
        mTruesight = value;
    }

    public String getSensesDescription() {
        ArrayList<String> parts = new ArrayList<>();

        String blindsight = getBlindsight();
        if (!StringHelper.isNullOrEmpty(blindsight) && !"0".equals(blindsight)) {
            parts.add(String.format(Locale.US, "blindsight %s ft.%s", blindsight, getIsBlind() ? " (blind beyond this radius)" : ""));
        }
        String darkvision = getDarkvision();
        if (!StringHelper.isNullOrEmpty(darkvision) && !"0".equals(darkvision)) {
            parts.add(String.format(Locale.US, "darkvision %s ft.", darkvision));
        }
        String tremorsense = getTremorsense();
        if (!StringHelper.isNullOrEmpty(tremorsense) && !"0".equals(tremorsense)) {
            parts.add(String.format(Locale.US, "tremorsense %s ft.", tremorsense));
        }
        String truesight = getTruesight();
        if (!StringHelper.isNullOrEmpty(truesight) && !"0".equals(truesight)) {
            parts.add(String.format(Locale.US, "truesight %s ft.", truesight));
        }
        parts.add(String.format(Locale.US, "passive Perception %d", 10 + getWisdomModifier()));

        return StringHelper.join(", ", parts);
    }

    private HashSet<Language> mLanguages;
    public Set<Language> getLanguages() {
        return mLanguages;
    }
    public void addLanguage(Language value) {
        mLanguages.add(value);
    }
    public void removeLanguage(Language value) {
        mLanguages.remove(value);
    }
    public void clearLanguages() {
        mLanguages.clear();
    }

    private int mTelepathy;
    public int getTelepathy() {
        return mTelepathy;
    }
    public void setTelepathy(int value) {
        mTelepathy = value;
    }

    private String mUnderstandsBut;
    public String getUnderstandsBut() {
        return mUnderstandsBut;
    }
    public void setUnderstandsBut(String value) {
        mUnderstandsBut = value;
    }

    public String getLanguagesDescription() {
        ArrayList<String> spokenLanguages = new ArrayList<>();
        ArrayList<String> understoodLanguages = new ArrayList<>();
        for (Language language : mLanguages) {
            if (language != null) {
                if (language.getSpeaks()) {
                    spokenLanguages.add(language.getName());
                } else {
                    understoodLanguages.add(language.getName());
                }
            }
        }
        Collections.sort(spokenLanguages);
        Collections.sort(understoodLanguages);

        String spokenLanguagesString = StringHelper.oxfordJoin(", ", ", and ", " and ", spokenLanguages);
        String understoodLanguagesString = StringHelper.oxfordJoin(", ", ", and ", " and ", understoodLanguages);

        String understandsBut = getUnderstandsBut();
        boolean hasUnderstandsBut = understandsBut.length() > 0;
        int telepathy = getTelepathy();
        boolean hasTelepathy = telepathy > 0;
        String telepathyString = String.format(Locale.US, ", telepathy %d ft.", telepathy);

        if (spokenLanguages.size() > 0) {
            if (understoodLanguages.size() > 0) {
                return String.format(
                        "%s, understands %s%s%s",
                        spokenLanguagesString,
                        understoodLanguagesString,
                        hasUnderstandsBut ? " but " + understandsBut : "",
                        hasTelepathy ? telepathyString : "");
            } else {
                return String.format(
                        "%s%s%s",
                        spokenLanguagesString,
                        hasUnderstandsBut ? " but " + understandsBut : "",
                        hasTelepathy ? telepathyString : "");
            }
        } else {
            if (understoodLanguages.size() > 0) {
                return String.format(
                        "understands %s%s%s",
                        understoodLanguagesString,
                        hasUnderstandsBut ? " but " + understandsBut : "",
                        hasTelepathy ? telepathyString : "");
            } else {
                return String.format(
                        "%S%s",
                        hasUnderstandsBut ? "none but " + understandsBut : "",
                        hasTelepathy ? telepathyString : "");
            }
        }
    }

    public String getChallengeRatingDescription() {
        String challengeRating = getChallengeRating();
        if ("*".equals(challengeRating)) {
            // Custom CR
            return getCustomChallengeRating();
        } else if ("0".equals(challengeRating)) {
            return "0 (10 XP)";
        } else if ("1/8".equals(challengeRating)) {
            return "1/8 (25 XP)";
        } else if ("1/4".equals(challengeRating)) {
            return "1/4 (50 XP)";
        } else if ("1/2".equals(challengeRating)) {
            return "1/2 (100 XP)";
        } else if ("1".equals(challengeRating)) {
            return "1 (200 XP)";
        } else if ("2".equals(challengeRating)) {
            return "2 (450 XP)";
        } else if ("3".equals(challengeRating)) {
            return "3 (700 XP)";
        } else if ("4".equals(challengeRating)) {
            return "4 (1,100 XP)";
        } else if ("5".equals(challengeRating)) {
            return "5 (1,800 XP)";
        } else if ("6".equals(challengeRating)) {
            return "6 (2,300 XP)";
        } else if ("7".equals(challengeRating)) {
            return "7 (2,900 XP)";
        } else if ("8".equals(challengeRating)) {
            return "8 (3,900 XP)";
        } else if ("9".equals(challengeRating)) {
            return "9 (5,000 XP)";
        } else if ("10".equals(challengeRating)) {
            return "10 (5,900 XP)";
        } else if ("11".equals(challengeRating)) {
            return "11 (7,200 XP)";
        } else if ("12".equals(challengeRating)) {
            return "12 (8,400 XP)";
        } else if ("13".equals(challengeRating)) {
            return "13 (10,000 XP)";
        } else if ("14".equals(challengeRating)) {
            return "14 (11,500 XP)";
        } else if ("15".equals(challengeRating)) {
            return "15 (13,000 XP)";
        } else if ("16".equals(challengeRating)) {
            return "16 (15,000 XP)";
        } else if ("17".equals(challengeRating)) {
            return "17 (18,000 XP)";
        } else if ("18".equals(challengeRating)) {
            return "18 (20,000 XP)";
        } else if ("19".equals(challengeRating)) {
            return "19 (22,000 XP)";
        } else if ("20".equals(challengeRating)) {
            return "20 (25,000 XP)";
        } else if ("21".equals(challengeRating)) {
            return "21 (33,000 XP)";
        } else if ("22".equals(challengeRating)) {
            return "22 (41,000 XP)";
        } else if ("23".equals(challengeRating)) {
            return "23 (50,000 XP)";
        } else if ("24".equals(challengeRating)) {
            return "24 (62,000 XP)";
        } else if ("25".equals(challengeRating)) {
            return "25 (75,000 XP)";
        } else if ("26".equals(challengeRating)) {
            return "26 (90,000 XP)";
        } else if ("27".equals(challengeRating)) {
            return "27 (105,000 XP)";
        } else if ("28".equals(challengeRating)) {
            return "28 (120,000 XP)";
        } else if ("29".equals(challengeRating)) {
            return "29 (135,000 XP)";
        } else if ("30".equals(challengeRating)) {
            return "30 (155,000 XP)";
        } else {
            return getCustomChallengeRating();
        }
    }

    private ArrayList<Ability> mAbilities;
    public List<Ability> getAbilities() {
        return mAbilities;
    }
    public void addAbility(Ability ability) {
        mAbilities.add(ability);
    }
    public void removeAbility(Ability ability) {
        mAbilities.remove(ability);
    }
    public void clearAbilities() {
        mAbilities.clear();
    }

    public List<String> getAbilityDescriptions() {
        ArrayList<String> abilities = new ArrayList<>();
        for (Ability ability : getAbilities()) {
            abilities.add(getPlaceholderReplacedText(String.format("__%s__ %s", ability.getName(), ability.getDescription())));
        }
        return abilities;
    }

    public String getPlaceholderReplacedText(String rawText) {
        return rawText
                .replaceAll("\\[STR SAVE]", String.format(Locale.US, "%+d", getSpellSaveDC("strength")))
                .replaceAll("\\[STR ATK]", String.format(Locale.US, "%+d", getAttackBonus("strength")))
                .replaceAll("\\[DEX SAVE]", String.format(Locale.US, "%+d", getSpellSaveDC("dexterity")))
                .replaceAll("\\[DEX ATK]", String.format(Locale.US, "%+d", getAttackBonus("dexterity")))
                .replaceAll("\\[CON SAVE]", String.format(Locale.US, "%+d", getSpellSaveDC("constitution")))
                .replaceAll("\\[CON ATK]", String.format(Locale.US, "%+d", getAttackBonus("constitution")))
                .replaceAll("\\[INT SAVE]", String.format(Locale.US, "%+d", getSpellSaveDC("intelligence")))
                .replaceAll("\\[INT ATK]", String.format(Locale.US, "%+d", getAttackBonus("intelligence")))
                .replaceAll("\\[WIS SAVE]", String.format(Locale.US, "%+d", getSpellSaveDC("wisdom")))
                .replaceAll("\\[WIS ATK]", String.format(Locale.US, "%+d", getAttackBonus("wisdom")))
                .replaceAll("\\[CHA SAVE]", String.format(Locale.US, "%+d", getSpellSaveDC("charisma")))
                .replaceAll("\\[CHA ATK]", String.format(Locale.US, "%+d", getAttackBonus("charisma")));
    }

    public int getSavingThrow(String name) {
        Set<SavingThrow> sts = getSavingThrows();
        for(SavingThrow st : sts) {
            if (name.equals(st.getName())) {
                return getAbilityModifier(name) + getProficiencyBonus();
            }
        }
        return getAbilityModifier(name);
    }

    public String getWisdomSave() {
        return String.format(Locale.US, "%+d", getSavingThrow("wis"));
    }

    public int getSpellSaveDC(String abilityScoreName) {
        return 8 + getProficiencyBonus() + getAbilityModifier(abilityScoreName);
    }

    public int getAttackBonus(String abilityScoreName) {
        return getProficiencyBonus() + getAbilityModifier(abilityScoreName);
    }

}
