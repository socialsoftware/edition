import { navigateTo } from 'shared/router.js';
import { tokenAuthRequest } from '../api-requests.js';
import { emitMessageEvent } from '../utils.js';

const mount = async (lang, ref) => {
  let params = new URL(document.location).searchParams;
  let path = `/sign-up-authorization?token=${params.get('token')}`;
  tokenAuthRequest(path)
    .then((res) => {
      console.log(res);
      res && emitMessageEvent(res.message);
    })
    .catch((error) => {
      console.error(error);
      error && emitMessageEvent(error.message, 'error');
    });
  navigateTo('/');
};
const unMount = () => console.info('unmount');

const path = '/sign-up-authorization';

export { mount, unMount, path };
