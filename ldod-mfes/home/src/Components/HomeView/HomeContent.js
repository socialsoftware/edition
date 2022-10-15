import { excerpts } from '../../../resources/home/constants/excerpts.js';
import { parseHTML } from 'shared/utils.js';
import { isMFEAvailable } from '../../utils.js';

const random = parseInt(Math.random() * 2) + 1;
const path = import.meta.url.includes('src') ? '../../../' : '';

const getUrl = () =>
  new URL(`${path}resources/home/webp/xxx.webp`, import.meta.url).href;

export const boxUrl = (version, key, lang) =>
  new URL(
    `${path}resources/home/svg/${version}-${lang}-${key}-${random}.svg`,
    import.meta.url
  ).href;

export const boxUrlH = (version, key, lang) =>
  new URL(
    `${path}resources/home/svg/${version}-${lang}-${key}-${random}-h.svg`,
    import.meta.url
  ).href;

const excerpt = excerpts[parseInt(Math.random() * excerpts.length)];

const boxes = [
  isMFEAvailable('reading') && { mod: 'reading', path: '/reading', index: 1 },
  isMFEAvailable('text') && {
    mod: 'documents',
    path: '/text/fragments',
    index: 2,
  },
  (isMFEAvailable('text') || isMFEAvailable('virutal')) && {
    mod: 'editions',
    path: '/text/edition',
    index: 3,
  },
  isMFEAvailable('search') && {
    mod: 'search',
    path: '/search/simple',
    index: 4,
  },
  isMFEAvailable('virtual') && {
    mod: 'virtual',
    path: '/virtual/virtual-editions',
    index: 5,
  },
].filter(Boolean);
export default (language, constants) => {
  const xmlId = excerpt.path.split('/')[0];
  const urlId = excerpt.path.split('/')[2];
  return parseHTML(html`
    <div>
      <div class="container ldod-default">
        <a
          is="nav-to"
          ${isMFEAvailable('text')
            ? html`to="/reading/fragment/${xmlId}/inter/${urlId}`
            : ''}
          class="home-frag-link"
        >
          <div class="raw col-xs-12 frag-excerpt">
            <span class="frag-number font-egyptian">${excerpt.number}</span>
            <span class="frag-editor font-condensed">${excerpt.editor}</span>
          </div>
        </a>
        <div class="frag-excerpt-text font-grotesque">
          <p>${excerpt.text}</p>
        </div>
        <hr class="line-points" />
        <div class="about font-monospace">
          <p id="about" class="update-language">${constants.about}</p>
        </div>
        <hr class="line-x" style="background: url(${getUrl()})  repeat-x 0 0" />
        <div class="menu-boxes hidden-xs col-xs-12">
          ${boxes.reduce((prev, { mod, path, index }) => {
            return prev.concat(boxGroup(mod, path, index, 'D', language));
          }, '')}
        </div>
        <div class="menu-boxes visible-xs-inline col-xs-12">
          ${boxes.reduce((prev, { mod, path, index }) => {
            return prev.concat(boxGroup(mod, path, index, 'M', language));
          }, '')}
        </div>
      </div>
      <home-info language=${language} class="language"></home-info>
    </div>
  `);
};

const boxGroup = (module, path, index, version, language) => {
  const key = `0${index}`;
  return html` ${key !== '01' ? html`<hr class="line-points" />` : ''}
    <a is="nav-to" to="${path}">
      <div class="div-link">
        <img
          id="${module}"
          version=${version}
          key="${key}"
          class="not-hover"
          src=${boxUrl(version, key, language)}
          alt="${module}"
        />
        <img
          id="${module}"
          version=${version}
          key="${key}"
          class="hover"
          src=${boxUrlH(version, key, language)}
          alt="${module}"
        />
      </div>
    </a>`;
};
