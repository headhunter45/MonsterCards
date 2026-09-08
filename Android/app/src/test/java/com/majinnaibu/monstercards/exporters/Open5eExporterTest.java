package com.majinnaibu.monstercards.exporters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.data.enums.AbilityScore;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.models.Trait;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class Open5eExporterTest {

    private Open5eExporter exporter;

    @Before
    public void setUp() {
        exporter = new Open5eExporter();
    }

    @Test
    public void testExportMonster_BasicFieldsAndEnvelope() {
        Monster monster = new Monster();
        monster.id = UUID.fromString("e5b72e61-a83d-4c3d-b4b1-e28a5a41c101");
        monster.name = "Goblin";
        monster.size = "Small";
        monster.type = "Humanoid";
        monster.subtype = "goblinoid";
        monster.alignment = "neutral evil";
        monster.strengthScore = 8;
        monster.dexterityScore = 14;
        monster.constitutionScore = 10;
        monster.intelligenceScore = 10;
        monster.wisdomScore = 8;
        monster.charismaScore = 8;
        monster.walkSpeed = 30;

        monster.playerName = "Player One";
        monster.background = "Criminal";
        monster.personalityTraits = "Always cautious";

        monster.languages.add(new Language("Common", true));
        monster.languages.add(new Language("Goblin", true));

        monster.skills.add(new Skill("Stealth", AbilityScore.DEXTERITY));

        monster.abilities.add(new Trait("Nimble Escape", "Disengage or Hide as bonus action."));
        monster.actions.add(new Trait("Scimitar", "+4 to hit, 1d6+2 slashing."));

        String json = exporter.exportMonster(monster);
        assertNotNull(json);

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        assertEquals("open5e", root.get("ruleset_id").getAsString());
        assertEquals("character", root.get("entity_type").getAsString());
        assertEquals("Goblin", root.get("display_name").getAsString());
        assertEquals("e5b72e61-a83d-4c3d-b4b1-e28a5a41c101", root.get("uuid").getAsString());

        assertTrue(root.has("properties"));
        JsonObject props = root.getAsJsonObject("properties");
        assertEquals("Goblin", props.get("name").getAsString());
        assertEquals("Small", props.get("size").getAsString());
        assertEquals("Player One", props.get("player_name").getAsString());
        assertEquals("Criminal", props.get("background").getAsString());

        JsonObject abilities = props.getAsJsonObject("abilities");
        assertEquals(8, abilities.get("strength").getAsInt());
        assertEquals(14, abilities.get("dexterity").getAsInt());

        assertTrue(props.has("skills"));
        assertTrue(props.has("languages"));
        assertTrue(props.has("traits"));
        assertTrue(props.has("actions"));
    }
}
