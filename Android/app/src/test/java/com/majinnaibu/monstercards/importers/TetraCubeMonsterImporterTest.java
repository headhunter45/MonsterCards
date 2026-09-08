package com.majinnaibu.monstercards.importers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.models.Monster;

import org.junit.Test;

public class TetraCubeMonsterImporterTest {

    private final TetraCubeMonsterImporter importer = new TetraCubeMonsterImporter();

    private static final String SAMPLE_TETRACUBE_JSON = "{"
            + "\"name\": \"Goblin\","
            + "\"size\": \"Small\","
            + "\"type\": \"humanoid\","
            + "\"tag\": \"goblinoid\","
            + "\"alignment\": \"neutral evil\","
            + "\"hitDice\": 2,"
            + "\"armorName\": \"leather\","
            + "\"shieldBonus\": 1,"
            + "\"speed\": 30,"
            + "\"strPoints\": 8,"
            + "\"dexPoints\": 14,"
            + "\"conPoints\": 10,"
            + "\"intPoints\": 10,"
            + "\"wisPoints\": 8,"
            + "\"chaPoints\": 8,"
            + "\"cr\": \"1/4\""
            + "}";

    @Test
    public void canImport_validTetraCubeJson_returnsTrue() {
        assertTrue(importer.canImport(SAMPLE_TETRACUBE_JSON));
    }

    @Test
    public void canImport_invalidJson_returnsFalse() {
        assertFalse(importer.canImport("not a json string"));
    }

    @Test
    public void canImport_emptyString_returnsFalse() {
        assertFalse(importer.canImport(""));
    }

    @Test
    public void canImport_otherJson_returnsFalse() {
        assertFalse(importer.canImport("{\"foo\": \"bar\"}"));
    }

    @Test
    public void parse_validTetraCubeJson_createsMonster() throws Exception {
        Monster monster = importer.parse(SAMPLE_TETRACUBE_JSON);
        assertNotNull(monster);
        assertEquals("Goblin", monster.name);
        assertEquals("Small", monster.size);
        assertEquals("humanoid", monster.type);
        assertEquals("goblinoid", monster.subtype);
        assertEquals("neutral evil", monster.alignment);
        assertEquals(2, monster.hitDice);
        assertEquals(30, monster.walkSpeed);
        assertEquals(8, monster.strengthScore);
        assertEquals(14, monster.dexterityScore);
    }

    @Test
    public void monsterImportHelper_fromJSON_delegatesToImporter() {
        Monster monster = MonsterImportHelper.fromJSON(SAMPLE_TETRACUBE_JSON);
        assertNotNull(monster);
        assertEquals("Goblin", monster.name);
    }
}
