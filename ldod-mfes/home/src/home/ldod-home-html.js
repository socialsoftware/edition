/** @format */

import style from './style/home.css?inline';
import containerCss from '../nav-bar/style/container.css?inline';
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

export default lang => {
	const xmlId = excerpt.path.split('/')[0];
	const urlId = excerpt.path.split('/')[2];
	return /*html*/ `
		<style>
			${style + containerCss}
		</style>
		<div class="container-md ldod-default">
			<a is="nav-to-new"
				data-mfe="reading"
				data-mfe-key="editionInterPath"
				data-mfe-key-params='["${xmlId}","${urlId}"]'
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
				<p data-home-key="about">${constants[lang].about}</p>
			</div>
			<hr class="line-x" />
			<div class="menu-boxes hidden-xs">
				${getBox('D', 'reading', 'index', '', lang, random, '01')}
				<hr class="line-points" />
				${getBox('D', 'text', 'fragments', '', lang, random, '02')}
				<hr class="line-points" />
				${getBox('D', 'text', 'editions', '', lang, random, '03')}
				<hr class="line-points" />
				${getBox('D', 'search', 'simple', '', lang, random, '04')}
				<hr class="line-points" />
				${getBox('D', 'virtual', 'virtualEditions', '', lang, random, '05')}
			</div>
			<div class="menu-boxes visible-xs-inline ">
				${getBox('M', 'reading', 'index', '', lang, random, '01')}
				<hr class="line-points" />
				${getBox('M', 'text', 'fragments', '', lang, random, '02')}
				<hr class="line-points" />
				${getBox('M', 'text', 'editions', '', lang, random, '03')}
				<hr class="line-points" />
				${getBox('M', 'search', 'simple', '', lang, random, '04')}
				<hr class="line-points" />
				${getBox('M', 'virtual', 'virtualEditions', '', lang, random, '05')}
			</div>
		</div>
`;
};

function getBox(version, mfe, mfeKey, mfeKeyparams, lang, random, key) {
	return /*html*/ `
		<a  is="nav-to-new"
			data-mfe="${mfe}"
			data-mfe-key="${mfeKey}"
			data-mfe-key-params='[${mfeKeyparams ? mfeKeyparams.map(p => '${p}').join(',') : ''}]'
		>
			<div class="div-link">
				<img
					loading="lazy"
					version
					key
					class="not-hover"
					id="${version}-${lang}-${key}-${random}"
					alt="${mfe}"
				/>
				<img
					loading="lazy"
					version
					key
					class="hover"
					id="${version}-${lang}-${key}-${random}-h"
					alt="${mfe}"
				/>
			</div>
		</a>
	`;
}
