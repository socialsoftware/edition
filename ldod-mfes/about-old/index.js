import api from './src/about.js';
import 'shared/public/router.js';

(() => {
  document.querySelectorAll('.but-lang').forEach((button) =>
    button.addEventListener('click', () =>
      button.dispatchEvent(
        new CustomEvent('ldod-language', {
          detail: { language: button.id },
          composed: true,
          bubbles: true,
        })
      )
    )
  );

  const unmountButton = document.querySelector('#unmount');
  unmountButton.onclick = async () => {
    window.dispatchEvent(
      new CustomEvent('ldod-url-changed', { detail: { path: '/about' } })
    );
  };
})();

await api.mount('en', '#root');
