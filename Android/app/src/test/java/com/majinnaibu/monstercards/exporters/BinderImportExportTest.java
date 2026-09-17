package com.majinnaibu.monstercards.exporters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.majinnaibu.monstercards.importers.BinderImporter;
import com.majinnaibu.monstercards.models.BinderExport;
import com.majinnaibu.monstercards.models.Monster;

import org.junit.Test;

import java.util.Collections;
import java.util.UUID;

public class BinderImportExportTest {

    @Test
    public void testExportAndImportBinder() throws Exception {
        Monster monster = new Monster();
        monster.id = UUID.randomUUID();
        monster.name = "Goblin Warrior";
        monster.size = "Small";
        monster.type = "humanoid";

        BinderExporter exporter = new BinderExporter();
        String json = exporter.exportBinder("Goblins", Collections.singletonList(monster));

        BinderImporter importer = new BinderImporter();
        assertTrue(importer.canImport(json));

        BinderExport binder = importer.parse(json);
        assertEquals(1, binder.schemaVersion);
        assertNotNull(binder.collections);
        assertEquals(1, binder.collections.size());
        assertEquals("Goblins", binder.collections.get(0).name);
        assertEquals(1, binder.collections.get(0).cards.size());
        assertEquals("Goblin Warrior", binder.collections.get(0).cards.get(0).name);
        assertEquals(1, binder.collections.get(0).cards.get(0).schemaVersion);
    }

    @Test
    public void testExportSingleCard() {
        Monster monster = new Monster();
        monster.id = UUID.randomUUID();
        monster.name = "Dragon";

        MonsterCardExporter exporter = new MonsterCardExporter();
        String json = exporter.exportCard(monster);

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        assertTrue(obj.has("schemaVersion"));
        assertEquals(1, obj.get("schemaVersion").getAsInt());
        assertEquals("Dragon", obj.get("name").getAsString());
    }
}
