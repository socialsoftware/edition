export default (frags, inters) => ({
  headers: ['fragments', 'interpretations'],
  pt: {
    searchSimple: 'Pesquisa Simples',
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
    searchSimple: 'Simple Search',
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
    searchSimple: 'Búsqueda Sencilla',
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
