package com.majinnaibu.monstercards.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.data.enums.MonsterOrigin;

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
        assertEquals(GameSystem.CUSTOM, GameSystem.fromString("Unknown"));
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
        monster.origin = MonsterOrigin.OPEN5E;
        monster.bookSource = "Tome of Beasts";
        monster.sourceLabel = "Bestiary 1";

        Gson gson = new Gson();
        String json = gson.toJson(monster);
        Monster deserialized = gson.fromJson(json, Monster.class);

        assertNotNull(deserialized);
        assertEquals(GameSystem.PF_2E, deserialized.gameSystem);
        assertEquals(MonsterOrigin.OPEN5E, deserialized.origin);
        assertEquals("Tome of Beasts", deserialized.bookSource);
        assertEquals("Bestiary 1", deserialized.sourceLabel);
    }

    @Test
    public void testUnrecognizedGameSystemAndOrigin() {
        Monster monster = new Monster();
        monster.name = "Star Wars Droid";
        monster.gameSystem = GameSystem.fromString("StarWars_d20");
        monster.customGameSystem = "StarWars_d20";
        monster.origin = com.majinnaibu.monstercards.data.enums.MonsterOrigin.fromString("custom_site.com");
        monster.customOrigin = "custom_site.com";
        monster.bookSource = "Star Wars Core Rulebook";

        assertEquals("StarWars_d20", monster.getGameSystemShortName());
        assertEquals("custom_site.com", monster.getOriginDisplayName());
        assertEquals("StarWars_d20 | Star Wars Core Rulebook", monster.getSourceTag());
    }
}
