import React, { useEffect, useState } from 'react';
import ReactTooltip from 'react-tooltip';
import '../resources/css/bootstrap-table.min.css';
import Loading from './Loading';
import Pagination from './Pagination';
import Search from './Search';

const getTableData = (data, index, numberOfItems) => {
  const start = numberOfItems * index;
  const end = start + numberOfItems;
  return data?.slice(start, end);
};
export default React.memo(
  ({
    data,
    classes,
    labels,
    pagination,
    search,
    setLength = () => {},
    itemsPerPage,
  }) => {
    const [mounted, setMounted] = useState();
    const [numberOfItems, setNumberOfItems] = useState(
      itemsPerPage ?? data?.length
    );
    const [dataFiltered, setDataFiltered] = useState(data);
    const [searchString, setSearchString] = useState('');
    const [index, setIndex] = useState(0);

    const tableData = () => getTableData(dataFiltered, index, numberOfItems);
    const headers = Object.keys(data?.[0] ?? {}).filter(
      (header) => header !== 'searchData'
    );

    useEffect(() => {
      setMounted(false);
      setDataFiltered(data);
      setSearchString('');
    }, [data, labels]);

    useEffect(() => {
      setIndex(0);
      setNumberOfItems(itemsPerPage ?? dataFiltered?.length);
      setLength(dataFiltered?.length);
      setMounted(true);
    }, [dataFiltered]);

    return (
      <>
        {!mounted ? (
          <Loading />
        ) : (
          <div className="bootstrap-table">
            <div className="fixed-table-toolbar">

              {search && (
                <Search
                  data={data}
                  setDataFiltered={setDataFiltered}
                  searchString={searchString}
                  setSearchString={setSearchString}
                />
              )}              {pagination && (
                <Pagination
                  numberOfItems={numberOfItems}
                  setNumberOfItems={setNumberOfItems}
                  index={index}
                  setIndex={setIndex}
                  length={dataFiltered?.length}
                />
              )}
            </div>
            <div className="fixed-table-container">
              <div className="fixed-table-body">
                <table
                  id="tableEditions"
                  data-search="true"
                  className={classes}
                >
                  <thead>
                    <tr>
                      {headers.map((header, index) => (
                        <th key={index} className={`expert-column-${header}`}>
                          <div className="th-inner">
                            <ReactTooltip
                              id={`${header}-info`}
                              type="dark"
                              place="bottom"
                              effect="solid"
                              className="edition-table-tooltip"
                              getContent={() => labels[`${header}_info`]}
                            />
                            <span data-tip="" data-for={`${header}-info`}>
                              {labels[header]}
                            </span>
                          </div>
                          <div className="fht-cell"></div>
                        </th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {tableData()?.map((entry, index) => (
                      <tr key={index} data-index={index}>
                        {headers.map((header, ind) => (
                          <td className={`expert-column-${header}`} key={ind}>
                            {entry?.[header]}
                          </td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        )}
      </>
    );
  }
);
