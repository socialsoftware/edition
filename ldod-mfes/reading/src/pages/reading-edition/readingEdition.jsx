import { getExpertEditionInter } from '../../apiRequests';
import './LdodReadingEdition';

const mount = async (lang, ref) => {
  const { acrn, xmlId, urlId } = history.state;
  getExpertEditionInter(acrn, xmlId, urlId)
    .then((data) => LdodReadingEdition.updateData(data))
    .catch((e) => console.error(e));
  const LdodReadingEdition = document
    .querySelector(ref)
    .appendChild(<ldod-reading-edition language={lang}></ldod-reading-edition>);
};

const unMount = () => document.querySelector('ldod-reading-edition')?.remove();

const path = '/:acrn/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
