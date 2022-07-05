const getNavLabel = (index, numberOfItems, length) => {
  const start = index * numberOfItems + 1;
  let end = numberOfItems * (index + 1);
  end = end > length ? length : end;
  return length ? `${start} - ${end} of ${length} items` : 0;
};

export default ({
  index,
  setIndex,
  setNumberOfItems,
  numberOfItems,
  length,
}) => {
  const handleSelect = (e) => {
    setIndex(parseInt((index * numberOfItems) / e.target.value));
    setNumberOfItems(+e.target.value);
  };

  const changeIndex = (val) => {
    val === 'FIRST' && setIndex(0);
    val === 'LAST' && setIndex(parseInt(length / numberOfItems));
    typeof val === 'number' && setIndex((index) => index + val);
  };

  return (
    <div className="pagination">
      <label htmlFor="pagination" style={{ marginRight: '5px' }}>
        Show
      </label>
      <select
        name="pagination"
        className="pagination-select"
        value={numberOfItems}
        onChange={handleSelect}
      >
        {[10, 25, 50, 100]
          .filter((option) => option < length)
          .map((option, index) => (
            <option key={index} value={option} className="option-select" >
              {option === length ? 'All' : option}
            </option>
          ))}
        <option value={length} className="option-select">
          All
        </option>
      </select>

      <div className="btn-group pagination-button" role="group" aria-label="">
        <button
          type="button"
          className="btn btn-default "
          aria-label="Left Align"
          id="first"
          onClick={() => changeIndex('FIRST')}
          disabled={index === 0 ? true : false}
        >
          <span
            className="glyphicon glyphicon-backward icon-align"
            aria-hidden="true"
          ></span>
        </button>
        <button
          type="button"
          className="btn btn-default"
          onClick={() => changeIndex(-1)}
          disabled={index === 0 ? true : false}
        >
          <span
            className="glyphicon glyphicon-chevron-left icon-align"
            aria-hidden="true"
          ></span>
        </button>
        <button
          disabled
          type="button"
          className="btn btn-default"
          style={{ width: '150px' }}
        >
         <div className="center-button-label">{getNavLabel(index, numberOfItems, length)}</div>
        </button>
        <button
          type="button"
          className="btn btn-default"
          onClick={() => changeIndex(+1)}
          disabled={index >= length / numberOfItems - 1 && true}
        >
          <span  className="glyphicon glyphicon-chevron-right icon-align"></span>
        </button>
        <button
          type="button"
          className="btn btn-default"
          onClick={() => changeIndex('LAST')}
          disabled={index >= length / numberOfItems - 1 && true}
        >
          <span className="glyphicon glyphicon-forward icon-align"></span>
        </button>
      </div>
    </div>
  );
};
