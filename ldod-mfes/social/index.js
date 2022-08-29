import router from './src/social.js';

router.mount('en', '#root');

document.querySelectorAll('button.lang').forEach((btn) => {
  btn.addEventListener('click', () =>
    document.querySelectorAll('[language]').forEach((ele) => {
      ele.setAttribute('language', btn.id);
    })
  );
});
