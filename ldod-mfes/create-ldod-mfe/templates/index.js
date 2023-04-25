/** @format */

// code here for index.html
const element = document.body.querySelector('template').content.firstElementChild.cloneNode(true);
element.textContent += ` ${import.meta.env.VITE_MFE_NAME}`;
document.body.querySelector('div#app').appendChild(element);
