export default (menuName, items) => {
  const res = /*html*/ `<a class="dropdown-toggle" data-toggle="dropdown"
    >${menuName}<span class="caret"></span>
  </a>
  <ul class="dropdown-menu">
    <div class="dropdown-menu-bg"></div>
    ${
      items?.reduce((prev, { id, route, link, name, clazz, selected }) => {
        return prev.concat(/*html*/ `
        <li
          ${clazz ? `class=${clazz}` : ''}
          ${selected ? 'default-selected' : ''}
        >
          ${
            route
              ? `<a is="nav-to" to=${route} id=${id ?? ''}
                >${name ?? ''}</a
              >`
              : ''
          }
          ${
            link
              ? /*html*/ `<a href=${link} target="_blank" id=${id}>${name}</a>`
              : ''
          }
        </li>
      `);
      }, '') || ''
    }
  </ul>
`;
  return res;
};
