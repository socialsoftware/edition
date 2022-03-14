import { useEffect, useState } from 'react';

const filterTable = (data, searchString) =>
  data?.filter((item) =>
    item?.searchData.toLowerCase()?.includes(searchString?.toLowerCase().trim())
  );

export default ({ data, setDataFiltered, language }) => {
  const [searchString, setSearchString] = useState('');
  const [result, setResult] = useState();
  const onSearch = (term) => {
    setSearchString(term);
    setResult(filterTable(data, term));
  };

  useEffect(() => {
    setResult();
    setDataFiltered();
    setSearchString('');
  }, [language, data]);

  useEffect(() => {
    if (result?.length === data?.length) setDataFiltered();
    else setDataFiltered(result);
  }, [result?.length]);

  return (
    <>
      <div className="pull-right search">
        <input
          className="form-control"
          type="text"
          value={searchString}
          placeholder="Search"
          onChange={(e) => onSearch(e.target.value)}
        />
      </div>
    </>
  );
};
