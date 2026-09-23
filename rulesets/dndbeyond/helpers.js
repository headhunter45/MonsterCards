/**
 * rulesets/dndbeyond/helpers.js
 * Comprehensive Handlebars helpers for rendering D&D Beyond character entities.
 * Automatically loaded by the preview server and test suites.
 */

const STAT_NAMES = {
  1: 'strength',
  2: 'dexterity',
  3: 'constitution',
  4: 'intelligence',
  5: 'wisdom',
  6: 'charisma'
};

const STAT_SHORT = {
  1: 'STR',
  2: 'DEX',
  3: 'CON',
  4: 'INT',
  5: 'WIS',
  6: 'CHA'
};

const ALIGNMENTS = {
  1: 'Lawful Good',
  2: 'Neutral Good',
  3: 'Chaotic Good',
  4: 'Lawful Neutral',
  5: 'True Neutral',
  6: 'Chaotic Neutral',
  7: 'Lawful Evil',
  8: 'Neutral Evil',
  9: 'Chaotic Evil'
};

const STANDARD_SKILLS = [
  { name: 'Acrobatics', slug: 'acrobatics', statId: 2 },
  { name: 'Animal Handling', slug: 'animal-handling', statId: 5 },
  { name: 'Arcana', slug: 'arcana', statId: 4 },
  { name: 'Athletics', slug: 'athletics', statId: 1 },
  { name: 'Deception', slug: 'deception', statId: 6 },
  { name: 'History', slug: 'history', statId: 4 },
  { name: 'Insight', slug: 'insight', statId: 5 },
  { name: 'Intimidation', slug: 'intimidation', statId: 6 },
  { name: 'Investigation', slug: 'investigation', statId: 4 },
  { name: 'Medicine', slug: 'medicine', statId: 5 },
  { name: 'Nature', slug: 'nature', statId: 4 },
  { name: 'Perception', slug: 'perception', statId: 5 },
  { name: 'Performance', slug: 'performance', statId: 6 },
  { name: 'Persuasion', slug: 'persuasion', statId: 6 },
  { name: 'Religion', slug: 'religion', statId: 4 },
  { name: 'Sleight of Hand', slug: 'sleight-of-hand', statId: 2 },
  { name: 'Stealth', slug: 'stealth', statId: 2 },
  { name: 'Survival', slug: 'survival', statId: 5 }
];

function getAllModifiers(p) {
  if (!p || !p.modifiers) return [];
  const mods = [];
  for (const source of ['race', 'class', 'background', 'item', 'feat', 'condition']) {
    const list = p.modifiers[source];
    if (Array.isArray(list)) {
      mods.push(...list);
    }
  }
  return mods;
}

function calcTotalLevel(p) {
  if (!p || !Array.isArray(p.classes)) return 1;
  return p.classes.reduce((sum, c) => sum + (c.level || 0), 0) || 1;
}

function calcProfBonus(p) {
  const lvl = calcTotalLevel(p);
  return Math.floor((lvl - 1) / 4) + 2;
}

function calcStatScore(p, statId) {
  if (!p) return 10;
  statId = Number(statId);

  // Check hard override
  if (Array.isArray(p.overrideStats)) {
    const ov = p.overrideStats.find(s => s.id === statId);
    if (ov && ov.value !== null && ov.value !== undefined) {
      return Number(ov.value);
    }
  }

  // Base stat
  let base = 10;
  if (Array.isArray(p.stats)) {
    const st = p.stats.find(s => s.id === statId);
    if (st && st.value !== null && st.value !== undefined) {
      base = Number(st.value);
    }
  }

  // Bonus stats
  let bonus = 0;
  if (Array.isArray(p.bonusStats)) {
    const bs = p.bonusStats.find(s => s.id === statId);
    if (bs && bs.value !== null && bs.value !== undefined) {
      bonus += Number(bs.value);
    }
  }

  // Modifier bonuses
  const statSlug = `${STAT_NAMES[statId]}-score`;
  const allMods = getAllModifiers(p);
  for (const m of allMods) {
    if (m.type === 'bonus' && m.subType === statSlug) {
      const val = Number(m.value || m.fixedValue || 0);
      bonus += val;
    }
  }

  return base + bonus;
}

