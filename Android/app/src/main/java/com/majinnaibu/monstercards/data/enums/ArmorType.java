package com.majinnaibu.monstercards.data.enums;

@SuppressWarnings("unused")
public enum ArmorType {
    NONE("none", "None", 10),
    NATURAL_ARMOR("natural armor", "Natural Armor", 10),
    MAGE_ARMOR("mage armor", "Mage Armor", 10),
    PADDED("padded", "Padded", 11),
    LEATHER("leather", "Leather", 11),
    STUDDED_LEATHER("studded", "Studded Leather", 12),
    HIDE("hide", "Hide", 12),
    CHAIN_SHIRT("chain shirt", "Chain Shirt", 13),
    SCALE_MAIL("scale mail", "Scale Mail", 14),
    BREASTPLATE("breastplate", "Breastplate", 14),
    HALF_PLATE("half plate", "Half Plate", 15),
    RING_MAIL("ring mail", "Ring Mail", 14),
    CHAIN_MAIL("chain mail", "Chain Mail", 16),
    SPLINT_MAIL("splint", "Splint Mail", 17),
    PLATE_MAIL("plate", "Plate Mail", 18),
    OTHER("other", "Other", 10),
    ;

    public final String displayName;
    public final String stringValue;
    public final int baseArmorClass;

    ArmorType(String stringValue, String displayName, int baseArmorClass) {
        this.displayName = displayName;
        this.stringValue = stringValue;
        this.baseArmorClass = baseArmorClass;
    }

    public static ArmorType valueOfString(String string) {
        for (ArmorType armorType : values()) {
            if (armorType.stringValue.equals(string)) {
                return armorType;
            }
        }
        return ArmorType.NONE;
    }
}
