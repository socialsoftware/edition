import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Table from '../../../shared/Table';
import { getEditionTaxonomy } from '../api/edition';
import {
  editionStoreSelector
} from '../editionStore';


export default ({ messages }) => {
  const { acronym } = useParams();
  const data = editionStoreSelector('data');

  useEffect(() => {
     getEditionTaxonomy(acronym);
  }, [messages, acronym]);

  return (
    <div>
      {data?.tableData && (
        <>
          <h3 className="text-center">
            {messages.taxonomy}: {data.title}
          </h3>
          <br />
          <p>
            <strong>{messages.virtualEdition}: </strong>
            {data.subTitle}
          </p>
          <p>
            <strong>
              {data.categorySetSize} {messages.categories}:
            </strong>
          </p>
          <br />
          <Table
          data={data.tableData}
          classes="table table-hover"
          labels={messages.taxonomyTableLabels}
          pagination
          search
          />
        </>
      )}
    </div>
  );
};
