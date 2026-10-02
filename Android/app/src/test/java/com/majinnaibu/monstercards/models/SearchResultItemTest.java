package com.majinnaibu.monstercards.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.majinnaibu.monstercards.data.enums.GameSystem;

import org.junit.Test;

import java.util.UUID;

public class SearchResultItemTest {

    @Test
    public void testSearchResultItemMonster() {
        Monster monster = new Monster();
        monster.id = UUID.randomUUID();
        monster.name = "Beholder";

        SearchResultItem item = new SearchResultItem(monster);
        assertEquals(SearchResultItem.Type.MONSTER, item.type);
        assertNotNull(item.monster);
        assertNull(item.collection);
        assertNull(item.referenceMonster);
    }

    @Test
    public void testSearchResultItemCollection() {
        Collection col = new Collection();
        col.id = UUID.randomUUID();
        col.name = "Underdark Encounters";

        SearchResultItem item = new SearchResultItem(col);
        assertEquals(SearchResultItem.Type.COLLECTION, item.type);
        assertNull(item.monster);
        assertNotNull(item.collection);
        assertNull(item.referenceMonster);
    }

    @Test
    public void testSearchResultItemReferenceMonster() {
        ReferenceMonster rm = new ReferenceMonster();
        rm.id = UUID.randomUUID().toString();
        rm.name = "Pathfinder Troll";
        rm.gameSystem = GameSystem.PF_2E;
        rm.sourceLabel = "Bestiary";

        SearchResultItem item = new SearchResultItem(rm);
        assertEquals(SearchResultItem.Type.REFERENCE_MONSTER, item.type);
        assertNull(item.monster);
        assertNull(item.collection);
        assertNotNull(item.referenceMonster);
        assertEquals("PF2e | Bestiary", item.referenceMonster.getSourceTag());
    }
}
