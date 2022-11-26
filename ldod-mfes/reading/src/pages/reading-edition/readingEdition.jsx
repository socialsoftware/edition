import { getExpertEditionInter } from '../../apiRequests';
import './LdodReadingEdition';

function errorEvent(message) {
  return new CustomEvent('ldod-error', {
    detail: { message },
    composed: true,
    bubbles: true,
  });
}

const mount = async (lang, ref) => {
  const { xmlId, urlId } = history.state;
  getExpertEditionInter(xmlId, urlId)
    .then((data) => LdodReadingEdition.updateData(data))
    .catch((e) => LdodReadingEdition.dispatchEvent(errorEvent(e?.message)));
  const LdodReadingEdition = document
    .querySelector(ref)
    .appendChild(<ldod-reading-edition language={lang}></ldod-reading-edition>);
};

const unMount = () => document.querySelector('ldod-reading-edition')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
