import { fetchAnnotations } from './api-requests';
import { NewAnnPopover } from './components/new-annotation/new-annotation-popover';
import { LdodAnnotation } from './components/ldod-annotation/ldod-annotation';
import { Annotation } from './annotation';
import { parseHTML } from 'shared/utils.js';
const html = String.raw;

export let ldodAnnotationComponent;
export let annotationsList = [];
let refNode;
let data;
let popover;

const annotationIdByXpath = (xPath) =>
  `${xPath.start}/${xPath.startOffset}${xPath.end}/${xPath.endOffset}`;

const processNonHumanAnnotations = (annotations) => {
  let nonHumamGroupedByRange = annotations.filter(
    (ann) => !ann.human && ann.username === 'Twitter'
  );

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

  const nonHumanMerged = Object.values(nonHumamGroupedByRange).map((val) =>
    val.reduce((prev, curr) => {
      if (!Object.keys(prev).length)
        prev = { ...curr, text: document.createElement('div') };
      prev.text.appendChild(
        parseHTML(html`
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
        `)
      );
      return prev;
    }, {})
  );
  annotations = annotations.filter((ann) => ann.human);
  nonHumanMerged.forEach((ann) => annotations.push(ann));
  return annotations;
};

export async function processExistingAnnotations(
  annotations = data.annotations
) {
  annotations = processNonHumanAnnotations(annotations);
  annotationsList.forEach((ann) => {
    ann?.destroyAndnormalizeCommonAncestor();
  });

  annotationsList = await Promise.all(
    annotations.map((ann) =>
      new Annotation(ann, refNode).highlight().catch((e) => console.error(e))
    )
  );
}

export function updateFetchedData(res) {
  data = res;
  processExistingAnnotations();
  ldodAnnotationComponent.categories = data.categories;
  ldodAnnotationComponent.openVocab = data.openVocab;
  if (!data.contributor && popover) popover.remove();
}

export default async ({ interId, referenceNode }) => {
  checkArgs(interId, referenceNode);
  refNode = referenceNode;

  ldodAnnotationComponent && ldodAnnotationComponent.remove();
  ldodAnnotationComponent = refNode.parentElement.appendChild(
    new LdodAnnotation(interId)
  );

  await fetchAnnotations(interId)
    .then(updateFetchedData)
    .catch((error) => console.error(error));

  popover && popover.remove();
  if (data.contributor)
    popover = refNode.parentElement.appendChild(
      new NewAnnPopover(refNode, ldodAnnotationComponent, interId)
    );
};

function checkArgs(interId, referenceNode) {
  if (!interId || !referenceNode || !referenceNode.isConnected)
    throw new Error('Invalid arguments');
}
