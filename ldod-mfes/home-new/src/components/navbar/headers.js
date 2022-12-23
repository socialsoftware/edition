export default ({ text, reading, virtual, search, about, social, user }) => {
  return {
    about: {
      name: 'header_about',
      pages: [
        { id: 'header_title', route: about?.archive?.() },
        { id: 'header_videos', route: about?.videos?.() },
        { id: 'header_tutorials', route: about?.tutorials?.() },
        { id: 'header_faq', route: about?.faq?.() },
        { id: 'header_encoding', route: about?.encoding?.() },
        { id: 'header_bibliography', route: about?.articles?.() },
        { id: 'header_book', route: about?.book?.() },
        { id: 'header_conduct', route: about?.conduct?.() },
        { id: 'header_privacy', route: about?.privacy?.() },
        { id: 'header_team', route: about?.team?.() },
        { id: 'header_acknowledgements', route: about?.ack?.() },
        { id: 'header_contact', route: about?.contact?.() },
        { id: 'header_copyright', route: about?.copyright?.() },
      ],
    },
    reading: {
      name: 'general_reading',
      pages: [
        {
          id: 'general_reading_sequences',
          route: reading?.index?.(),
        },
        {
          id: 'general_reading_visual_external',
          link: `${window.process?.host}/ldod-visual`,
        },
        {
          id: 'general_reading_visual_integrated',
          route: '/visual',
        },
        {
          id: 'general_citations_twitter',
          route: social?.twitterCitations?.(),
        },
      ],
    },

    documents: {
      name: 'header_documents',
      pages: [
        { id: 'authorial_source', route: text?.sources?.() },
        { id: 'fragment_codified', route: text?.fragments?.() },
      ],
    },
    editions: {
      name: 'header_editions',
      pages: [
        {
          id: 'general_editor_prado',
          route: text?.edition?.('JPC'),
        },
        {
          id: 'general_editor_cunha',
          route: text?.edition?.('TSC'),
        },
        {
          id: 'general_editor_zenith',
          route: text?.edition?.('RZ'),
        },
        {
          id: 'general_editor_pizarro',
          route: text?.edition?.('JP'),
        },
        { clazz: 'divider' },
        {
          id: 'header_title',
          route: virtual?.virtualEdition?.('LdoD-Arquivo'),
        },
        { clazz: 'divider' },
        {
          id: 'LdoD-Mallet',
          route: virtual?.virtualEdition?.('LdoD-Mallet'),
        },
        {
          id: 'LdoD-Twitter',
          route: virtual?.virtualEdition?.('LdoD-Twitter'),
        },
      ],
    },
    search: {
      name: 'header_search',
      pages: [
        { id: 'header_search_simple', route: search?.simple?.() },
        { id: 'header_search_advanced', route: search?.advanced?.() },
      ],
    },
    virtual: {
      name: 'header_virtual',
      pages: [
        {
          id: 'header_virtualeditions',
          route: virtual?.virtualEditions?.(),
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
          route: text?.manageFragments?.(),
        },
        {
          id: 'user_management',
          route: user?.manageUsers?.(),
        },
        {
          id: 'virtual_editions_management',
          route: virtual?.manageVirtualEditions?.(),
        },
        {
          id: 'tweets_management',
          route: social?.manageTweets?.(),
        },
      ],
    },
  };
};
