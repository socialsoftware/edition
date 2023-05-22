/** @format */
import { expect } from '@esm-bundle/chai';
import { mergeHeaders } from '../request-proxy.js';
const URL = 'some url';

const options = {
	headers: new Headers({
		Authorization: 'Bearer asdf',
	}),
};

const RESOURCE = new Request(URL, {});
const RESOURCE_TOKEN = new Request(RESOURCE, options);

it('maintain original token', () => {
	const headers = mergeHeaders(new Request(URL, options));
	expect(headers.get('Authorization')).to.equal('Bearer asdf');
});

it('maintain original token', () => {
	const headers = mergeHeaders(URL);
	expect(headers.get('Authorization')).to.null;
});

it('replace original token', () => {
	const req = new Request(RESOURCE, options);
	expect(req.headers.get('Authorization')).to.equal('Bearer asdf');
});

it('replace original token', () => {
	const req = new Request(RESOURCE_TOKEN);
	expect(req.headers.get('Authorization')).to.equal('Bearer asdf');
});

it('maintain original token', () => {
	const token = RESOURCE_TOKEN.headers.get('Authorization');
	const headers = new Headers({
		Authorization: token || 'test',
	});
	const req = new Request(RESOURCE_TOKEN, { headers });
	expect(req.headers.get('Authorization')).to.equal('Bearer asdf');
});
