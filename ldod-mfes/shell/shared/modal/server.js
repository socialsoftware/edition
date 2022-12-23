import modalHTMLSerialized from './src/components/modal.js';

export const staticGeneration = (dialogClass, noFooter) => {
  return /*html*/ `
    <template shadowroot="open">
      ${modalHTMLSerialized(dialogClass, noFooter)}
    </template>`;
};
