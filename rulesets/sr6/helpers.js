/**
 * SR6 Ruleset Handlebars Helpers
 * Auto-loaded by preview server for ruleset 'sr6'.
 */
module.exports = function (handlebars) {
  // Compute maximum physical damage boxes: 8 + ceil(body / 2)
  handlebars.registerHelper('calcPhysicalBoxes', function (body) {
    const b = typeof body === 'object' && body !== null ? (body.value || body.base || 0) : Number(body || 0);
    return 8 + Math.ceil(b / 2);
  });

  // Compute maximum stun damage boxes: 8 + ceil(willpower / 2)
  handlebars.registerHelper('calcStunBoxes', function (willpower) {
    const w = typeof willpower === 'object' && willpower !== null ? (willpower.value || willpower.base || 0) : Number(willpower || 0);
    return 8 + Math.ceil(w / 2);
  });

  // Format currency with nuyen symbol
  handlebars.registerHelper('formatNuyen', function (amount) {
    if (amount === undefined || amount === null) return '0¥';
    const num = typeof amount === 'number' ? amount : parseInt(String(amount).replace(/[^0-9]/g, ''), 10) || 0;
    return `${num.toLocaleString('en-US')}¥`;
  });

  // Format dice pool helper: (skill rating + linked attribute value)
  handlebars.registerHelper('calcDicePool', function (skill, attributes) {
    if (!skill) return 0;
    const rating = typeof skill.rating === 'number' ? skill.rating : (parseInt(skill.rating, 10) || 0);
    const attrName = (skill.attribute || '').toLowerCase();
    let attrVal = 0;
    if (attributes && attrName && attributes[attrName] !== undefined) {
      const a = attributes[attrName];
      attrVal = typeof a === 'object' && a !== null ? (a.value || a.base || 0) : (Number(a) || 0);
    }
    const bonus = skill.expertise ? 3 : (skill.specialization ? 2 : 0);
    return rating + attrVal + bonus;
  });

  // Safe attribute value extractor
  handlebars.registerHelper('attr', function (attributes, name) {
    if (!attributes || !name) return '—';
    const val = attributes[name.toLowerCase()];
    if (val === undefined || val === null) return '—';
    return typeof val === 'object' ? (val.value ?? val.base ?? '—') : val;
  });

  // Helper to build 3-box rows for condition monitor
  handlebars.registerHelper('conditionMonitorRows', function (totalBoxes, currentDamage, maxGridBoxes, options) {
    const total = Number(totalBoxes) || 8;
    const damage = Number(currentDamage) || 0;
    const maxBoxes = Number(maxGridBoxes) || 18;
    const rowsCount = Math.ceil(maxBoxes / 3);

    let output = '';
    for (let r = 0; r < rowsCount; r++) {
      const boxes = [];
      for (let c = 1; c <= 3; c++) {
        const boxIdx = r * 3 + c;
        if (boxIdx <= maxBoxes) {
          boxes.push({
            index: boxIdx,
            active: boxIdx <= total,
            filled: boxIdx <= damage,
            blackedOut: boxIdx > total,
            isThreshold: c === 3,
            penalty: -(r + 1)
          });
        }
      }
      output += options.fn({
        rowNumber: r + 1,
        penalty: -(r + 1),
        boxes: boxes
      });
    }
    return output;
  });

  // Matrix condition monitor boxes generator (1 to 12)
  handlebars.registerHelper('matrixMonitorBoxes', function (maxBoxes, currentDamage, options) {
    const total = Number(maxBoxes) || 12;
    const damage = Number(currentDamage) || 0;
    let output = '';
    for (let i = 1; i <= total; i++) {
      output += options.fn({
        index: i,
        filled: i <= damage
      });
    }
    return output;
  });

  // Edge points tracker generator (1 to maxEdge)
  handlebars.registerHelper('edgePointsBoxes', function (maxEdge, currentEdge, options) {
    const max = Number(maxEdge) || 7;
    const curr = Number(currentEdge) || 0;
    let output = '';
    for (let i = 1; i <= Math.max(7, max); i++) {
      output += options.fn({
        index: i,
        filled: i <= curr,
        disabled: i > max
      });
    }
    return output;
  });
};
