import { fetcher } from 'shared/fetcher.js';

import router from './src/virtual.js';
router.mount('en', '#root');

document.querySelectorAll('button.lang').forEach((btn) => {
  btn.addEventListener('click', () =>
    document.querySelectorAll('[language]').forEach((ele) => {
      ele.setAttribute('language', btn.id);
    })
  );
});

document.querySelector('form#tokenForm').onsubmit = async (e) => {
  e.preventDefault();
  const form = e.target;
  const data = Object.fromEntries(new FormData(form));
  fetcher
    .post(`${import.meta.env.VITE_HOST}/auth/sign-in`, data)
    .then((data) => {
      if (data.accessToken) {
        window.token = data.accessToken;
        form.toggleAttribute('auth', true);
        form.reset();
      }
    });
};
