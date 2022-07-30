import { G_CLIENT_ID } from '../src/resources/constants';
import { socialAuthRequest } from './apiRequests.js';
import { emitMessageEvent } from './utils';
let constants;

function getGsiClientScript() {
  const script = document.createElement('script');
  script.src = 'https://accounts.google.com/gsi/client';
  script.addEventListener('load', onLoadGoogleAuth);
  return script;
}

export function socialAuth(provider, messages) {
  constants = messages;
  switch (provider) {
    case 'google':
      document.head.appendChild(getGsiClientScript());
      break;
    default:
      break;
  }
}

const handleCredentials = ({ credential }) =>
  socialAuthRequest('google', { accessToken: credential })
    .then(
      (response) => response && emitMessageEvent(constants[response.message])
    )
    .catch((error) => emitMessageEvent(constants[error.message], 'error'));

function onLoadGoogleAuth() {
  const id = window.google.accounts.id;
  id.initialize({
    ...G_CLIENT_ID,
    callback: handleCredentials,
  });
  id.prompt();
}
