package com.majinnaibu.monstercards.importers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.models.Monster;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

public class DnDBeyondImporterTest {

    private DnDBeyondImporter importer;

    @Before
    public void setUp() {
        importer = new DnDBeyondImporter();
    }

    @Test
    public void testCanImport_UrlAndNumericIdAndJson() {
        assertTrue(importer.canImport("https://www.dndbeyond.com/characters/49074997/s7sLyX"));
        assertTrue(importer.canImport("https://www.dndbeyond.com/characters/49074997"));
        assertTrue(importer.canImport("49074997"));
        assertTrue(importer.canImport("  49074997  "));
        assertFalse(importer.canImport("invalid url"));
        assertFalse(importer.canImport(""));
    }

    @Test
    public void testExtractCharacterId() {
        assertEquals("49074997", DnDBeyondImporter.extractCharacterId("https://www.dndbeyond.com/characters/49074997/s7sLyX"));
        assertEquals("49074997", DnDBeyondImporter.extractCharacterId("https://www.dndbeyond.com/characters/49074997"));
        assertEquals("49074997", DnDBeyondImporter.extractCharacterId("49074997"));
    }

    @Test
    public void testParseSampleCharacterJson() throws Exception {
        File sampleFile = new File("../docs/dndbeyond-character-sample.json");
        if (!sampleFile.exists()) {
            sampleFile = new File("../../docs/dndbeyond-character-sample.json");
        }
        assertTrue("Sample character JSON file should exist", sampleFile.exists());

        String json = new String(Files.readAllBytes(sampleFile.toPath()));
        assertTrue(importer.canImport(json));

        Monster monster = importer.parse(json);
        assertNotNull(monster);
        assertEquals("Joy", monster.name);
        assertEquals(12, monster.strengthScore);
        assertEquals(15, monster.dexterityScore);
        assertEquals(14, monster.constitutionScore);
        assertEquals(10, monster.intelligenceScore);
        assertEquals(13, monster.wisdomScore);
        assertEquals(17, monster.charismaScore);

        assertTrue(monster.hasCustomHP);
        assertEquals("28 (5d8)", monster.customHPDescription);
        assertEquals(5, monster.hitDice);

        Monster delegatedMonster = MonsterImportHelper.fromJSON(json);
        assertNotNull(delegatedMonster);
        assertEquals("Joy", delegatedMonster.name);
    }
}
