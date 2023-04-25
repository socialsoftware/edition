/** @format */
import './frag-nav-panel.js';
import constants from '../../constants.js';
import { isVirtualInter } from '../../utils.js';
import references from '../../../../references.js';
/**
 * @param {[]} data
 * @param {string} type
 */

const dataArray = {
	witnesses: (data, inters) =>
		data.map(entry => ({
			inters: entry.map(inter => ({
				externalId: inter.externalId,
				current: { xmlId: inter.xmlId, urlId: inter.urlId, name: inter.shortName },
				checked: inters.has(inter.externalId),
			})),
		})),
	experts: (data, inters) => {
		return data.filter(Boolean).map(entry => ({
			name: entry[0]?.editor,
			url: references.edition(entry[0]?.acronym),
			inters: entry.map(inter => ({
				externalId: inter.externalId,
				previous: { xmlId: inter.prevXmlId, urlId: inter.prevUrlId },
				current: { xmlId: inter.xmlId, urlId: inter.urlId, name: inter.number.toString() },
				next: { xmlId: inter.nextXmlId, urlId: inter.nextUrlId },
				checked: inters.has(inter.externalId),
			})),
		}));
	},
};

export default function (root) {
	const inters = root.inters;
	const witnessesData = root.data.sortedSourceInterList;
	const expertsData = root.editorialInters;

	return /*html*/ `
			<frag-nav-panel 
				id="witnesses"
				data-input='${getDataInput('witnesses', root, dataArray.witnesses([witnessesData], inters))}'
			>
			</frag-nav-panel>
			<frag-nav-panel
				id="experts"
				data-input='${getDataInput('experts', root, dataArray.experts(expertsData, inters))}'
			>
			</frag-nav-panel>
			`;
}

function getDataInput(id, root, data) {
	return JSON.stringify({
		type: constants[root.language][id],
		tooltip: constants[root.language][`${id}Tooltip`],
		data,
	});
}
