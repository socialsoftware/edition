import { excerpts } from '../../../resources/home/constants/excerpts.js';
import { parseHTML } from 'shared/utils.js';
import { textReferences, readingReferences, searchReferences, virtualReferences } from '../../external-deps.js';

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
  readingReferences && {
    mod: 'reading',
    path: readingReferences.index?.(),
    index: 1,
  },
  textReferences && {
    mod: 'documents',
    path: textReferences.fragments?.(),
    index: 2,
  },
  textReferences && {
    mod: 'editions',
    path: textReferences.editions?.(),
    index: 3,
  },
  searchReferences && {
    mod: 'search',
    path: searchReferences.simple?.(),
    index: 4,
  },
  virtualReferences && {
    mod: 'virtual',
    path: virtualReferences.virtualEditions?.(),
    index: 5,
  },
].filter(Boolean);

const boxGroup = (module, path, index, version, language) => {
  const key = `0${index}`;
  return html` ${key !== '01' ? '<hr class="line-points" />' : ''}
    <a is="nav-to" to="${path}">
      <div class="div-link">
        <img
          loading="lazy"
          id="${module}"
          version=${version}
          key="${key}"
          class="not-hover"
          src=${boxUrl(version, key, language)}
          alt="${module}"
        />
        <img
          loading="lazy"
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

export default (language, constants) => {
  const xmlId = excerpt.path.split('/')[0];
  const urlId = excerpt.path.split('/')[2];
  return parseHTML(html`
    <div>
      <div class="container ldod-default">
        <a
          is="nav-to"
          to="${readingReferences?.editionInterPath?.(xmlId, urlId)}"
          class="home-frag-link"
        >
          <div class="raw col-xs-12 frag-excerpt">
            <span class="frag-number font-egyptian">${excerpt.number}</span>
            <span class="frag-editor font-condensed">${excerpt.editor}</span>
          </div>
          <div class="frag-excerpt-text font-grotesque">
            <p>${excerpt.text}</p>
          </div>
        </a>

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
