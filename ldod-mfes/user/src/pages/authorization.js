import { navigateTo } from 'shared/router.js';
import { tokenAuthRequest } from '../apiRequests.js';
import { emitMessageEvent, loadConstants } from '../utils.js';

const mount = async (lang, ref) => {
  const emitMessage = (key, type) =>
    loadConstants(lang).then((messages) =>
      emitMessageEvent(messages[key], type)
    );
  let params = new URL(document.location).searchParams;
  let path = `/sign-up-authorization?token=${params.get('token')}`;
  tokenAuthRequest(path)
    .then((res) => res && emitMessage(res.messages))
    .catch((error) => console.error(error));
  navigateTo('/');
};
const unMount = () => console.log('unmount');

const path = '/sign-up-authorization';

export { mount, unMount, path };