function calcStatMod(p, statId) {
  const score = calcStatScore(p, statId);
  return Math.floor((score - 10) / 2);
}

function formatMod(val) {
  const num = Number(val) || 0;
  return num >= 0 ? `+${num}` : `${num}`;
}

function stripHtml(html) {
  if (!html) return '';
  return String(html)
    .replace(/<br\s*\/?>/gi, '\n')
    .replace(/<\/p>/gi, '\n\n')
    .replace(/<[^>]+>/g, '')
    .replace(/&nbsp;/g, ' ')
    .replace(/&rsquo;/g, "’")
    .replace(/&lsquo;/g, "‘")
    .replace(/&rdquo;/g, '”')
    .replace(/&ldquo;/g, '“')
    .replace(/&mdash;/g, '—')
    .replace(/&ndash;/g, '–')
    .replace(/&amp;/g, '&')
    .trim();
}

module.exports = function (handlebars) {
  // Stat calculations
  handlebars.registerHelper('ddbStatScore', (p, id) => calcStatScore(p, id));
  handlebars.registerHelper('ddbStatMod', (p, id) => calcStatMod(p, id));
  handlebars.registerHelper('ddbStatModText', (p, id) => formatMod(calcStatMod(p, id)));

  // Total level and proficiency bonus
  handlebars.registerHelper('ddbTotalLevel', (p) => calcTotalLevel(p));
  handlebars.registerHelper('ddbProfBonus', (p) => calcProfBonus(p));
  handlebars.registerHelper('ddbProfBonusText', (p) => `+${calcProfBonus(p)}`);

  // Class & Level summary
  handlebars.registerHelper('ddbClassDisplay', (p) => {
    if (!p || !Array.isArray(p.classes) || p.classes.length === 0) return 'Adventurer 1';
    return p.classes.map(c => {
      const cname = c.definition?.name || 'Class';
      const sub = c.subclassDefinition?.name;
      return sub ? `${cname} (${sub}) ${c.level}` : `${cname} ${c.level}`;
    }).join(' / ');
  });

  // Alignment
  handlebars.registerHelper('ddbAlignment', (p) => {
    if (!p || !p.alignmentId) return 'Neutral';
    return ALIGNMENTS[p.alignmentId] || 'Neutral';
  });

  // Saving Throws
  handlebars.registerHelper('ddbHasSaveProf', (p, statId) => {
    const statName = STAT_NAMES[Number(statId)];
    const saveSlug = `${statName}-saving-throws`;
    const allMods = getAllModifiers(p);
    return allMods.some(m => m.type === 'proficiency' && m.subType === saveSlug);
  });

  handlebars.registerHelper('ddbSavingThrow', (p, statId) => {
    const mod = calcStatMod(p, statId);
    const statName = STAT_NAMES[Number(statId)];
    const saveSlug = `${statName}-saving-throws`;
    const allMods = getAllModifiers(p);
    const isProf = allMods.some(m => m.type === 'proficiency' && m.subType === saveSlug);
    const profBonus = calcProfBonus(p);
    return formatMod(mod + (isProf ? profBonus : 0));
  });

  // Skills
  handlebars.registerHelper('ddbSkillList', function (p, options) {
    if (!p) return '';
    const profBonus = calcProfBonus(p);
    const allMods = getAllModifiers(p);
    const hasJack = allMods.some(m => m.type === 'half-proficiency' && m.subType === 'ability-checks');

    const result = STANDARD_SKILLS.map(sk => {
      const statMod = calcStatMod(p, sk.statId);
      const isExpert = allMods.some(m => m.type === 'expertise' && m.subType === sk.slug);
      const isProf = isExpert || allMods.some(m => m.type === 'proficiency' && m.subType === sk.slug);

      let total = statMod;
      let profLevel = 0; // 0=none, 1=proficient, 2=expertise
      if (isExpert) {
        total += profBonus * 2;
        profLevel = 2;
      } else if (isProf) {
        total += profBonus;
        profLevel = 1;
      } else if (hasJack) {
        total += Math.floor(profBonus / 2);
      }

      return {
        name: sk.name,
        slug: sk.slug,
        statShort: STAT_SHORT[sk.statId],
        statId: sk.statId,
        bonus: formatMod(total),
        bonusNum: total,
        isProf: isProf,
        isExpert: isExpert,
        profLevel: profLevel
      };
    });

    if (options && options.fn) {
      return result.map(item => options.fn(item)).join('');
    }
    return result;
  });

  // Skills grouped by ability for alternative sheet
  handlebars.registerHelper('ddbSkillsForStat', function (p, statId, options) {
    if (!p) return '';
    const profBonus = calcProfBonus(p);
    const allMods = getAllModifiers(p);
    const hasJack = allMods.some(m => m.type === 'half-proficiency' && m.subType === 'ability-checks');
    const targetStat = Number(statId);

    const skills = STANDARD_SKILLS.filter(sk => sk.statId === targetStat).map(sk => {
      const statMod = calcStatMod(p, targetStat);
      const isExpert = allMods.some(m => m.type === 'expertise' && m.subType === sk.slug);
      const isProf = isExpert || allMods.some(m => m.type === 'proficiency' && m.subType === sk.slug);

      let total = statMod;
      let profLevel = 0;
      if (isExpert) {
        total += profBonus * 2;
        profLevel = 2;
      } else if (isProf) {
        total += profBonus;
        profLevel = 1;
      } else if (hasJack) {
        total += Math.floor(profBonus / 2);
      }

      return {
        name: sk.name,
        slug: sk.slug,
        bonus: formatMod(total),
        isProf,
        isExpert,
        profLevel
      };
    });

    if (options && options.fn) {
      return skills.map(item => options.fn(item)).join('');
    }
    return skills;
  });

  // Passive Perception
  handlebars.registerHelper('ddbPassivePerception', (p) => {
    const profBonus = calcProfBonus(p);
    const wisMod = calcStatMod(p, 5);
    const allMods = getAllModifiers(p);
    const isExpert = allMods.some(m => m.type === 'expertise' && m.subType === 'perception');
    const isProf = isExpert || allMods.some(m => m.type === 'proficiency' && m.subType === 'perception');
    let perceptionBonus = wisMod + (isExpert ? profBonus * 2 : (isProf ? profBonus : 0));
    return 10 + perceptionBonus;
  });

  // Combat Stats: Armor Class
  handlebars.registerHelper('ddbArmorClass', (p) => {
    if (!p) return 10;
    const dexMod = calcStatMod(p, 2);
    const conMod = calcStatMod(p, 3);
    const wisMod = calcStatMod(p, 5);
    const inv = Array.isArray(p.inventory) ? p.inventory : [];

    let baseAc = 10;
    let dexBonus = dexMod;
    let shieldBonus = 0;
    let hasBodyArmor = false;

    for (const item of inv) {
      if (!item.equipped) continue;
      const defn = item.definition;
      if (!defn) continue;

      if (defn.filterType === 'Armor' && defn.type === 'Shield') {
        shieldBonus += (defn.armorClass || 2);
      } else if (defn.filterType === 'Armor') {
        hasBodyArmor = true;
        baseAc = defn.armorClass || 10;
        const typeId = defn.armorTypeId;
        if (typeId === 1) { // Light
          dexBonus = dexMod;
        } else if (typeId === 2) { // Medium
          dexBonus = Math.min(2, dexMod);
        } else if (typeId === 3) { // Heavy
          dexBonus = 0;
        }
      }
    }

    if (!hasBodyArmor) {
      const classes = Array.isArray(p.classes) ? p.classes.map(c => c.definition?.name) : [];
      if (classes.includes('Barbarian')) {
        baseAc = 10 + dexMod + conMod;
        dexBonus = 0;
      } else if (classes.includes('Monk')) {
        baseAc = 10 + dexMod + wisMod;
        dexBonus = 0;
      } else {
        baseAc = 10;
        dexBonus = dexMod;
      }
    }

    // Item/modifier bonus to AC
    let modBonus = 0;
    const allMods = getAllModifiers(p);
    for (const m of allMods) {
      if (m.type === 'bonus' && m.subType === 'armor-class') {
        modBonus += Number(m.value || m.fixedValue || 0);
      }
    }

    return baseAc + dexBonus + shieldBonus + modBonus;
  });

  // Initiative
  handlebars.registerHelper('ddbInitiative', (p) => {
    const dexMod = calcStatMod(p, 2);
    let bonus = 0;
    const allMods = getAllModifiers(p);
    for (const m of allMods) {
      if (m.type === 'bonus' && m.subType === 'initiative') {
        bonus += Number(m.value || m.fixedValue || 0);
      }
    }
    return formatMod(dexMod + bonus);
  });

  // Speed
  handlebars.registerHelper('ddbSpeed', (p) => {
    let baseSpeed = p?.race?.weightSpeeds?.normal?.walk;
    if (baseSpeed === undefined || baseSpeed === null) baseSpeed = 30;
    let bonus = 0;
    const allMods = getAllModifiers(p);
    for (const m of allMods) {
      if (m.type === 'bonus' && m.subType === 'speed') {
        bonus += Number(m.value || m.fixedValue || 0);
      }
    }
    return baseSpeed + bonus;
  });

  // Hit Points
  handlebars.registerHelper('ddbMaxHp', (p) => {
    if (!p) return 10;
    return p.overrideHitPoints != null ? p.overrideHitPoints : ((p.baseHitPoints || 10) + (p.bonusHitPoints || 0));
  });

  handlebars.registerHelper('ddbCurrentHp', (p) => {
    if (!p) return 10;
    const max = p.overrideHitPoints != null ? p.overrideHitPoints : ((p.baseHitPoints || 10) + (p.bonusHitPoints || 0));
    return Math.max(0, max - (p.removedHitPoints || 0));
  });

  handlebars.registerHelper('ddbTempHp', (p) => (p && p.temporaryHitPoints) || 0);

  // Hit Dice
  handlebars.registerHelper('ddbHitDice', (p) => {
    if (!p || !Array.isArray(p.classes)) return '1d8';
    return p.classes.map(c => `${c.level}d${c.definition?.hitDice || 8}`).join(' + ');
  });

  // Attacks Table
  handlebars.registerHelper('ddbAttacks', function (p, options) {
    if (!p || !Array.isArray(p.inventory)) return '';
    const profBonus = calcProfBonus(p);
    const strMod = calcStatMod(p, 1);
    const dexMod = calcStatMod(p, 2);

    const attacks = [];
    for (const item of p.inventory) {
      if (!item.equipped) continue;
      const defn = item.definition;
      if (!defn || defn.filterType !== 'Weapon') continue;

      const isRanged = (defn.range && defn.range > 5) || (defn.type && defn.type.toLowerCase().includes('ranged'));
      const isFinesse = defn.properties?.some(prop => prop.name?.toLowerCase() === 'finesse');
      const atkAbilityMod = isRanged ? dexMod : (isFinesse ? Math.max(strMod, dexMod) : strMod);

      const atkBonus = formatMod(atkAbilityMod + profBonus);
      const dmgCount = defn.damage?.diceCount || 1;
      const dmgVal = defn.damage?.diceValue || 6;
      const dmgType = defn.damageType || 'bludgeoning';
      const dmgBonus = formatMod(atkAbilityMod);
      const damageStr = `${dmgCount}d${dmgVal} ${dmgBonus} ${dmgType}`;

      attacks.push({
        name: defn.name,
        atkBonus,
        damage: damageStr
      });
    }

    if (attacks.length === 0) {
      attacks.push({ name: 'Unarmed Strike', atkBonus: formatMod(strMod + profBonus), damage: `1 ${formatMod(strMod)} bludgeoning` });
    }

    if (options && options.fn) {
      return attacks.map(atk => options.fn(atk)).join('');
    }
    return attacks;
  });

  // Spellcasting Attributes
  handlebars.registerHelper('ddbSpellcastingClass', (p) => {
    if (!p || !Array.isArray(p.classes)) return '—';
    const caster = p.classes.find(c => c.definition?.canCastSpells) || p.classes[0];
    return caster?.definition?.name || '—';
  });

  handlebars.registerHelper('ddbSpellcastingAbility', (p) => {
    if (!p || !Array.isArray(p.classes)) return 'INT';
    const caster = p.classes.find(c => c.definition?.canCastSpells);
    const abId = caster?.definition?.spellCastingAbilityId || 4;
    return STAT_SHORT[abId] || 'INT';
  });

  handlebars.registerHelper('ddbSpellSaveDc', (p) => {
    const profBonus = calcProfBonus(p);
    const caster = Array.isArray(p?.classes) ? p.classes.find(c => c.definition?.canCastSpells) : null;
    const abId = caster?.definition?.spellCastingAbilityId || 4;
    const mod = calcStatMod(p, abId);
    return 8 + profBonus + mod;
  });

  handlebars.registerHelper('ddbSpellAttackBonus', (p) => {
    const profBonus = calcProfBonus(p);
    const caster = Array.isArray(p?.classes) ? p.classes.find(c => c.definition?.canCastSpells) : null;
    const abId = caster?.definition?.spellCastingAbilityId || 4;
    const mod = calcStatMod(p, abId);
    return formatMod(profBonus + mod);
  });

  // Spells grouped by level (0..9)
  handlebars.registerHelper('ddbSpellsByLevel', function (p, level, options) {
    if (!p) return '';
    level = Number(level);
    const spells = [];

    // Class spells
    if (Array.isArray(p.classSpells)) {
      for (const group of p.classSpells) {
        if (Array.isArray(group.spells)) {
          for (const s of group.spells) {
            const defn = s.definition;
            if (defn && defn.level === level) {
              spells.push({
                name: defn.name,
                prepared: !!(s.prepared || s.alwaysPrepared),
                school: defn.school,
                castingTime: defn.castingTimeDescription || '1 action',
                range: defn.range?.rangeValue ? `${defn.range.rangeValue} ft` : 'Self',
                duration: defn.duration?.durationType || 'Instant',
                ritual: !!defn.ritual,
                concentration: !!defn.concentration
              });
            }
          }
        }
      }
    }

    // Source spells (race, feat, item)
    if (p.spells && typeof p.spells === 'object') {
      for (const src of ['race', 'class', 'feat', 'item']) {
        if (Array.isArray(p.spells[src])) {
          for (const s of p.spells[src]) {
            const defn = s.definition;
            if (defn && defn.level === level) {
              spells.push({
                name: defn.name,
                prepared: true,
                school: defn.school,
                castingTime: defn.castingTimeDescription || '1 action',
                range: defn.range?.rangeValue ? `${defn.range.rangeValue} ft` : 'Self',
                duration: defn.duration?.durationType || 'Instant',
                ritual: !!defn.ritual,
                concentration: !!defn.concentration
              });
            }
          }
        }
      }
    }

    // Deduplicate by name
    const unique = [];
    const names = new Set();
    for (const sp of spells) {
      if (!names.has(sp.name)) {
        names.add(sp.name);
        unique.push(sp);
      }
    }

    if (options && options.fn) {
      return unique.map(sp => options.fn(sp)).join('');
    }
    return unique;
  });

  // Spell Slot Information for a Level
  handlebars.registerHelper('ddbSpellSlotInfo', (p, level) => {
    level = Number(level);
    if (!p || !Array.isArray(p.spellSlots)) return { level, total: 0, used: 0, available: 0 };
    const slot = p.spellSlots.find(s => s.level === level);
    if (!slot) return { level, total: 0, used: 0, available: 0 };
    return {
      level,
      total: (slot.available || 0) + (slot.used || 0),
      used: slot.used || 0,
      available: slot.available || 0
    };
  });

  // Strip HTML
  handlebars.registerHelper('ddbStripHtml', (str) => stripHtml(str));

  // Race name
  handlebars.registerHelper('ddbRaceName', (p) => {
    return p?.race?.fullName || p?.race?.baseRaceName || '—';
  });

  // Background name
  handlebars.registerHelper('ddbBackgroundName', (p) => {
    return p?.background?.definition?.name || (p?.background?.hasCustomBackground ? 'Custom' : '—');
  });

  // Death saves
  handlebars.registerHelper('ddbDeathSaves', (p) => {
    return {
      successes: p?.deathSaves?.successCount || 0,
      failures: p?.deathSaves?.failCount || 0
    };
  });

  // Other Proficiencies & Languages
  handlebars.registerHelper('ddbOtherProficiencies', (p) => {
    const allMods = getAllModifiers(p);
    const armor = new Set();
    const weapons = new Set();
    const tools = new Set();
    const languages = new Set();

    const skillSlugs = new Set(STANDARD_SKILLS.map(s => s.slug));

    for (const m of allMods) {
      if (m.type === 'language') {
        const lang = m.friendlySubtypeName || m.subType;
        if (lang) languages.add(lang);
      } else if (m.type === 'proficiency') {
        const name = m.friendlySubtypeName || m.subType;
        const sub = (m.subType || '').toLowerCase();
        if (skillSlugs.has(sub) || sub.endsWith('-saving-throws') || sub.endsWith('-ability-checks')) {
          continue;
        }
        if (sub.includes('armor') || sub.includes('shields')) {
          armor.add(name);
        } else if (sub.includes('weapon') || sub.includes('sword') || sub.includes('axe') || sub.includes('bow') || sub.includes('crossbow')) {
          weapons.add(name);
        } else {
          tools.add(name);
        }
      }
    }

    return {
      armor: Array.from(armor).join(', '),
      weapons: Array.from(weapons).join(', '),
      tools: Array.from(tools).join(', '),
      languages: Array.from(languages).join(', ')
    };
  });

  // Inventory items
  handlebars.registerHelper('ddbInventoryItems', (p, options) => {
    if (!p || !Array.isArray(p.inventory)) return [];
    const items = p.inventory.map(item => {
      const defn = item.definition || {};
      return {
        name: defn.name || 'Unknown Item',
        quantity: item.quantity || 1,
        weight: defn.weight || 0,
        equipped: !!item.equipped,
        isAttuned: !!item.isAttuned,
        type: defn.filterType || defn.type || 'Item'
      };
    });
    if (options && options.fn) {
      return items.map(item => options.fn(item)).join('');
    }
    return items;
  });

  // Currencies
  handlebars.registerHelper('ddbCurrencies', (p) => {
    return p?.currencies || { cp: 0, sp: 0, ep: 0, gp: 0, pp: 0 };
  });

  // Features & Traits
  handlebars.registerHelper('ddbFeatures', (p, options) => {
    if (!p) return [];
    const features = [];
    const seenNames = new Set();

    // 1. Racial traits
    if (Array.isArray(p.race?.racialTraits)) {
      for (const t of p.race.racialTraits) {
        const def = t.definition;
        if (def && !def.hideInSheet && !seenNames.has(def.name)) {
          seenNames.add(def.name);
          features.push({
            name: def.name,
            source: p.race.fullName || 'Racial Trait',
            snippet: def.snippet || '',
            description: stripHtml(def.description || def.snippet || '')
          });
        }
      }
    }

    // 2. Class features
    if (Array.isArray(p.classes)) {
      for (const c of p.classes) {
        const lvl = c.level || 1;
        const cname = c.definition?.name || 'Class';
        if (Array.isArray(c.definition?.classFeatures)) {
          for (const cf of c.definition.classFeatures) {
            if ((cf.requiredLevel || 1) <= lvl && !cf.hideInSheet && !seenNames.has(cf.name)) {
              seenNames.add(cf.name);
              features.push({
                name: cf.name,
                source: `${cname} Level ${cf.requiredLevel || 1}`,
                snippet: cf.snippet || '',
                description: stripHtml(cf.description || cf.snippet || '')
              });
            }
          }
        }
        if (Array.isArray(c.subclassDefinition?.classFeatures)) {
          const subName = c.subclassDefinition.name || 'Subclass';
          for (const cf of c.subclassDefinition.classFeatures) {
            if ((cf.requiredLevel || 1) <= lvl && !cf.hideInSheet && !seenNames.has(cf.name)) {
              seenNames.add(cf.name);
              features.push({
                name: cf.name,
                source: `${subName} Level ${cf.requiredLevel || 1}`,
                snippet: cf.snippet || '',
                description: stripHtml(cf.description || cf.snippet || '')
              });
            }
          }
        }
      }
    }

    // 3. Feats
    if (Array.isArray(p.feats)) {
      for (const f of p.feats) {
        const def = f.definition;
        if (def && !seenNames.has(def.name)) {
          seenNames.add(def.name);
          features.push({
            name: def.name,
            source: 'Feat',
            snippet: def.snippet || '',
            description: stripHtml(def.description || def.snippet || '')
          });
        }
      }
    }

    // 4. Background feature
    if (p.background?.definition?.featureName && !seenNames.has(p.background.definition.featureName)) {
      seenNames.add(p.background.definition.featureName);
      features.push({
        name: p.background.definition.featureName,
        source: p.background.definition.name || 'Background',
        snippet: '',
        description: stripHtml(p.background.definition.featureDescription || '')
      });
    }

    if (options && options.fn) {
      return features.map(f => options.fn(f)).join('');
    }
    return features;
  });

  // Avatar / Backdrop
  handlebars.registerHelper('ddbAvatarUrl', (p) => p?.decorations?.avatarUrl || p?.avatarUrl || '');
  handlebars.registerHelper('ddbBackdropUrl', (p) => p?.decorations?.backdropAvatarUrl || p?.decorations?.defaultBackdrop?.backdropAvatarUrl || '');

  // Comparison helpers (gte, eq)
  handlebars.registerHelper('ddbGte', (a, b) => Number(a) >= Number(b));
  handlebars.registerHelper('ddbEq', (a, b) => a === b);

  // Subentity Helpers
  handlebars.registerHelper('ddbSpellLevelSchool', (level, school) => {
    level = Number(level);
    if (level === 0) return `${school || 'Universal'} Cantrip`;
    const ord = ['th', 'st', 'nd', 'rd'][((level % 100 - 20) % 10) || (level % 100)] || 'th';
    return `${level}${ord}-level ${school || ''}`.trim();
  });

  handlebars.registerHelper('ddbSpellComponents', (components, matDesc) => {
    if (!Array.isArray(components) || components.length === 0) return 'None';
    const names = [];
    if (components.includes(1)) names.push('V');
    if (components.includes(2)) names.push('S');
    if (components.includes(3)) {
      names.push(matDesc ? `M (${matDesc})` : 'M');
    }
    return names.join(', ');
  });

  handlebars.registerHelper('ddbSpellDuration', (duration, concentration) => {
    if (!duration) return 'Instantaneous';
    let text = '';
    if (duration.durationInterval) {
      text = `${duration.durationInterval} ${duration.durationUnit || 'round'}${duration.durationInterval > 1 ? 's' : ''}`;
    } else if (duration.durationType) {
      text = duration.durationType;
    } else {
      text = 'Instantaneous';
    }
    if (concentration || duration.durationType === 'Concentration') {
      return `Concentration, up to ${text}`;
    }
    return text;
  });

  handlebars.registerHelper('ddbSpellRange', (range) => {
    if (!range) return 'Self';
    if (range.rangeValue) return `${range.rangeValue} ft.`;
    return range.origin || 'Self';
  });

  handlebars.registerHelper('ddbCostText', (cost) => {
    if (cost === undefined || cost === null) return '—';
    const num = Number(cost);
    if (num >= 1) return `${num} gp`;
    if (num >= 0.1) return `${Math.round(num * 10)} sp`;
    return `${Math.round(num * 100)} cp`;
  });

  handlebars.registerHelper('ddbDamageString', (damage, damageType) => {
    if (!damage) return '—';
    const dice = damage.diceString || (damage.diceCount && damage.diceValue ? `${damage.diceCount}d${damage.diceValue}` : '');
    const bonus = damage.fixedValue ? (damage.fixedValue > 0 ? `+${damage.fixedValue}` : `${damage.fixedValue}`) : '';
    const part = [dice, bonus].filter(Boolean).join('');
    return [part, damageType].filter(Boolean).join(' ');
  });

  // Automatically register all templates as partials if fs is available
  try {
    const fs = require('fs');
    const path = require('path');
    const tplDir = path.join(__dirname, 'templates');
    if (fs.existsSync(tplDir)) {
      const files = fs.readdirSync(tplDir);
      for (const f of files) {
        if (f.endsWith('.html')) {
          const partialName = path.basename(f, '.html');
          handlebars.registerPartial(partialName, fs.readFileSync(path.join(tplDir, f), 'utf8'));
        }
      }
    }
  } catch (e) {
    // Non-filesystem runtime fallback
  }
};

