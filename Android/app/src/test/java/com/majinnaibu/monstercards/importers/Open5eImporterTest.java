package com.majinnaibu.monstercards.importers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.majinnaibu.monstercards.exporters.Open5eExporter;
import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.models.Monster;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

public class Open5eImporterTest {

    private Open5eImporter importer;
    private Open5eExporter exporter;

    @Before
    public void setUp() {
        importer = new Open5eImporter();
        exporter = new Open5eExporter();
    }

    @Test
    public void testParseGoblinSampleJson() throws Exception {
        File sampleFile = new File("/Users/tom/Projects/TTRPG/CharacterDataFiles/rulesets/open5e/examples/character/goblin.json");
        if (!sampleFile.exists()) {
            sampleFile = new File("../../../CharacterDataFiles/rulesets/open5e/examples/character/goblin.json");
        }
        if (!sampleFile.exists()) {
            sampleFile = new File("../../CharacterDataFiles/rulesets/open5e/examples/character/goblin.json");
        }
        assertTrue("Goblin sample JSON file should exist", sampleFile.exists());

        String json = new String(Files.readAllBytes(sampleFile.toPath()));
        assertTrue(importer.canImport(json));

        Monster monster = importer.parse(json);
        assertNotNull(monster);
        assertEquals("Goblin", monster.name);
        assertEquals("small", monster.size);
        assertEquals("Humanoid", monster.type);
        assertEquals("goblinoid", monster.subtype);
        assertEquals("neutral evil", monster.alignment);

        assertEquals(8, monster.strengthScore);
        assertEquals(14, monster.dexterityScore);
        assertEquals(10, monster.constitutionScore);
        assertEquals(10, monster.intelligenceScore);
        assertEquals(8, monster.wisdomScore);
        assertEquals(8, monster.charismaScore);

        assertEquals(30, monster.walkSpeed);
        assertEquals(2, monster.hitDice);

        assertEquals(1, monster.abilities.size());
        assertEquals("Nimble Escape", monster.abilities.get(0).name);

        assertEquals(2, monster.actions.size());
        assertEquals("Scimitar", monster.actions.get(0).name);
        assertEquals("Shortbow", monster.actions.get(1).name);

        Monster delegatedMonster = MonsterImportHelper.fromJSON(json);
        assertNotNull(delegatedMonster);
        assertEquals("Goblin", delegatedMonster.name);
    }

    @Test
    public void testRoundTrip_ExportAndImport() throws Exception {
        Monster monster = new Monster();
        monster.name = "Custom Dragon";
        monster.size = "Huge";
        monster.type = "Dragon";
        monster.strengthScore = 23;
        monster.dexterityScore = 10;
        monster.constitutionScore = 21;
        monster.intelligenceScore = 14;
        monster.wisdomScore = 13;
        monster.charismaScore = 17;
        monster.playerName = "DM";
        monster.background = "Ancient";

        String open5eJson = exporter.exportMonster(monster);
        assertTrue(importer.canImport(open5eJson));

        Monster importedMonster = importer.parse(open5eJson);
        assertNotNull(importedMonster);
        assertEquals("Custom Dragon", importedMonster.name);
        assertEquals("Huge", importedMonster.size);
        assertEquals("Dragon", importedMonster.type);
        assertEquals(23, importedMonster.strengthScore);
        assertEquals(21, importedMonster.constitutionScore);
        assertEquals("DM", importedMonster.playerName);
        assertEquals("Ancient", importedMonster.background);
    }
}
