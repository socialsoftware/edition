import 'user';
import './Components/Navbar/Navbar.js';
import './Components/HomeView/Home.js';

window.addEventListener('ldod-language', ({ detail: { language } }) =>
  document
    .querySelectorAll('[language]')
    .forEach((element) => element.setAttribute('language', language))
);
