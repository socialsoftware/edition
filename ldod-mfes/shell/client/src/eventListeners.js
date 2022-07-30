import { store } from './store.js';

const onToken = ({ detail: { token } }) => store.setState({ token });
window.addEventListener('ldod-token', onToken);

window.addEventListener('ldod-logout', () =>
  onToken({ detail: { token: undefined } })
);
const onLanguage = ({ detail: { language } }) => {
  store.setState({ language });
};
window.addEventListener('ldod-language', onLanguage);
