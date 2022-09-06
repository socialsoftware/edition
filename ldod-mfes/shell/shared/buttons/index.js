import './buttons.js';

const exportFrags = document.createElement('ldod-export');
exportFrags.setAttribute('title', 'Export selected Fragments');

exportFrags.setAttribute('file-prefix', 'exported-file');
exportFrags.dataset.url =
  'http://localhost:8000/api/text/admin/export-fragments';
exportFrags.setAttribute('method', 'POST');
(exportFrags.dataset.body = JSON.stringify([
  '1407529502375939',
  '1407529502375938',
])),
  document.querySelector('#root').appendChild(exportFrags);
