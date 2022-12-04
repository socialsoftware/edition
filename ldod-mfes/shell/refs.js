
if (typeof window !== "undefined") {
  window.references = {
    search: {
      simple: () => "/search/simple",
      advanced: () => "/search/advanced"
    },
    about: {
      archive: () => "/about/archive",
      videos: () => "/about/videos",
      tutorials: () => "/about/tutorials",
      faq: () => "/about/faq",
      encoding: () => "/about/encoding",
      articles: () => "/about/articles",
      book: () => "/about/book",
      privacy: () => "/about/privacy",
      team: () => "/about/team",
      ack: () => "/about/acknowledgements",
      contact: () => "/about/contact",
      conduct: () => "/about/conduct",
      copyright: () => "/about/copyright"
    }
  }
}