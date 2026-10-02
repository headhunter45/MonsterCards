package com.majinnaibu.monstercards.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.majinnaibu.monstercards.data.enums.ArmorType;
import com.majinnaibu.monstercards.data.enums.ChallengeRating;
import com.majinnaibu.monstercards.data.enums.GameSystem;

import org.junit.Test;

import java.util.UUID;

public class ReferenceMonsterTest {

    @Test
    public void testFromMonsterAndToMonster() {
        Monster monster = new Monster();
        monster.id = UUID.randomUUID();
        monster.name = "Adult Red Dragon";
        monster.size = "Huge";
        monster.type = "Dragon";
        monster.alignment = "Chaotic Evil";
        monster.gameSystem = GameSystem.DND_5E;
        monster.sourceLabel = "SRD 5.1";
        monster.strengthScore = 27;
        monster.dexterityScore = 10;
        monster.constitutionScore = 25;
        monster.intelligenceScore = 16;
        monster.wisdomScore = 13;
        monster.charismaScore = 21;
        monster.armorType = ArmorType.NATURAL_ARMOR;
        monster.naturalArmorBonus = 9;
        monster.challengeRating = ChallengeRating.SEVENTEEN;
        monster.walkSpeed = 40;
        monster.climbSpeed = 40;
        monster.flySpeed = 80;
        monster.damageImmunities.add("Fire");
        monster.abilities.add(new Trait("Legendary Resistance", "If the dragon fails a saving throw, it can choose to succeed instead."));
        monster.actions.add(new Trait("Multiattack", "The dragon can use its Frightful Presence. It then makes three attacks."));

        ReferenceMonster ref = ReferenceMonster.fromMonster(monster, "srd_5e", "Monster Manual");

        assertEquals(monster.id.toString(), ref.id);
        assertEquals("srd_5e", ref.sourceId);
        assertEquals("SRD 5.1", ref.sourceLabel);
        assertEquals("Monster Manual", ref.bookSource);
        assertEquals(GameSystem.DND_5E, ref.gameSystem);
        assertEquals("Adult Red Dragon", ref.name);
        assertEquals(27, ref.strengthScore);
        assertEquals(1, ref.abilities.size());
        assertEquals(1, ref.actions.size());
        assertTrue(ref.damageImmunities.contains("Fire"));
        assertEquals("5e | SRD 5.1", ref.getSourceTag());

        // Clone/convert back to regular Monster
        Monster cloned = ref.toMonster();
        assertNotNull(cloned.id);
        assertNotEquals(monster.id, cloned.id); // Cloned monster should receive a fresh UUID
        assertEquals("Adult Red Dragon", cloned.name);
        assertEquals(GameSystem.DND_5E, cloned.gameSystem);
        assertEquals("SRD 5.1", cloned.sourceLabel);
        assertEquals(27, cloned.strengthScore);
        assertEquals(1, cloned.abilities.size());
        assertEquals("Legendary Resistance", cloned.abilities.get(0).name);
        assertTrue(cloned.damageImmunities.contains("Fire"));
    }

    @Test
    public void testPf2eReferenceMonsterSourceTag() {
        ReferenceMonster ref = new ReferenceMonster();
        ref.name = "Goblin Warrior";
        ref.gameSystem = GameSystem.PF_2E;
        ref.sourceId = "pf2e_bestiary";
        ref.sourceLabel = "Bestiary 1";
        ref.bookSource = "Bestiary 1";

        assertEquals("PF2e | Bestiary 1", ref.getSourceTag());
    }

    @Test
    public void testCustomSystemReferenceMonsterSourceTag() {
        ReferenceMonster ref = new ReferenceMonster();
        ref.name = "Cyberpunk Drone";
        ref.gameSystem = GameSystem.CUSTOM;
        ref.sourceLabel = "Homebrew 2099";

        assertEquals("Custom | Homebrew 2099", ref.getSourceTag());
    }
}
