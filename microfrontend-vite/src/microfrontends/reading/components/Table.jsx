import React ,{ useCallback, useMemo, useState } from 'react';
import '../../../resources/css/bootstrap-table.min.css';
import Pagination from './Pagination';
import Search from './Search';

const getTableData = (data, index, numberOfItems) => {
  const start = numberOfItems * index;
  const end = start + numberOfItems;
  return data?.slice(start, end);
};

export default React.memo(
  ({ headers, data, dataFiltered, classes, messages, setDataFiltered }) => {
    const [numberOfItems, setNumberOfItems] = useState(dataFiltered?.length);
    const [index, setIndex] = useState(0);
    const tableData = useMemo(() =>
      getTableData(dataFiltered, index, numberOfItems)
    );

    const changeNumberOfItems = useCallback((val) => setNumberOfItems(val));
    const changeIndex = useCallback((index) => setIndex(index));

    return (
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Pagination
            numberOfItems={numberOfItems}
            setNumberOfItems={changeNumberOfItems}
            index={index}
            setIndex={changeIndex}
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
            <table id="tablecitations" data-search="true" className={classes}>
              <thead>
                <tr>
                  {Object.keys(headers ?? {}).map((header, index) => (
                    <th key={index} className={`tb-header-${header}`}>
                      <div className="th-inner">{headers[header]}</div>
                      <div className="fht-cell"></div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {tableData?.map((entry, index) => (
                  <tr key={index} data-index={index}>
                    {Object.keys(headers ?? {}).map((header, ind) => (
                      <td className={`tb-data-${header}`} key={ind}>
                        {entry[header]}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }
);
