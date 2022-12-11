import textReferences from '@src/references';
import { virtualEdition } from '../../../external-deps';
export const editions = [
  {
    filename: 'JPC',
    path: textReferences.edition('JPC'),
    padding: '5px',
  },
  {
    filename: 'TSC',
    path: textReferences.edition('TSC'),
    padding: '5px',
  },
  {
    filename: 'RZ',
    path: textReferences.edition('RZ'),
    padding: '5px',
  },
  {
    filename: 'JP',
    path: textReferences.edition('JP'),
    padding: '10px',
  },
  {
    filename: 'ALdod',
    path: virtualEdition('LdoD-Arquivo'),
    padding: '5px',
  },
];
export default {
  headers: [
    'number',
    'title',
    'reading',
    'volume',
    'page',
    'date',
    'heteronym',
  ],
  pt: {
    editions: 'Edições',
    edition: ({ editor, value }) => `Edição ${editor} (${value})`,
    number: 'Número',
    title: 'Título',
    reading: 'Leitura',
    volume: 'Volume',
    page: 'Página',
    date: 'Data',
    heteronym: 'Heterónimo',
    notAssigned: 'Não atribuído',
  },
  en: {
    editions: 'Editions',
    edition: ({ editor, value }) => `Edition ${editor} (${value})`,
    number: 'Number',
    title: 'Title',
    reading: 'Reading',
    volume: 'Volume',
    page: 'Page',
    date: 'Date',
    heteronym: 'Heteronym',
    notAssigned: 'Not assigned',
  },
  es: {
    editions: 'Ediciones',
    edition: ({ editor, value }) => `Edicione ${editor} (${value})`,
    number: 'Número',
    title: 'Título',
    reading: 'Lectura',
    volume: 'Volume',
    page: 'Página',
    date: 'Fecha',
    heteronym: 'Heterónimo',
    notAssigned: 'No assignado',
  },
};
