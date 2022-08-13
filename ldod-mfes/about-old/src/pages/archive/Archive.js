import { parseHTML, ldodRender } from 'shared/public/utils.js';

const Archive = async (lang) => (await import(`./Archive-${lang}.js`)).default;

export const mount = async (language, ref) => {
  const archive = parseHTML(html`<div id="about-archive" class="ldod-default">
    <p>&nbsp;</p>
    ${(await Archive(language))()}
  </div>`);
  ldodRender(archive, document.querySelector(ref));
};
export const unMount = () => {
  const archive = document.querySelector('div#about-archive');
  archive?.remove();
};
