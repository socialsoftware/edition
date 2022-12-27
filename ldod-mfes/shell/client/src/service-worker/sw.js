async function registerServiceWorker() {
  await import('./sw-register.js');
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker
      .register('./sw-register.js')
      .then((registration) => {});
  }
}

registerServiceWorker();
