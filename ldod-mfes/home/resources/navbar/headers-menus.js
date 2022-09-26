import { isMFEAvailable } from '../../src/utils.js';

export default {
  about: isMFEAvailable('about') && {
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
  reading: (isMFEAvailable('social') ||
    isMFEAvailable('reading') ||
    isMFEAvailable('ldod-visual')) && {
    name: 'general_reading',
    pages: [
      isMFEAvailable('reading') && {
        id: 'general_reading_sequences',
        route: '/reading',
      },
      isMFEAvailable('ldod-visual') && {
        id: 'general_reading_visual',
        link: `${process.host}/ldod-visual`,
      },
      isMFEAvailable('social') && {
        id: 'general_citations_twitter',
        route: '/social/twitter-citations',
      },
    ],
  },
  documents: isMFEAvailable('text') && {
    name: 'header_documents',
    pages: [
      { id: 'authorial_source', route: '/text/sources' },
      { id: 'fragment_codified', route: '/text/fragments' },
    ],
  },
  editions: {
    name: 'header_editions',
    pages: [
      isMFEAvailable('text') && {
        id: 'general_editor_prado',
        route: '/text/edition/acronym/JPC',
      },
      isMFEAvailable('text') && {
        id: 'general_editor_cunha',
        route: '/text/edition/acronym/TSC',
      },
      isMFEAvailable('text') && {
        id: 'general_editor_zenith',
        route: '/text/edition/acronym/RZ',
      },
      isMFEAvailable('text') && {
        id: 'general_editor_pizarro',
        route: '/text/edition/acronym/JP',
      },
      isMFEAvailable('virtual') && { clazz: 'divider' },
      isMFEAvailable('virtual') && {
        id: 'header_title',
        route: '/edition/acronym/LdoD-Arquivo',
      },
      isMFEAvailable('virtual') && { clazz: 'divider' },
    ],
  },
  search: isMFEAvailable('search') && {
    name: 'header_search',
    pages: [
      { id: 'header_search_simple', route: '/search/simple' },
      { id: 'header_search_advanced', route: '/search/advanced' },
    ],
  },
  virtual: (isMFEAvailable('virtual') ||
    isMFEAvailable('classification-game')) && {
    name: 'header_virtual',
    pages: [
      isMFEAvailable('virtual') && {
        id: 'header_virtualeditions',
        route: '/virtual/virtualeditions',
      },
      isMFEAvailable('classification-game') && {
        id: 'general_classificationGame',
        route: '/virtual/classificationGames',
      },
    ],
  },
  admin: {
    name: 'header_admin',
    pages: [
      isMFEAvailable('text') && {
        id: 'fragments_management',
        route: '/text/manage-fragments',
      },
      isMFEAvailable('user') && {
        id: 'user_management',
        route: '/user/manage-users',
      },
      isMFEAvailable('virtual') && {
        id: 'virtual_editions_management',
        route: '/virtual/manage-virutal-editions',
      },
      isMFEAvailable('social') && {
        id: 'tweets_management',
        route: '/social/manage-tweets',
      },
    ],
  },
};
