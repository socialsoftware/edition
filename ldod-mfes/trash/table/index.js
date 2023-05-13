import './table.js';
const root = document.getElementById('container');
const table = document.createElement('ldod-table');
table.headers = ['col1', 'col2', 'col3', 'col4'];
table.dataset.searchkey = 'index';
table.data = [...Array(100).keys()].map((num, index) => ({
  index,
  col1: num,
  col2: num,
  col3: num,
  col4: num,
  search: num,
}));
root.appendChild(table);

root.addEventListener('ldod-table-searched', ({ target }) => {
  console.log(target.allRows);
  console.log(target.searchedRows);
  console.log(target.unSearchedRows);
});
