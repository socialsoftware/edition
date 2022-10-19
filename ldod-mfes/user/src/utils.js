export function setInvalidFor(input, message) {
  const formFloating = input.parentElement;
  const small = formFloating.querySelector('small');
  formFloating.className = 'form-floating invalid';
  small.innerText = message;
}

export function setValidFor(input) {
  const formFloating = input.parentElement;
  formFloating.className = 'form-floating valid';
}

export function capitalizeFirstLetter(word) {
  return word.slice(0, 1).toUpperCase().concat(word.slice(1, word.length));
}

export const emitMessageEvent = (message, type = 'message') =>
  window.dispatchEvent(
    new CustomEvent(`ldod-${type}`, { detail: { message } })
  );

export const loadConstants = async (lang) =>
  (await import(`./resources/messages-${lang}.js`)).default;

export function eventEmiter(eventName, options = {}, emiter = window) {
  const event = new CustomEvent(eventName, options);
  emiter.dispatchEvent(event);
}
export const logoutEvent = new CustomEvent('ldod-logout', {
  composed: true,
  bubbles: true,
});

export const tokenEvent = (token) =>
  new CustomEvent('ldod-token', {
    detail: { token },
    bubbles: true,
    composed: true,
  });

export const loginEvent = (user) =>
  new CustomEvent('ldod-login', {
    detail: { user },
    bubbles: true,
    composed: true,
  });

export const messageEvent = (message) =>
  new CustomEvent('ldod-message', {
    detail: { message },
    bubbles: true,
    composed: true,
  });
export const errorEvent = (message) =>
  new CustomEvent('ldod-error', {
    detail: { message },
    bubbles: true,
    composed: true,
  });
export const isDev = () => import.meta.env.DEV;
