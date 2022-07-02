import NavTo from '../src/NavTo.js';
import { Window } from 'happy-dom';

describe('nav-to component testing on DOM', () => {
  let result;
  let window;
  let document;
  cb = jest.fn((e) => (result = e.detail));

  beforeEach(() => {
    window = new Window();
    window.customElements.define('nav-to', NavTo);
    document = window.document;
    document.addEventListener('ldod-url-changed', cb);
    const navTo = document.createElement('nav-to');
    navTo.setAttribute('to', 'path');
    document.body.appendChild(navTo);
  });

  it('adding and clicking on nav-to a ldod-url-changed is fired up', async () => {
    const uut = document.querySelector('nav-to');
    uut.dispatchEvent(new Event('click'));
    expect(uut).toBeDefined();
    expect(uut.getAttribute('to')).toBe('path');
    expect(cb).toHaveBeenCalled();
    expect(result.path).toBe('path');
  });
});
