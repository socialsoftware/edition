export default (frags, inters) => ({
  headers: ['fragments', 'interpretations'],
  jpc: 'Jacinto Prado Coelho',
  tsc: 'Teresa Sobral Cunha',
  rz: 'Richard Zenith',
  jp: 'Jerónimo Pizarro',
  pt: {
    all: 'Todas',
    witnesses: 'Testemunhos',
    searchSimple: 'Pesquisa Simples',
    searchFor: 'Pesquisar por...',
    searchType: 'Tipo de pesquisa',
    searchSource: 'Tipos de fonte',
    titleSearch: 'Pesquisa por título',
    completeSearch: 'Pesquisa completa',
    fragment: 'Fragmento',
    fragments: (
      <div>
        <span data-search-key="fragment">Fragmento </span>
        {` (${frags})`}
      </div>
    ),
    interpretation: 'Interpretações',
    interpretations: (
      <div>
        <span data-search-key="interpretation">Interpretações</span>
        {` (${inters})`}
      </div>
    ),
  },
  en: {
    all: 'All',
    witnesses: 'Witnesses',
    searchSimple: 'Simple Search',
    searchFor: 'Search for...',
    searchType: 'Search type',
    searchSource: 'Source types',
    titleSearch: 'Search by title',
    completeSearch: 'Full search',
    fragment: 'Fragment',
    fragments: (
      <div>
        <span data-search-key="fragment">Fragment</span>
        {` (${frags})`}
      </div>
    ),
    interpretation: 'Interpretations',
    interpretations: (
      <div>
        <span data-search-key="interpretation">Interpretations</span>
        {` (${inters})`}
      </div>
    ),
  },
  es: {
    all: 'Todas',
    witnesses: 'Testimonios',
    searchSimple: 'Búsqueda Sencilla',
    searchType: 'Tipo de búsqueda',
    searchSource: 'Tipos de fuente',
    searchFor: 'Busca por...',
    titleSearch: 'Búsqueda por título',
    completeSearch: 'Búsqueda completa',
    fragment: 'Fragmento',
    fragments: (
      <div>
        <span data-search-key="fragment">Fragmento</span>
        {` (${frags})`}
      </div>
    ),
    interpretation: 'Interpretaciones',
    interpretations: (
      <div>
        <span data-search-key="interpretations">Interpretaciones</span>
        {` (${inters})`}
      </div>
    ),
  },
});
