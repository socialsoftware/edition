/** @format */

import './home-info.js';
import excerpts from './constants/excerpts.js';
import constants from './constants/constants.js';
import {
	textReferences,
	readingReferences,
	searchReferences,
	virtualReferences,
} from '../external-deps.js';

const random = parseInt(Math.random() * 2) + 1;

const excerpt = excerpts[parseInt(Math.random() * excerpts.length)];

export default root => {
	const xmlId = excerpt.path.split('/')[0];
	const urlId = excerpt.path.split('/')[2];
	return /*html*/ `
		<div class="container-md ldod-default">
			<a
				is="nav-to"
				to="${readingReferences?.editionInterPath?.(xmlId, urlId)}"
				class="home-frag-link"
			>
				<div class="frag-excerpt">
					<span class="frag-number font-egyptian">${excerpt.number}</span>
					<span class="frag-editor font-condensed">${excerpt.editor}</span>
				</div>
				<div class="frag-excerpt-text font-grotesque">
					<p>${excerpt.text}</p>
				</div>
			</a>

			<hr class="line-points" />
			<div class="about font-monospace">
				<p data-home-key="about">${constants[root.language].about}</p>
			</div>
			<hr class="line-x" />
			<div class="menu-boxes hidden-xs">
				${getBox('D', readingReferences.index?.(), root.language, random, '01')}
				<hr class="line-points" />
				${getBox('D', textReferences.fragments?.(), root.language, random, '02')}
				<hr class="line-points" />
				${getBox('D', textReferences.editions?.(), root.language, random, '03')}
				<hr class="line-points" />
				${getBox('D', searchReferences.simple?.(), root.language, random, '04')}
				<hr class="line-points" />
				${getBox('D', virtualReferences.virtualEditions?.(), root.language, random, '05')}
			</div>
			<div class="menu-boxes visible-xs-inline ">
				${getBox('M', readingReferences.index?.(), root.language, random, '01')}
				<hr class="line-points" />
				${getBox('M', textReferences.fragments?.(), root.language, random, '02')}
				<hr class="line-points" />
				${getBox('M', textReferences.editions?.(), root.language, random, '03')}
				<hr class="line-points" />
				${getBox('M', searchReferences.simple?.(), root.language, random, '04')}
				<hr class="line-points" />
				${getBox('M', virtualReferences.virtualEditions?.(), root.language, random, '05')}
			</div>
		</div>
		<home-info language="${root.language}"></home-info>
	`;
};

function getBox(version, path, lang, random, key) {
	return /*html*/ `
		<a is="nav-to" to="${path}">
			<div class="div-link">
				<img
					loading="lazy"
					version
					key
					class="not-hover"
					src="${getUrl(version, lang, random, key)}"
					alt="${path}"
				/>
				<img
					loading="lazy"
					version
					key
					class="hover"
					src="${getUrl(version, lang, random, key, true)}"
					alt="${path}"
				/>
			</div>
		</a>
	`;
}

function getUrl(version, lang, random, key, isHover) {
	const path = `${import.meta.env.VITE_BASE}resources/svg/${version}-${lang}-${key}-${random}${
		isHover ? '-h' : ''
	}.svg`;
	return new URL(path, import.meta.url).href;
}
