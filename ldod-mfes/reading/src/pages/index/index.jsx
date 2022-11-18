import { getStartReading } from '../../apiRequests';
import './LdodReading';

const mount = async (lang, ref) => {
  getStartReading().then((data) => {
    console.log(reading);
  }).catch;
  var reading = document
    .querySelector(ref)
    .appendChild(<ldod-reading language={lang}></ldod-reading>);
};

const unMount = () => document.querySelector('ldod-reading')?.remove();

const path = '';

export const index = () => ({
  mount,
  unMount,
  path,
});
