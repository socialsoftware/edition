import { getExpertEditionInter } from '../../api-requests';
import { errorPublisher } from '../../events-module';
import './ldod-reading-edition';

const mount = async (lang, ref) => {
	const { xmlId, urlId } = history.state;
	getExpertEditionInter(xmlId, urlId)
		.then(data => LdodReadingEdition.updateData(data))
		.catch(e => errorPublisher(e?.message));
	const LdodReadingEdition = document
		.querySelector(ref)
		.appendChild(<ldod-reading-edition language={lang}></ldod-reading-edition>);
};

const unMount = () => document.querySelector('ldod-reading-edition')?.remove();

const path = '/fragment/:xmlId/inter/:urlId';

export { mount, unMount, path };
