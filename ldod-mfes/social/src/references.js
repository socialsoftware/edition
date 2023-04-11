/** @format */

const references = {
	twitterCitations: () => '/social/twitter-citations',
	manageTweets: () => '/social/manage-tweets',
};

const twitterCitationsHeader = {
	name: 'reading',
	data: {
		name: 'reading',
		pages: [{ id: 'twitter_citations', route: references.twitterCitations() }],
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

const tweetsManagement = {
	name: 'admin',
	data: {
		name: 'admin',
		pages: [{ id: 'tweets_management', route: references.manageTweets() }],
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

if (typeof window !== 'undefined') {
	import('@shared/ldod-events.js').then(({ ldodEventBus }) => {
		customElements.whenDefined('nav-bar').then(() => {
			ldodEventBus.publish('ldod:header', twitterCitationsHeader);
			ldodEventBus.publish('ldod:header', tweetsManagement);
		});
	});
}

export default references;
