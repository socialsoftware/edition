/** @format */

import { html, fixture, expect } from '@open-wc/testing';
import '../src/pages/new-mfe';

describe('NewMfe', () => {
	it('renders hello world', async () => {
		const el = await fixture(html` <new-mfe></new-mfe> `);

		expect(el.shadowRoot.textContent).to.include('Hey there');
	});

	it('passes the a11y audit', async () => {
		const el = await fixture(html` <new-mfe-lit></new-mfe-lit> `);

		await expect(el).shadowDom.to.be.accessible();
	});
});
