import { getStartReading } from '../../api-requests';
import './ldod-reading';

const mount = async (lang, ref) => {
  getStartReading()
    .then((data) => LdodReading.updateData(data))
    .catch((e) => console.error(e));
  const LdodReading = document
    .querySelector(ref)
    .appendChild(<ldod-reading language={lang}></ldod-reading>);
};

const unMount = () => document.querySelector('ldod-reading')?.remove();

const path = '/';

export const index = () => ({
  mount,
  unMount,
  path,
});
