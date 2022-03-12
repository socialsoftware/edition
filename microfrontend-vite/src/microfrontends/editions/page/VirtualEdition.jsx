import parserHTML from 'html-react-parser';
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
  const { acronym } = useParams();
  const edition = editionStoreSelector('edition');
  const dataFiltered = editionStoreSelector('dataFiltered');

  useEffect(() => {
    acronym !== getAcronym() && getVirtualEdition(acronym);
    setAcronym(acronym);
  }, [acronym]);

  useEffect(() => {
    console.log(edition);
  },[edition])

  return (
    <div>
      {edition && isVirtualEdition(edition) && (
        <>
          <h3 className="text-center">
            {messages.virtualEdition}: {edition.title}
          </h3>
          <br />
          <p>
            <strong>{messages.editors}: </strong>
            {edition.editors}
          </p>
          <p>
            <strong>{messages.synopse}: </strong>
            {parserHTML(edition.synopsis ?? '')}
          </p>
          <p>
            <strong>{messages.taxonomy}: </strong>
            <Link to={`/edition/${acronym}/taxonomy`}>
              {getTitle(messages, edition)}
            </Link>
          </p>
          <p>
            <strong>
              {edition.interpsSize} {messages.fragments} :
            </strong>
          </p>
          <Table
            data={edition?.tableData}
            setDataFiltered={setDataFiltered}
            dataFiltered={dataFiltered ?? edition?.tableData}
            classes="table table-hover"
            messages={messages}
            headers={messages.virtualTableHeaders}
          />
        </>
      )}
    </div>
  );
};
