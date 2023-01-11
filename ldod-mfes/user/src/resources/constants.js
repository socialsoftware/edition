const ALPHABETIC_REGEX = /^[-'a-zA-ZÀ-ÖØ-öø-ÿ ]+$/;
const ALPHANUMERIC_REGEX = /^[0-9a-zA-Z]+$/;
const PASSWORD_REGEX = /[0-9a-zA-Z]{6,}/;
const EMAIL_REGEX = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
const G_CLIENT_ID = {
	client_id: '180526053205-km4c6tgpnh3j7orrmh0sgm81bibnpl1c.apps.googleusercontent.com',
};
const FB_APP_ID = '723622308749071';
export { ALPHABETIC_REGEX, ALPHANUMERIC_REGEX, PASSWORD_REGEX, EMAIL_REGEX, G_CLIENT_ID, FB_APP_ID };
