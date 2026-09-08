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

    private static final String ANCIENT_BLACK_DRAGON_JSON = "{\"name\":\"Ancient Black Dragon\",\"size\":\"gargantuan\",\"type\":\"Dragon\",\"tag\":\"\",\"alignment\":\"chaotic evil\",\"hitDice\":\"21\",\"armorName\":\"natural armor\",\"shieldBonus\":0,\"natArmorBonus\":10,\"otherArmorDesc\":\"22 (natural armor)\",\"speed\":\"40\",\"burrowSpeed\":\"0\",\"climbSpeed\":\"0\",\"flySpeed\":\"80\",\"hover\":false,\"swimSpeed\":\"40\",\"customHP\":false,\"customSpeed\":false,\"hpText\":\"367 (21d20 + 147)\",\"speedDesc\":\"40 ft., fly 80 ft., swim 40 ft.\",\"strPoints\":\"27\",\"dexPoints\":\"14\",\"conPoints\":\"25\",\"intPoints\":\"16\",\"wisPoints\":\"15\",\"chaPoints\":\"19\",\"blindsight\":\"60\",\"blind\":false,\"darkvision\":\"120\",\"tremorsense\":\"0\",\"truesight\":\"0\",\"telepathy\":0,\"cr\":\"21\",\"customCr\":\"21 (33,000 XP)\",\"customProf\":7,\"isLegendary\":true,\"legendariesDescription\":\"The dragon can take 3 legendary actions, choosing from the options below. Only one legendary action option can be used at a time and only at the end of another creature's turn. The dragon regains spent legendary actions at the start of its turn.\",\"isLair\":true,\"lairDescription\":\"When fighting inside its lair, the ancient black dragon can invoke the ambient magic to take lair actions. On initiative count 20 (losing initiative ties), the ancient black dragon can take one lair action to cause one of the following effects:\",\"lairDescriptionEnd\":\"The goblin can't repeat an effect until they have all been used, and it can't use the same effect two rounds in a row.\",\"isMythic\":true,\"mythicDescription\":\"If the ancient black dragon's mythic trait is active, it can use the options below as legendary actions for 1 hour after using {Some Ability}.\",\"isRegional\":true,\"regionalDescription\":\"The region containing the ancient black dragon's lair is warped by the creature's presence, which creates one or more of the following effects:\",\"regionalDescriptionEnd\":\"If the ancient black dragon dies, the first two effects fade over the course of 3d10 days.\",\"properties\":[],\"abilities\":[{\"name\":\"Amphibious\",\"desc\":\"The dragon can breathe air and water.\"},{\"name\":\"Legendary Resistance (3/Day)\",\"desc\":\"If the dragon fails a saving throw, it can choose to succeed instead.\"}],\"actions\":[{\"name\":\"Multiattack\",\"desc\":\"The dragon can use its Frightful Presence. It then makes three attacks: one with its bite and two with its claws.\"},{\"name\":\"Bite\",\"desc\":\"_Melee Weapon Attack:_ +15 to hit, reach 15 ft., one target. _Hit:_ 19 (2d10 + 8) piercing damage plus 9 (2d8) acid damage.\"},{\"name\":\"Claw\",\"desc\":\"_Melee Weapon Attack:_ +15 to hit, reach 10 ft., one target. _Hit:_ 15 (2d6 + 8) slashing damage.\"},{\"name\":\"Tail\",\"desc\":\"_Melee Weapon Attack:_ +15 to hit, reach 20 ft., one target. _Hit:_ 17 (2d8 + 8) bludgeoning damage.\"},{\"name\":\"Frightful Presence\",\"desc\":\"Each creature of the dragon's choice that is within 120 feet of the dragon and aware of it must succeed on a DC 19 Wisdom saving throw or become frightened for 1 minute. A creature can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success. If a creature's saving throw is successful or the effect ends for it, the creature is immune to the dragon's Frightful Presence for the next 24 hours.\"},{\"name\":\"Acid Breath (Recharge 5-6)\",\"desc\":\"The dragon exhales acid in a 90-foot line that is 10 feet wide. Each creature in that line must make a DC 22 Dexterity saving throw, taking 67 (15d8) acid damage on a failed save, or half as much damage on a successful one.\"}],\"bonusActions\":[],\"reactions\":[],\"legendaries\":[{\"name\":\"Detect\",\"desc\":\"The dragon makes a Wisdom (Perception) check.\"},{\"name\":\"Tail Attack\",\"desc\":\"The dragon makes a tail attack.\"},{\"name\":\"Wing Attack (Costs 2 Actions)\",\"desc\":\"The dragon beats its wings. Each creature within 15 ft. of the dragon must succeed on a DC 23 Dexterity saving throw or take 15 (2d6 + 8) bludgeoning damage and be knocked prone. The dragon can then fly up to half its flying speed.\"}],\"mythics\":[],\"lairs\":[],\"regionals\":[],\"sthrows\":[{\"name\":\"dex\",\"order\":1},{\"name\":\"con\",\"order\":2},{\"name\":\"wis\",\"order\":4},{\"name\":\"cha\",\"order\":5}],\"skills\":[{\"name\":\"perception\",\"stat\":\"wis\",\"note\":\" (ex)\"},{\"name\":\"stealth\",\"stat\":\"dex\"}],\"damagetypes\":[{\"name\":\"acid\",\"note\":\" (Immune)\",\"type\":\"i\"}],\"specialdamage\":[],\"conditions\":[],\"languages\":[{\"name\":\"Common\",\"speaks\":true},{\"name\":\"Draconic\",\"speaks\":true}],\"understandsBut\":\"\",\"shortName\":\"\",\"pluralName\":\"\",\"doubleColumns\":false,\"separationPoint\":5,\"damage\":[]}";

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
    public void parse_bonusActions_populatesBonusActions() throws Exception {
        String json = "{"
                + "\"name\": \"Goblin Boss\","
                + "\"hitDice\": 4,"
                + "\"bonusActions\": [{\"name\": \"Nimble Escape\", \"desc\": \"Disengage or Hide as bonus action.\"}]"
                + "}";
        Monster monster = importer.parse(json);
        assertNotNull(monster);
        assertEquals(1, monster.bonusActions.size());
        assertEquals("Nimble Escape", monster.bonusActions.get(0).name);
        assertEquals("Disengage or Hide as bonus action.", monster.bonusActions.get(0).description);
    }

    @Test
    public void parse_blindsightWithBlindFlag_appendsNote() throws Exception {
        String json = "{"
                + "\"name\": \"Grimlock\","
                + "\"hitDice\": 3,"
                + "\"blindsight\": 30,"
                + "\"blind\": true"
                + "}";
        Monster monster = importer.parse(json);
        assertNotNull(monster);
        assertTrue(monster.senses.contains("blindsight 30 ft. (blind beyond this radius)"));
    }

    @Test
    public void parse_legendaryAndMythicDescriptions_parsedCorrectly() throws Exception {
        String json = "{"
                + "\"name\": \"Ancient Dragon\","
                + "\"hitDice\": 20,"
                + "\"legendariesDescription\": \"The dragon can take 3 legendary actions.\","
                + "\"mythicDescription\": \"If mythic trait is active...\","
                + "\"mythics\": [{\"name\": \"Grasp\", \"desc\": \"Grabs a creature.\"}]"
                + "}";
        Monster monster = importer.parse(json);
        assertNotNull(monster);
        assertEquals(3, monster.legendaryActions.size());
        assertEquals("Legendary Actions", monster.legendaryActions.get(0).name);
        assertEquals("Mythic Actions", monster.legendaryActions.get(1).name);
        assertEquals("Mythic Action: Grasp", monster.legendaryActions.get(2).name);
    }

    @Test
    public void parse_lairAndRegionalDescriptions_parsedCorrectly() throws Exception {
        String json = "{"
                + "\"name\": \"Lich\","
                + "\"hitDice\": 18,"
                + "\"lairDescription\": \"Lair intro.\","
                + "\"lairDescriptionEnd\": \"Lair end.\","
                + "\"regionalDescription\": \"Regional intro.\","
                + "\"regionalDescriptionEnd\": \"Regional end.\""
                + "}";
        Monster monster = importer.parse(json);
        assertNotNull(monster);
        assertEquals(1, monster.lairActions.size());
        assertEquals("Lair Actions", monster.lairActions.get(0).name);
        assertEquals("Lair intro.\n\nLair end.", monster.lairActions.get(0).description);
        assertEquals(1, monster.regionalActions.size());
        assertEquals("Regional Effects", monster.regionalActions.get(0).name);
        assertEquals("Regional intro.\n\nRegional end.", monster.regionalActions.get(0).description);
    }

    @Test
    public void parse_ancientBlackDragonExport_populatesAllSections() throws Exception {
        Monster monster = importer.parse(ANCIENT_BLACK_DRAGON_JSON);
        assertNotNull(monster);
        assertEquals("Ancient Black Dragon", monster.name);
        assertEquals("gargantuan", monster.size);
        assertEquals("Dragon", monster.type);
        assertEquals("chaotic evil", monster.alignment);
        assertEquals(27, monster.strengthScore);

        // Abilities
        assertEquals(2, monster.abilities.size());
        assertEquals("Amphibious", monster.abilities.get(0).name);

        // Actions
        assertEquals(6, monster.actions.size());
        assertEquals("Multiattack", monster.actions.get(0).name);

        // Legendary + Mythic Actions
        // 1 legendary intro + 3 legendary actions + 1 mythic intro = 5 items
        assertEquals(5, monster.legendaryActions.size());
        assertEquals("Legendary Actions", monster.legendaryActions.get(0).name);
        assertEquals("Detect", monster.legendaryActions.get(1).name);
        assertEquals("Mythic Actions", monster.legendaryActions.get(4).name);

        // Lair Actions
        assertEquals(1, monster.lairActions.size());
        assertEquals("Lair Actions", monster.lairActions.get(0).name);
        assertTrue(monster.lairActions.get(0).description.contains("invoke the ambient magic"));

        // Regional Effects
        assertEquals(1, monster.regionalActions.size());
        assertEquals("Regional Effects", monster.regionalActions.get(0).name);
        assertTrue(monster.regionalActions.get(0).description.contains("warped by the creature's presence"));
    }

    @Test
    public void monsterImportHelper_fromJSON_delegatesToImporter() {
        Monster monster = MonsterImportHelper.fromJSON(SAMPLE_TETRACUBE_JSON);
        assertNotNull(monster);
        assertEquals("Goblin", monster.name);
    }
}
