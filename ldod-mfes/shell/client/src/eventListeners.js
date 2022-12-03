import { store } from './store.js';
import 'shared/modal.js';

const onToken = ({ detail: token }) => {
  store.setState(token);
};

window.addEventListener('ldod-token', onToken);

window.addEventListener('ldod-logout', () => {
  onToken({ detail: { token: undefined } });
});
const onLanguage = ({ detail: { language } }) => {
  store.setState({ language });
};

window.addEventListener('ldod-language', onLanguage);

const handleErrorMessage = ({ detail }) => {
  const modal = document.querySelector('body.ldod-default>ldod-modal#error');
  const body = modal.querySelector('div[slot="body-slot"]');
  body.innerHTML = detail?.message || 'Something went wrong';
  modal.toggleAttribute('show', true);
};

const handleInfoMessage = ({ detail }) => {
  const modal = document.querySelector('body.ldod-default>ldod-modal#success');
  const body = modal.querySelector('div[slot="body-slot"]');
  body.innerHTML = detail.message || '';
  modal.toggleAttribute('show', true);
};

window.addEventListener('ldod-message', handleInfoMessage);
window.addEventListener('ldod-error', handleErrorMessage);
