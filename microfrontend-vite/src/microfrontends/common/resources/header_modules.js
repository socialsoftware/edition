export const navHeaders = {
  about: {
    name: 'header_about',
    pages: [
      { id: 'header_title', route: '/about/archive' },
      { id: 'header_videos', route: '/about/videos' },
      { id: 'header_tutorials', route: '/about/tutorials' },
      { id: 'header_faq', route: '/about/faq' },
      { id: 'header_encoding', route: '/about/encoding' },
      { id: 'header_bibliography', route: '/about/articles' },
      { id: 'header_book', route: '/about/book' },
      { id: 'header_conduct', route: '/about/conduct' },
      { id: 'header_privacy', route: '/about/privacy' },
      { id: 'header_team', route: '/about/team' },
      { id: 'header_acknowledgements', route: '/about/acknowledgements' },
      { id: 'header_contact', route: '/about/contact' },
      { id: 'header_copyright', route: '/about/copyright' },
    ],
  },
  reading: {
    name: 'general_reading',
    pages: [
      { id: 'general_reading_sequences', route: '/reading' },
      { id: 'general_reading_visual', link: 'https://ldod.uc.pt/ldod-visual' },
      { id: 'general_citations_twitter', route: '/reading/citations' },
    ],
  },
  documents: {
    name: 'header_documents',
    pages: [
      { id: 'authorial_source', route: '/documents/source/list' },
      { id: 'fragment_codified', route: '/documents/fragments' },
    ],
  },
  editions: {
    name: 'header_editions',
    pages: [],
  },
  search: {
    name: 'header_search',
    pages: [
      { id: 'header_search_simple', route: '/search/simple' },
      { id: 'header_search_advanced', route: '/search/advanced' },
    ],
  },
  virtual: {
    name: 'header_virtual',
    pages: [
      { id: 'header_virtualeditions', route: '/virtual/virtualeditions' },
      {
        id: 'general_classificationGame',
        route: '/virtual/classificationGames',
      },
    ],
  },
  admin: {
    name: 'header_admin',
    pages: [
      { id: 'load', route: '/admin/loadForm' },
      { id: 'general_export', route: '/admin/exportForm' },
      { id: 'fragment_delete', route: '/admin/fragment/list' },
      { id: 'user_manage', route: '/admin/user/list' },
      { id: 'virtual_editions_manage', route: '/admin/virtual/list' },
      { id: 'twitter_manage', route: '/admin/tweets' },
    ],
  },
};