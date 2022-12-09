import { navigateTo } from 'shared/router.js';
import { tokenConfirmRequest } from '../api-requests.js';
import { userReferences } from '../user-references';
import { emitMessageEvent } from '../utils.js';

const mount = async (lang, ref) => {
  let params = new URL(document.location).searchParams;
  let path = `/sign-up-confirmation?token=${params.get('token')}`;
  await tokenConfirmRequest(path)
    .then((res) => {
      console.log(res);
      res && emitMessageEvent(res.message);
    })
    .catch((error) => {
      console.error(error);
      error && emitMessageEvent(error.message, 'error');
    });
  navigateTo(userReferences.signin());
};
const unMount = () => console.info('unmount');

const path = '/sign-up-confirmation';

export { mount, unMount, path };
