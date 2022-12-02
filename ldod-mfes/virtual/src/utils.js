export const computeSelectPureHeight = (selectPure, delta = 80) => {
  const height =
    selectPure.shadowRoot.querySelector('div.select > div.dropdown')
      .clientHeight + delta;
  selectPure.parentElement.style.height = selectPure.visible
    ? `${Math.max(height, 120)}px`
    : 'auto';
};
export const isDev = () => import.meta.env.DEV;
