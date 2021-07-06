package com.majinnaibu.monstercards.data.converters;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.majinnaibu.monstercards.data.enums.ArmorType;

public class ArmorTypeConverter {

    @TypeConverter
    public static String fromArmorType(@NonNull ArmorType armorType) {
        return armorType.stringValue;
    }

    @TypeConverter
    public static ArmorType armorTypeFromStringValue(String stringValue) {
        return ArmorType.valueOfString(stringValue);
    }
}
