import HTMLReactParser from 'html-react-parser';
import { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getVirtualEdition } from '../api/edition';
import Search from '../components/Search';
import Table from '../components/Table';
import {
  editionStoreSelector,
  getAcronym,
  setAcronym,
  setDataFiltered,
} from '../editionStore';
import { isVirtualEdition } from '../Models/VirtualEdition';

const getTitle = (messages, edition) =>
  `${messages['edition_of']} ${edition.title} `;

const getFullName = (edition) => `${edition?.firstName} ${edition?.lastName}`;

export default ({ messages }) => {
  const { username } = useParams();
  const edition = editionStoreSelector('edition');
  const dataFiltered = editionStoreSelector('dataFiltered');

  useEffect(() => {
    setAcronym(acronym);
  }, [acronym]);

  return (
    <div>

    </div>
  );
};
