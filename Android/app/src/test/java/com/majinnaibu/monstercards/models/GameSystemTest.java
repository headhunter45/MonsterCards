package com.majinnaibu.monstercards.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.majinnaibu.monstercards.data.enums.GameSystem;

import org.junit.Test;

public class GameSystemTest {

    @Test
    public void testGameSystemEnumValues() {
        assertEquals("5e", GameSystem.DND_5E.getShortName());
        assertEquals("PF2e", GameSystem.PF_2E.getShortName());
        assertEquals("SF2e", GameSystem.SF_2E.getShortName());
        assertEquals("Custom", GameSystem.CUSTOM.getShortName());

        assertEquals(GameSystem.PF_2E, GameSystem.fromString("PF2e"));
        assertEquals(GameSystem.PF_2E, GameSystem.fromString("Pathfinder 2e"));
        assertEquals(GameSystem.SF_2E, GameSystem.fromString("SF2e"));
        assertEquals(GameSystem.DND_5E, GameSystem.fromString("Unknown"));
    }

    @Test
    public void testMonsterSourceTagFormatting() {
        Monster monster = new Monster();
        monster.gameSystem = GameSystem.PF_2E;
        monster.sourceLabel = "Bestiary";
        assertEquals("PF2e | Bestiary", monster.getSourceTag());

        monster.sourceLabel = "";
        assertEquals("PF2e", monster.getSourceTag());

        monster.gameSystem = GameSystem.DND_5E;
        monster.sourceLabel = "SRD";
        assertEquals("5e | SRD", monster.getSourceTag());
    }

    @Test
    public void testMonsterJsonSerializationWithGameSystem() {
        Monster monster = new Monster();
        monster.name = "Goblin Warrior";
        monster.gameSystem = GameSystem.PF_2E;
        monster.sourceLabel = "Bestiary 1";

        Gson gson = new Gson();
        String json = gson.toJson(monster);
        Monster deserialized = gson.fromJson(json, Monster.class);

        assertNotNull(deserialized);
        assertEquals(GameSystem.PF_2E, deserialized.gameSystem);
        assertEquals("Bestiary 1", deserialized.sourceLabel);
    }
}
