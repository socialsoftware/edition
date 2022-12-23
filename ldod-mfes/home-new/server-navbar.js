import { navbarHtml } from './src/components/navbar/navbar.js';

export const staticGeneration = () => {
  return /*html*/ `
    <template shadowroot="open">
      ${navbarHtml()}
    </template>`;
};
