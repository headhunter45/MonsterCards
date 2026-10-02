package com.majinnaibu.monstercards.importers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.majinnaibu.monstercards.helpers.MonsterImportHelper;
import com.majinnaibu.monstercards.models.Monster;

import org.junit.Test;

import java.util.List;

public class MonsterImportHelperTest {

    @Test
    public void testListFromJSON_SingleMonster() {
        String json = "{\n" +
                "  \"name\": \"Goblin\",\n" +
                "  \"size\": \"Small\",\n" +
                "  \"type\": \"Humanoid\",\n" +
                "  \"hit_dice\": \"2d6\",\n" +
                "  \"armor_class\": 15,\n" +
                "  \"challenge_rating\": \"1/4\"\n" +
                "}";
        List<Monster> monsters = MonsterImportHelper.listFromJSON(json, "goblin.json");
        assertNotNull(monsters);
        assertEquals(1, monsters.size());
        assertEquals("Goblin", monsters.get(0).name);
    }

    @Test
    public void testListFromJSON_MonsterArray() {
        String json = "[\n" +
                "  {\n" +
                "    \"name\": \"Goblin\",\n" +
                "    \"size\": \"Small\",\n" +
                "    \"type\": \"Humanoid\",\n" +
                "    \"hit_dice\": \"2d6\",\n" +
                "    \"armor_class\": 15\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Orc\",\n" +
                "    \"size\": \"Medium\",\n" +
                "    \"type\": \"Humanoid\",\n" +
                "    \"hit_dice\": \"2d8\",\n" +
                "    \"armor_class\": 13\n" +
                "  }\n" +
                "]";
        List<Monster> monsters = MonsterImportHelper.listFromJSON(json, "monsters.json");
        assertNotNull(monsters);
        assertEquals(2, monsters.size());
        assertEquals("Goblin", monsters.get(0).name);
        assertEquals("Orc", monsters.get(1).name);
    }
}
