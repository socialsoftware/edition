/** @format */

import toolsStyle from './tools.css?inline';
import tableStyle from './table.css?inline';

export function tableComponent(root) {
	const div = document.createElement('div');
	div.innerHTML = /*html */ `
    <style>${tableStyle}</style>
    <div id="table-tools">${root.searchKey ? searchTool() : ''}</div>
    <div class="table-container">
      <div class="table-body">
        <table id=${root.id} class="${root.classes}">
          <thead></thead>
          <tbody></tbody>
        </table>
      </div>
    </div> 
  `;

	div.querySelector('thead').appendChild(tableHeaders(root.headers, root.getConstants));
	div.querySelector('tbody').append(...tableBody(root));

	return div.childNodes;
}

function searchTool() {
	return /*html*/ `
      <style>${toolsStyle}</style>
      <input
        id="table-search-field"
        type="search"
        name="search"
        placeholder="search"
      />
  `;
}

function tableHeaders(headers, constants) {
	const tr = document.createElement('tr');
	tr.append(
		...headers.map(key => {
			const th = document.createElement('th');
			th.classList.add('th-inner');
			th.setAttribute('data-key', key);
			return appender(th, constants(key));
		})
	);
	return tr;
}

function tableBody(root) {
	if (!root.data.length) return '';
	return tableRows(root, 0, root.interval);
}

export function tableRows(root, start, end) {
	return root.data.slice(start, end).map(row => {
		root.lastIndex += 1;
		return getRow(row, root);
	});
}

export function getRow(row, root) {
	const entry = row.data && typeof row.data === 'function' ? row.data() : row;
	const tr = document.createElement('tr');
	tr.toggleAttribute('searched', true);
	tr.setAttribute('id', row[root.searchKey] || '');
	tr.append(...root.headers.map(key => rowCell(entry[key])));
	return tr;
}

function rowCell(cellData) {
	const cell = document.createElement('td');
	let inner = typeof cellData === 'function' ? cellData() : cellData;
	return appender(cell, inner);
}

function appender(parent, child) {
	if (child instanceof Node) parent.appendChild(child);
	else if (Symbol.iterator in Object(child)) parent.append(...child);
	else parent.textContent = child || '';
	parent.normalize();
	return parent;
}
