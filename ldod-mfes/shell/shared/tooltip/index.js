import './tooltip';

const tooltip = document.body.querySelector('ldod-dynamic-tooltip#test');

Array.from(['1', '2', '3', '4', '5', '6', '7']).forEach((index) => {
  const div = document.createElement('div');
  div.id = `para-${index}`;
  div.classList.add('test-div');
  div.innerHTML = index;
  div.style.width = '100px';
  document.body.querySelector('div#root').appendChild(div);

  div.addEventListener('pointerenter', () => {
    tooltip.dataset.content = 'test' + index;
    tooltip.setAttribute('placement', 'bottom');
    tooltip.setAttribute('ref', `div#${div.id}`);
  });
});
