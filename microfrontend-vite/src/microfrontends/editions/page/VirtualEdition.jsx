import { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getVirtualEdition } from '../api/edition';
import Table from '../../../shared/Table';
import { editionStoreSelector } from '../editionStore';
import { isVirtualEdition } from '../Models/VirtualEdition';

export default ({ messages }) => {
  const { acronym } = useParams();
  const data = editionStoreSelector('data');

  useEffect(() => {
    getVirtualEdition(acronym);
  }, [messages]);

  return (
    <div>
      {data?.tableData && isVirtualEdition(data) && (
        <>
          <h3 className="text-center">
            {messages.virtualEdition}: {data.title}
          </h3>
          <br />
          <p>
            <strong>{messages.editors}: </strong>
            {data.editors}
          </p>
          <p>
            <strong>{messages.synopse}: </strong>
            {data.synopsis}
          </p>
          <p>
            <strong>{messages.taxonomy}: </strong>
            <Link to={`/edition/${acronym}/taxonomy`}>
              {messages['edition_of']} {data.title}
            </Link>
          </p>
          <p>
            <strong>
              {data.interpsSize} {messages.fragments} :
            </strong>
          </p>
          <Table
            data={data?.tableData}
            classes="table table-hover"
            labels={messages.virtualTableLabels}
            pagination
            search
          />
        </>
      )}
    </div>
  );
};
