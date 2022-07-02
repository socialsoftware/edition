/**
 * @jest-environment jsdom
 */
import { UserComponent } from '../build/UserComponent.js';
import { parseHTML } from 'shared/utils.js';

describe('UserComponent no auth created and registered in JSDOM', () => {
  afterEach(() => document.body.childNodes.forEach((node) => node.remove()));

  it('NoAuth UserComponent is rendered', () => {
    const userComponentNoAuth = parseHTML(
      String.raw`<li id="user-component" is="user-component" token="false" master language="en"></li>`
    );
    document.body.append(userComponentNoAuth);
    const result = document.querySelector('li#user-component');
    const anchor = result.childNodes[0];

    expect(result.childNodes).toHaveLength(1);
    expect(anchor).toBeInstanceOf(HTMLAnchorElement);
    expect(result).toBeInstanceOf(UserComponent);
    expect(result.getAttribute('token')).toBe('false');
    expect(result.getAttribute('master')).toBeDefined();
    expect(result.getAttribute('language')).toBe('en');
    expect(result.textContent).toBe('Login');
  });

  it('Auth UserComponent is rendered', () => {
    const userComponentAuth = parseHTML(
      String.raw`<li id="user-component" is="user-component" token="true" master language="en"></li>`
    );
    document.body.append(userComponentAuth);
    const result = document.querySelector('li#user-component');

    const anchor = result.childNodes[0];
    const drop = result.childNodes[1];

    expect(result).toBeInstanceOf(UserComponent);
    expect(result.childNodes).toHaveLength(2);
    expect(result.getAttribute('token')).toEqual('true');
    expect(result.getAttribute('master')).toBeDefined();
    expect(result.getAttribute('language')).toBe('en');
    expect(anchor).toBeInstanceOf(HTMLAnchorElement);
    expect(anchor.textContent).toBe('João Raimundo');
    expect(drop).toBeInstanceOf(HTMLUListElement);
    expect(drop.childNodes).toHaveLength(2);
  });
});
