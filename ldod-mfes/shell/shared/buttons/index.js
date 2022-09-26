import './buttons.js';

const exportFrags = document.createElement('ldod-export');
exportFrags.setAttribute('title', 'Export selected Fragments');

exportFrags.setAttribute('file-prefix', 'exported-file');
exportFrags.dataset.url =
  'http://localhost:8000/api/text/admin/export-fragments';
exportFrags.setAttribute('method', 'POST');
(exportFrags.dataset.body = JSON.stringify(['844579548954625'])),
  document.querySelector('#root').appendChild(exportFrags);

const testButton = document.createElement('ldod-button');
testButton.dataset.btnid = 'button-test';
testButton.id = 'testBtn';
testButton.setAttribute('class', 'btn btn-danger');
testButton.setAttribute('type', 'button');
testButton.setAttribute('title', 'Test');
testButton.handlers = {
  click: (e) => console.log(e),
};

document.querySelector('#root').appendChild(testButton);

window.addEventListener('ldod-file-uploaded', ({ detail: { data } }) => {
  const uploadedFrags = data.filter((frag) => frag.uploaded);
  const notUploadedFrags = data.filter((frag) => !frag.uploaded);

  const uploadedFragsResult = uploadedFrags.reduce(
    (accumulated, { xmlId, title, overwritten }) => {
      return `${accumulated}\n[${xmlId}(${title})]${
        overwritten ? ' (overwritten)' : ''
      }`;
    },
    `New uploaded fragments: ${uploadedFrags.length}`
  );

  const notUploadedFragsResult = notUploadedFrags.reduce(
    (accumulated, { xmlId, title, overwritten }) => {
      return `${accumulated}\n[${xmlId}(${title})]${
        overwritten ? ' (overwritten)' : ''
      }`;
    },
    `\nAlready uploaded fragments: ${notUploadedFrags.length}`
  );

  window.dispatchEvent(
    new CustomEvent('ldod-message', {
      detail: {
        message: uploadedFragsResult.concat('\n', notUploadedFragsResult),
      },
    })
  );
});

window.addEventListener('ldod-message', ({ detail }) =>
  console.log(detail.message)
);
