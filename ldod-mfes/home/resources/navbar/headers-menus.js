import {
  textReferences,
  readingReferences,
  virtualReferences,
  searchReferences,
  aboutReferences,
  socialReferences,
  userReferences,
} from '../../src/externalDeps.js';

export default {
  about: {
    name: 'header_about',
    pages: [
      { id: 'header_title', route: aboutReferences?.archive() },
      { id: 'header_videos', route: aboutReferences?.videos() },
      { id: 'header_tutorials', route: aboutReferences?.tutorials() },
      { id: 'header_faq', route: aboutReferences?.faq() },
      { id: 'header_encoding', route: aboutReferences?.encoding() },
      { id: 'header_bibliography', route: aboutReferences?.articles() },
      { id: 'header_book', route: aboutReferences?.book() },
      { id: 'header_conduct', route: aboutReferences?.conduct() },
      { id: 'header_privacy', route: aboutReferences?.privacy() },
      { id: 'header_team', route: aboutReferences?.team() },
      { id: 'header_acknowledgements', route: aboutReferences?.ack() },
      { id: 'header_contact', route: aboutReferences?.contact() },
      { id: 'header_copyright', route: aboutReferences?.copyright() },
    ],
  },
  reading: {
    name: 'general_reading',
    pages: [
      {
        id: 'general_reading_sequences',
        route: readingReferences?.index(),
      },
      {
        id: 'general_reading_visual_external',
        link: `${process.host}/ldod-visual`,
      },
      {
        id: 'general_reading_visual_integrated',
        route: '/visual',
      },
      {
        id: 'general_citations_twitter',
        route: socialReferences?.twitterCitations(),
      },
    ],
  },

  documents: {
    name: 'header_documents',
    pages: [
      { id: 'authorial_source', route: textReferences?.sources() },
      { id: 'fragment_codified', route: textReferences?.fragments() },
    ],
  },
  editions: {
    name: 'header_editions',
    pages: [
      {
        id: 'general_editor_prado',
        route: textReferences?.edition?.('JPC'),
      },
      {
        id: 'general_editor_cunha',
        route: textReferences?.edition?.('TSC'),
      },
      {
        id: 'general_editor_zenith',
        route: textReferences?.edition?.('RZ'),
      },
      {
        id: 'general_editor_pizarro',
        route: textReferences?.edition?.('JP'),
      },
      { clazz: 'divider' },
      {
        id: 'header_title',
        route: virtualReferences?.virtualEdition?.('LdoD-Arquivo'),
      },
      { clazz: 'divider' },
    ],
  },
  search: {
    name: 'header_search',
    pages: [
      { id: 'header_search_simple', route: searchReferences?.simple() },
      { id: 'header_search_advanced', route: searchReferences?.advanced() },
    ],
  },
  virtual: {
    name: 'header_virtual',
    pages: [
      {
        id: 'header_virtualeditions',
        route: virtualReferences?.virtualEditions(),
      },
      {
        id: 'general_classificationGame',
        route: '/virtual/classificationGames',
      },
    ],
  },
  admin: {
    name: 'header_admin',
    pages: [
      {
        id: 'fragments_management',
        route: textReferences?.manageFragments(),
      },
      {
        id: 'user_management',
        route: userReferences?.manageUsers(),
      },
      {
        id: 'virtual_editions_management',
        route: virtualReferences?.manageVirtualEditions(),
      },
      {
        id: 'tweets_management',
        route: socialReferences?.manageTweets(),
      },
    ],
  },
};
