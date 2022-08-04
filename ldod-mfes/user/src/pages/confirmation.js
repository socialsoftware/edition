import { navigateTo } from 'shared/router.js';
import { tokenConfirmRequest } from '../apiRequests.js';
import { emitMessageEvent, loadConstants } from '../utils.js';

const mount = async (lang, ref) => {
  const emitMessage = (key, type) =>
    loadConstants(lang).then((messages) =>
      emitMessageEvent(messages[key], type)
    );
  let params = new URL(document.location).searchParams;
  let path = `/sign-up-confirmation?token=${params.get('token')}`;
  await tokenConfirmRequest(path)
    .then(({ message }) => emitMessage(message))
    .catch(({ message }) => emitMessage(message), 'error');
  navigateTo('/user/signin');
};
const unMount = () => console.log('unmount');

const path = '/sign-up-confirmation';

export { mount, unMount, path };
