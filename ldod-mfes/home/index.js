//import 'user';
import './src/components/navbar/ldod-navbar.js';
//import './src/Components/HomeView/Home.js';

window.addEventListener('ldod-language', ({ detail: { language } }) =>
  document
    .querySelectorAll('[language]')
    .forEach((element) => element.setAttribute('language', language))
);
