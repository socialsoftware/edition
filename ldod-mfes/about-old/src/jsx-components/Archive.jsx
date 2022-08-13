/** @jsx createElement */
/** @jsxFrag createFragment */

import { ldodRender } from 'shared/public/utils.js';
import { createElement, createFragment } from 'shared/public/vanilla-jsx.js';

const Archive = async (lang) =>
  (await import(`../pages/archive/Archive-${lang}.js`)).default;

export const mount = async (language, ref) => {
  console.log(ref);
  const archive = (
    <div>
      <p>
        &nbsp;
        {(await Archive(language))()}
      </p>
    </div>
  );
  ldodRender(archive, document.querySelector(ref));
};

export const unMount = () => {
  const archive = document.querySelector('div#about-archive');
  archive?.remove();
};
