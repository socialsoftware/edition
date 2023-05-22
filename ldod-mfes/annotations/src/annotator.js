/** @format */

import { fetchAnnotations } from './api-requests';
import { NewAnnPopover } from './components/new-annotation/new-annotation-popover';
import { LdodAnnotation } from './components/ldod-annotation/ldod-annotation';
import { Annotation } from './annotation';
import { annotationsList, mutateAnnotationsList } from './ldod-annotations.js';

export let ldodAnnotationComponent;
let refNode;
let data;
let popover;

window.hash = str =>
	str.split('').reduce((prv, cur) => ((prv << 5) - prv + cur.charCodeAt(0)) | 0, 0);

const selectionStyle = () => {
	const style = document.createElement('style');
	style.innerHTML = /*css*/ `
  ::selection {
    background-color: mark;
    color: marktext;
  }`;
	return style;
};

const annotationIdByXpath = xPath =>
	`${xPath.start}/${xPath.startOffset}${xPath.end}/${xPath.endOffset}`;

/**
 *
 * @param {[]} annotations
 * @returns
 */
const processNonHumanAnnotations = annotations => {
	let nonHumamGroupedByRange = annotations.filter(isNotHumanAnn);
	if (!nonHumamGroupedByRange.length) return annotations;
	nonHumamGroupedByRange = nonHumamGroupedByRange.reduce((prev, curr) => {
		let xPathId = annotationIdByXpath(curr.ranges[0]);
		if (Object.keys(prev).includes(xPathId)) {
			prev[xPathId].push(curr);
			return prev;
		}
		prev[xPathId] = [curr];
		return prev;
	}, {});

	const nonHumanMerged = Object.values(nonHumamGroupedByRange).map(val =>
		val.reduce((prev, curr) => {
			if (!Object.keys(prev).length) prev = { ...curr, text: document.createElement('div') };
			prev.text.innerHTML = /*html*/ `
          <div>
            <div>
              <a href=${curr.sourceLink} target="_blank">Tweet</a>
            </div>
            <div>
              <a href=${curr.profileURL} target="_blank"
                >${curr.profileURL.split('https://twitter.com/')[1]}</a
              >
            </div>
            <p>Date: ${curr.date}</p>
          </div>
        `;
			return prev;
		}, {})
	);
	annotations = annotations.filter(ann => ann.human);
	nonHumanMerged.forEach(ann => annotations.push(ann));
	return annotations;
};

export async function processExistingAnnotations(annotations = data.annotations) {
	annotations = processNonHumanAnnotations(annotations);
	annotationsList.forEach(ann => {
		ann?.destroyAndnormalizeCommonAncestor();
	});
	let annList = await Promise.all(
		annotations.map(ann =>
			new Annotation(ann, refNode).highlight().catch(e => console.error(e))
		)
	);
	mutateAnnotationsList(annList);
}

export function updateFetchedData(res) {
	data = res;
	processExistingAnnotations();
	ldodAnnotationComponent.categories = data.categories;
	ldodAnnotationComponent.openVocab = data.openVocab;
	if (!data.contributor && popover) popover.remove();
}

export async function annotatorService({ id, element }) {
	checkArgs(id, element);
	refNode = element;
	element.appendChild(selectionStyle());

	ldodAnnotationComponent && ldodAnnotationComponent.remove();
	ldodAnnotationComponent = refNode.parentElement.appendChild(new LdodAnnotation(id));

	await fetchAnnotations(id)
		.then(updateFetchedData)
		.catch(error => console.error(error));

	popover && popover.remove();
	if (data.contributor)
		popover = refNode.parentElement.appendChild(
			new NewAnnPopover(refNode, ldodAnnotationComponent, id)
		);
}

function checkArgs(id, element) {
	if (!id || !element || !element.isConnected) throw new Error('Invalid arguments');
}

function isNotHumanAnn(ann) {
	return !ann.human && ann.username === 'Twitter';
}
