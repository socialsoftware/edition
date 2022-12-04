export default {
  fragments: () => '/text/fragments',
  sources: () => '/text/sources',
  editions: () => '/text/edition',
  edition: (acrn) => `/text/edition/acronym/${acrn}`,
  fragment: (xmlId) => `/text/fragment/${xmlId}`,
  fragmentInter: (xmlId, urlId) => `/text/fragment/${xmlId}/inter/${urlId}`,
  manageFragments: () => '/text/manage-fragments',
};
