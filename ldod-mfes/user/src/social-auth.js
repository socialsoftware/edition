import { G_CLIENT_ID } from './resources/constants';
import { socialAuthRequest } from './api-requests.js';
import { errorPublisher, messagePublisher } from './events-modules';
let constants;

function getGsiClientScript(loginCB) {
  const script = document.createElement('script');
  script.src = 'https://accounts.google.com/gsi/client';
  script.addEventListener('load', () => onLoadGoogleAuth(loginCB));
  return script;
}

export function socialAuth(provider, node) {
  constants = node.constants;
  if (provider === 'google')
    document.head.appendChild(getGsiClientScript(node.onAuthSuccess));
}

const handleCredentials = (credential, loginCB) =>
  socialAuthRequest('google', { accessToken: credential }, loginCB)
    .then(
      (response) => response && messagePublisher(constants[response.message])
    )
    .catch((error) => errorPublisher(constants[error.message]));

function onLoadGoogleAuth(loginCB) {
  const id = window.google.accounts.id;
  id.initialize({
    ...G_CLIENT_ID,
    callback: ({ credential }) => handleCredentials(credential, loginCB),
  });
  id.prompt();
}
