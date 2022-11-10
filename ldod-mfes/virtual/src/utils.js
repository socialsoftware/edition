export const computeSelectPureHeight = (e, node = document, delta = 80) => {
  const selectPure = node.querySelector('select-pure#virtual-associateTag');
  const height =
    selectPure.shadowRoot.querySelector('div.select > div.dropdown')
      .clientHeight + delta;
  selectPure.parentElement.style.height = selectPure.visible
    ? `${Math.max(height, 120)}px`
    : 'auto';
};
