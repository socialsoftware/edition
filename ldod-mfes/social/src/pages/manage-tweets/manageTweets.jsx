import { getTweetsToManage } from '@src/apiRequests.js';
import './LdodManageTweets.jsx';

const mount = (lang, ref) => {
  getTweetsToManage().then((data) => {
    const manageTweets = document.querySelector(`${ref}>ldod-manage-tweets`);
    manageTweets.tweets = data;
    manageTweets.toggleAttribute('data');
  });
  document
    .querySelector(ref)
    .appendChild(<ldod-manage-tweets language={lang}></ldod-manage-tweets>);
};
const unMount = () => document.querySelector('ldod-citations')?.remove();

const path = '/manage-tweets';

export { mount, unMount, path };
