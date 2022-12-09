import { G_CLIENT_ID } from './resources/constants';
import { socialAuthRequest } from './api-requests.js';
import { emitMessageEvent } from './utils';
let constants;

function getGsiClientScript(loginCB) {
  const script = document.createElement('script');
  script.src = 'https://accounts.google.com/gsi/client';
  script.addEventListener('load', () => onLoadGoogleAuth(loginCB));
  return script;
}

export function socialAuth(provider, node) {
  constants = node.constants;
  switch (provider) {
    case 'google':
      document.head.appendChild(getGsiClientScript(node.onAuthSuccess));
      break;
    default:
      break;
  }
}

const handleCredentials = (credential, loginCB) =>
  socialAuthRequest('google', { accessToken: credential }, loginCB)
    .then(
      (response) => response && emitMessageEvent(constants[response.message])
    )
    .catch((error) => emitMessageEvent(constants[error.message], 'error'));

function onLoadGoogleAuth(loginCB) {
  const id = window.google.accounts.id;
  id.initialize({
    ...G_CLIENT_ID,
    callback: ({ credential }) => handleCredentials(credential, loginCB),
  });
  id.prompt();
}
