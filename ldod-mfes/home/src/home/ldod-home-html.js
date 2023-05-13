/** @format */

import style from './style/home.css?inline';
import common from './style/common.css?inline';
import excerpts from './constants/excerpts.js';
import constants from './constants/constants.js';

const random = parseInt(Math.random() * 2) + 1;

const excerpt = excerpts[parseInt(Math.random() * excerpts.length)];

export default lang => {
	const xmlId = excerpt.path.split('/')[0];
	const urlId = excerpt.path.split('/')[2];
	return /*html*/ `
		<style>
			${style + common}
		</style>
		<div class="ldod-default">
			<a is="nav-to"
				data-mfe="reading"
				data-mfe-key="editionInterPath"
				data-mfe-key-params='["${xmlId}","${urlId}"]'
				class="home-frag-link"
				content
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
		<a  is="nav-to"
			data-mfe="${mfe}"
			data-mfe-key="${mfeKey}"
			data-mfe-key-params='[${mfeKeyparams ? mfeKeyparams.map(p => '${p}').join(',') : ''}]'
			content
		>
			<div class="div-link" >
				<div class="skeleton"></div>
				<img
					loading="lazy"
					version
					key
					class="not-hover"
					id="svg/${version}-${lang}-${key}-${random}.svg"
				/>
				<img
					loading="lazy"
					version
					key
					class="hover"
					id="svg/${version}-${lang}-${key}-${random}-h.svg"
				/>
			</div>
		</a>
	`;
}
