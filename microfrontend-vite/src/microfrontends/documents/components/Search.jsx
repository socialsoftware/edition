import { useEffect, useState } from 'react';

const filterTable = (data, searchString) =>
  data?.filter((item) =>
    item?.searchData.toLowerCase().includes(searchString?.toLowerCase().trim())
  );

export default ({ data, setDataFiltered, language }) => {
  const [searchString, setSearchString] = useState();
  const [result, setResult] = useState();
  const onSearch = (term) => {
    setSearchString(term);
    searchString?.length < 1
      ? setDataFiltered(data)
      : setResult(filterTable(data, term));
  };

  useEffect(() => {
    setDataFiltered();
    setSearchString('');
  }, [language]);

  useEffect(() => {
    setDataFiltered(result);
  }, [result?.length]);

  return (
    <>
      <div className="pull-right search">
        <input
          className="form-control"
          type="text"
          placeholder="Search"
          value={searchString}
          onChange={(e) => onSearch(e.target.value)}
        />
      </div>
    </>
  );
};
