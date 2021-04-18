## Monster
id: UUID as TEXT // doesn't exist in the iOS model
abilities: Set<Trait> converted to JSON as TEXT
actions: Set<Trait> converted to JSON as TEXT
alignment: String as TEXT
armor_type: Enum<String> as TEXT
blindsight_range: int as INTEGER
burrow_speed: int as INTEGER
can_hover: boolean as INTEGER
challenge_rating: Enum<String> as TEXT
charisma_saving_throw_advantage
charisma_saving_throw_proficiency
charisma_score: int as INTEGER
climb_speed: int as INTEGER
condition_immunities: Set<String> converted to JSON as TEXT
constitution_saving_throw_advantage
constitution_saving_throw_proficiency
constitution_score: int as INTEGER
//other_armor_description: String as TEXT
custom_challenge_rating_description: String as TEXT
custom_hit_points_description: String
custom_proficiency_bonus: int as INTEGER
custom_speed_description: String as TEXT
damage_immunities: Set<String> converted to JSON as TEXT
damage_resistances: Set<String> converted to JSON as TEXT
damage_vulnerabilities: Set<String> converted to JSON as TEXT
darkvision_range: int as INTEGER
dexterity_saving_throw_advantage
dexterity_saving_throw_proficiency
dexterity_score: int as INTEGER
fly_speed: int as INTEGER
has_custom_hit_points: boolean as INTEGER
has_custom_speed: boolean as INTEGER
// has_shield
hit_dice: int as INTEGER
intelligence_saving_throw_advantage
intelligence_saving_throw_proficiency
intelligence_score: int as INTEGER
is_blind_beyond_blindsight_range: boolean as INTEGER
lair_actions
languages: Set<Language> converted to JSON as TEXT
legendary_actions
name: String as TEXT
natural_armor_bonus: int as INTEGER
other_armor_description: String as TEXT
reactions
regional_actions
// senses
shield_bonus: int as INTEGER
size: String as TEXT
strength_saving_throw_advantage
strength_saving_throw_proficiency
strength_score: int as INTEGER
tag: String as TEXT // subtype || tag
swim_speed: int as INTEGER
telepathy_range: int as INTEGER
tremorsense_range: int as INTEGER
truesight_range: int as INTEGER
type: String as TEXT
understands_but_description: String as TEXT
walk_speed: int as INTEGER
wisdom_saving_throw_advantage
wisdom_saving_throw_proficiency
wisdom_score: int as INTEGER

// tracked as relationship (don't do this)
skills: Set<Skill> converted to JSON as TEXT

## Skill
// ability_score_name String defaults to "strength"
// advantage String defaults to "none"
// name String defaults to ""
// proficiency String defaults to "none"
