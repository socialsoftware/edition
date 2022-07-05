import { useEffect } from 'react';

const filterTable = (data, searchString) =>
  data?.filter((item) =>
    item?.searchData.toLowerCase()?.includes(searchString?.toLowerCase().trim())
  );

export default ({ data, setDataFiltered, searchString, setSearchString }) => {

  useEffect(() => {
    if (searchString) {
      setDataFiltered(() => {
        const result = filterTable(data, searchString);
        return data.length === result.length ? data : result;
      });
    } else setDataFiltered(data)
  }, [searchString]);

  return (
    <>
      <div className="pull-right search">
        <input
          className="form-control"
          type="text"
          value={searchString}
          placeholder="Search"
          onChange={(e) => setSearchString(e.target.value)}
        />
      </div>
    </>
  );
};
