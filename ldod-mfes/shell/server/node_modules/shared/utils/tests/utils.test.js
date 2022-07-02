/**
 * @jest-environment jsdom
 */
import { parseHTML, ldodRender, dom } from '../src/utils.js';

const html = String.raw;

describe('Node creation with parseHTML', () => {
  it('create a div element and is returned as div node', () => {
    const div = parseHTML(html`<div>Test</div>`);
    expect(div instanceof HTMLDivElement).toBeTruthy();
    expect(div.textContent).toBe('Test');
  });

  it('create a link element and is returned as link node', () => {
    const link = parseHTML(
      html`<link rel="stylesheet" href="style.css" type="text/css" />`
    );
    expect(link instanceof HTMLLinkElement).toBeTruthy();
    expect(link.rel).toBe('stylesheet');
    expect(link.href).toBe('style.css');
    expect(link.type).toBe('text/css');
  });
});

describe('Node creation with dom', () => {
  it('create a div element and is returned as div node', () => {
    const div = dom(html`<div>Test</div>`);

    document.body.appendChild(div);
    const uut = document.body.firstChild;
    expect(uut instanceof HTMLDivElement).toBeTruthy();
    expect(uut.textContent).toBe('Test');
  });

  it('create and append a link element', () => {
    const link = dom(
      html`<link rel="stylesheet" href="style.css" type="text/css" />`
    );

    document.head.appendChild(link);
    const uut = document.head.firstChild;
    expect(uut instanceof HTMLLinkElement).toBeTruthy();
    expect(uut.rel).toBe('stylesheet');
    expect(uut.href).toBe('http://localhost/style.css');
    expect(uut.type).toBe('text/css');
  });
});

describe('rendering nodes on document', () => {
  const valid = document.createElement('div');
  valid.innerHTML = 'Valid';
  valid.id = 'valid-div';

  const invalid = String.raw`<div id="invalid-div">Invalid</div>`;

  it('render a valid node on the document', () => {
    ldodRender(valid, document.body);
    const result = document.getElementById('valid-div');
    expect(result).toBeInstanceOf(Node);
    expect(result.textContent).toBe('Valid');
  });

  it('render a invalid node on the document throws an exception', () => {
    expect(() => ldodRender(invalid, document.body)).toThrow(TypeError);
  });
});
