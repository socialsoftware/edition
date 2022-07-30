export function setInvalidFor(input, message) {
  const formControl = input.parentElement;
  const small = formControl.querySelector('small');
  formControl.className = 'form-control invalid';
  small.innerText = message;
}

export function setValidFor(input) {
  const formControl = input.parentElement;
  formControl.className = 'form-control valid';
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

export const isDev = () => import.meta.env.DEV;
