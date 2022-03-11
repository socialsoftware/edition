import React, { useMemo, useState } from 'react';
import ReactTooltip from 'react-tooltip';
import '../../../resources/css/bootstrap-table.min.css';
import Pagination from './Pagination';
import Search from './Search';

const dropEmptyCols = (headers, data) =>
  Object.keys(headers).filter((header) => data?.some((row) => row[header]));

const getTableData = (data, index, numberOfItems) => {
  const start = numberOfItems * index;
  const end = start + numberOfItems;
  return data?.slice(start, end);
};

export default ({
  headers,
  data,
  dataFiltered,
  classes,
  messages,
  setDataFiltered  
}) => {
  const [numberOfItems, setNumberOfItems] = useState(10);
  const [index, setIndex] = useState(0);
  const tableData = useMemo(() => getTableData(dataFiltered, index, numberOfItems))

  return (
    <div className="bootstrap-table">
      <div className="fixed-table-toolbar">
        <Pagination
          numberOfItems={numberOfItems}
          setNumberOfItems={setNumberOfItems}
          index={index}
          setIndex={setIndex}
          length={dataFiltered?.length}
        />

        <Search
          data={data}
          setDataFiltered={setDataFiltered}
          language={messages}
        />
      </div>
      <div className="fixed-table-container">
        <div className="fixed-table-body">
          <table
            id="tableEditions"
            data-pagination="false"
            data-search="true"
            className={classes}
          >
            <thead>
              <tr>
                {dropEmptyCols(headers, dataFiltered).map((header, index) => (
                  <th key={index} className={`expert-column-${header}`}>
                    <ReactTooltip
                      id={`${header}-info`}
                      type="dark"
                      place="bottom"
                      effect="solid"
                      className="edition-table-tooltip"
                      getContent={() => messages?.[`${header}_info`]}
                    />
                    <span data-tip="" data-for={`${header}-info`}>
                      <div className="th-inner">{headers?.[header]}</div>
                    </span>
                    <div className="fht-cell"></div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {tableData?.map(
                (entry, index) => (
                  <tr key={index} data-index={index}>
                    {dropEmptyCols(headers, dataFiltered).map((header, ind) => (
                      <td className={`expert-column-${header}`} key={ind}>
                        {entry[header]}
                      </td>
                    ))}
                  </tr>
                )
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
