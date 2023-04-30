/** @format */
import './frag-nav-panel.js';
import constants from '../../constants.js';
import references from '../../../../references.js';
/**
 * @param {[]} data
 * @param {string} type
 */

const dataArray = {
	/**
	 *
	 * @param {string[]} inters
	 * @returns
	 */
	witnesses: (data, inters) =>
		data.map(entry => ({
			inters: entry.map(inter => ({
				externalId: inter.externalId,
				current: { xmlId: inter.xmlId, urlId: inter.urlId, name: inter.shortName },
				checked: inters.includes(inter.externalId),
			})),
		})),

	/**
	 *
	 * @param {string[]} inters
	 * @returns
	 */
	experts: (data, inters) => {
		return data.filter(Boolean).map(entry => ({
			name: entry[0]?.editor,
			url: references.edition(entry[0]?.acronym),
			inters: entry.map(inter => ({
				externalId: inter.externalId,
				previous: { xmlId: inter.prevXmlId, urlId: inter.prevUrlId },
				current: { xmlId: inter.xmlId, urlId: inter.urlId, name: inter.number.toString() },
				next: { xmlId: inter.nextXmlId, urlId: inter.nextUrlId },
				checked: inters.includes(inter.externalId),
			})),
		}));
	},
};

export default function (data, intersSelected, language) {
	const inters = intersSelected;
	const witnessesData = data.sortedSourceInterList;
	const expertsData = Object.values(data.expertsInterMap);

	return /*html*/ `
		<div id="text-frag-nav-container">
			<frag-nav-panel 
				id="witnesses"
				data-input='${getDataInput('witnesses', language, dataArray.witnesses([witnessesData], inters))}'
			>
			</frag-nav-panel>
			<frag-nav-panel
				id="experts"
				data-input='${getDataInput('experts', language, dataArray.experts(expertsData, inters))}'
			>
			</frag-nav-panel>
		</div>
		`;
}

function getDataInput(id, lang, data) {
	return JSON.stringify({
		type: constants[lang][id],
		tooltip: constants[lang][`${id}Tooltip`],
		data,
	});
}
