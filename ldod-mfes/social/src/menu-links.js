/** @format */

import references from './references';

const twitterCitationMenuLink = {
	name: 'reading',
	data: {
		name: 'reading',
		links: [{ key: 'twitter_citations', route: references.twitterCitations() }],
	},
	constants: {
		pt: {
			twitter_citations: 'Citações no Twitter',
		},
		en: {
			twitter_citations: 'Citations on Twitter',
		},
		es: {
			twitter_citations: 'Citas en Twitter',
		},
	},
};

const tweetsManagementMenuLink = {
	name: 'admin',
	hidden: true,
	data: {
		name: 'admin',
		links: [{ key: 'tweets_management', route: references.manageTweets() }],
	},
	constants: {
		pt: {
			tweets_management: 'Gerir Tweets',
		},
		en: {
			tweets_management: 'Manage Tweets',
		},
		es: {
			tweets_management: 'Administrar Tweets',
		},
	},
};

function updateMenus() {
	eventBus.publish('ldod:header', tweetsManagementMenuLink);
	eventBus.publish('ldod:header', twitterCitationMenuLink);
}

customElements.whenDefined('nav-bar').then(updateMenus);
