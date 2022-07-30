import { navigateTo } from 'shared/router.js';
import { tokenAuthRequest } from '../apiRequests.js';
import { emitMessageEvent, loadConstants } from '../utils.js';

const mount = async (lang, ref) => {
  const emitMessage = (key, type) =>
    loadConstants(lang).then((messages) =>
      emitMessageEvent(messages[key], type)
    );
  let params = new URL(document.location).searchParams;
  let path = `/auth?token=${params.get('token')}`;
  tokenAuthRequest(path)
    .then(({ message }) => emitMessage(message))
    .catch(({ message }) => emitMessage(message), 'error');
  navigateTo('/');
};
const unMount = () => console.log('unmount');

export { mount, unMount };
