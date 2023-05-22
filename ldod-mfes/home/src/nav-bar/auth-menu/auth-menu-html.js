/** @format */

export default ({ isAuth, data, name }, lang) => {
	return isAuth ? authed(name, data, lang) : links(data, lang);
};

const authed = (name, data, lang) => {
	return /*html*/ `
  <a class="dropdown-toggle nav-link" role="button" key="user">
    ${name}
    <span class="caret"></span>
  </a>
  <ul class="dropdown-menu">
    ${links(data, lang)}
  </ul>`;
};

const links = ({ constants, links }, lang) => {
	return links
		.map(({ key, path }) => {
			return /* html */ `
			<a is="nav-to" class="dropdown-item" data-key="${key}" to="${path}" key="user">
				${constants[lang]?.[key] ?? key}
			</a>`;
		})
		.join('');
};
