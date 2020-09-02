package com.majinnaibu.monstercards.models;

import com.majinnaibu.monstercards.helpers.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Monster {

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

}
