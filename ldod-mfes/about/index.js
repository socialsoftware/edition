import router from '@src/about.js';

router.mount('pt', '#app');

document.querySelectorAll('button.lang').forEach((btn) => {
  btn.addEventListener('click', () =>
    document.querySelectorAll('[language]').forEach((ele) => {
      ele.setAttribute('language', btn.id);
    })
  );
});
