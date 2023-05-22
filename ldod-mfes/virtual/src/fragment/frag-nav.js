/** @format */
import references from '../references';
import constants from './constants';
const dataArray = {
	virtual: (data, inters) =>
		data.map(entry => ({
			name: entry.acronym,
			url: references.virtualEdition(entry.acronym),
			add: entry.canAddInter && entry.member,
			interId: entry.interExternalId,
			veId: entry.veExternalId,
			inters: entry.inters.map(inter => ({
				externalId: inter.externalId,
				previous: { xmlId: inter.prevXmlId, urlId: inter.prevUrlId },
				current: {
					xmlId: inter.xmlId,
					urlId: inter.urlId,
					name: inter.number.toString(),
				},
				next: { xmlId: inter.nextXmlId, urlId: inter.nextUrlId },
				checked: inters.includes(inter.externalId),
			})),
		})),
};

export default ({ lang, virtualEditions, intersChecked }) => {
	const id = 'virtual';
	return /*html*/ `<frag-nav-panel id="${id}" data-input='${getDataInput(
		id,
		lang,
		dataArray.virtual(
			virtualEditions,
			intersChecked.map(item => item.externalId)
		)
	)}'></frag-nav-panel>`;
};

function getDataInput(id, lang, data) {
	return JSON.stringify({
		type: constants[lang][id],
		tooltip: constants[lang][`${id}Tooltip`],
		data,
	});
}
