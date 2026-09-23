// Client-side interactivity for the generic entity_form editor.
// Purely DOM manipulation - this preview app never writes changes back to
// the example JSON files. A consuming app can read the form via FormData
// (or listen for these same events) to implement its own persistence.
window.addEventListener('DOMContentLoaded', () => {
  const form = document.querySelector('.entity-editor');
  if (!form) return;

  // Re-numbers "key[N]" / "key[N].field" name attributes after add/remove
  // so submitted field names stay contiguous.
  function reindexList(listEl, key) {
    Array.from(listEl.children).forEach((li, index) => {
      li.querySelectorAll('[name]').forEach((el) => {
        el.name = el.name.replace(/^([^[]+)\[\d+\]/, `${key}[${index}]`);
      });
    });
  }

  form.addEventListener('click', (evt) => {
    const target = evt.target;

    // Ability score steppers (+/-)
    if (target.classList.contains('step-up') || target.classList.contains('step-down')) {
      const wrapper = target.closest('.field-ability-stepper');
      const input = wrapper && wrapper.querySelector('input[type="number"]');
      if (!input) return;
      const delta = target.classList.contains('step-up') ? 1 : -1;
      const min = input.min !== '' ? Number(input.min) : -Infinity;
      const max = input.max !== '' ? Number(input.max) : Infinity;
      const next = (Number(input.value) || 0) + delta;
      input.value = Math.min(max, Math.max(min, next));
      input.dispatchEvent(new Event('change', { bubbles: true }));
      return;
    }

    // Simple array-list: add/remove plain text entries
    if (target.classList.contains('add-array-item')) {
      const container = target.closest('.field-array-list');
      const list = container && container.querySelector('.array-list-entries');
      if (!list) return;
      const li = document.createElement('li');
      li.innerHTML = `<input type="text" value=""><button type="button" class="remove-array-item" aria-label="Remove item">&times;</button>`;
      list.appendChild(li);
      reindexList(list, container.dataset.arrayKey);
      return;
    }
    if (target.classList.contains('remove-array-item')) {
      const container = target.closest('.field-array-list');
      const list = container && container.querySelector('.array-list-entries');
      const li = target.closest('li');
      if (li) li.remove();
      if (list) reindexList(list, container.dataset.arrayKey);
      return;
    }

    // Action list editor: add/remove name+desc entries
    if (target.classList.contains('add-action')) {
      const container = target.closest('.field-action-list');
      const list = container && container.querySelector('.action-entries');
      if (!list) return;
      const li = document.createElement('li');
      li.className = 'action-entry';
      li.innerHTML =
        `<input type="text" class="action-name" value="" placeholder="Name">` +
        `<textarea class="action-desc" placeholder="Description"></textarea>` +
        `<button type="button" class="remove-action" aria-label="Remove action">&times; Remove</button>`;
      list.appendChild(li);
      reindexList(list, container.dataset.arrayKey);
      return;
    }
    if (target.classList.contains('remove-action')) {
      const container = target.closest('.field-action-list');
      const list = container && container.querySelector('.action-entries');
      const li = target.closest('li');
      if (li) li.remove();
      if (list) reindexList(list, container.dataset.arrayKey);
      return;
    }

    // Array-of-objects editor (attacks, feats, features_and_traits, class_levels, ...):
    // clone the widget's blank <template> row and append it.
    if (target.classList.contains('add-array-object')) {
      const container = target.closest('.field-array-of-objects');
      const list = container && container.querySelector('.array-object-entries');
      const tpl = container && container.querySelector('.array-object-entry-template');
      if (!list || !tpl) return;
      const fragment = tpl.content.cloneNode(true);
      list.appendChild(fragment);
      return;
    }
    if (target.classList.contains('remove-array-object')) {
      const entry = target.closest('.array-object-entry');
      if (entry) entry.remove();
      return;
    }
  });

  // This is a template preview - never submit/persist automatically.
  form.addEventListener('submit', (evt) => {
    evt.preventDefault();
    console.log('[entity_form] Preview only - not saved. Form data:', new FormData(form));
  });
});
