import { PASSWORD_REGEX } from '@src/resources/constants.js';
import { setState, getState } from '@src/store';
import { navigateTo } from '@shared/router.js';
import { changePasswordRequest } from '@src/api-requests.js';
import { setInvalidFor, setValidFor, loadConstants } from '@src/utils';
import ChangePasswordForm from './change-password-form';
import { userReferences } from '../../user-references';
import { errorPublisher, messagePublisher } from '../../events-modules';

export class ChangePassword extends HTMLElement {
	constructor() {
		super();
		this.isValid = true;
		this.username = getState().user?.username;
		this.current = {
			value: '',
			test: val => val,
			message: () => this.getConstants('required'),
		};
		this.new = {
			value: '',
			test: val => PASSWORD_REGEX.test(val) && val !== this.current.value,
			message: () => this.getConstants('minCurrent'),
		};
		this.confirm = {
			value: '',
			test: val => val && val === this.new.value,
			message: () => this.getConstants('confirmPattern'),
		};
	}
	static get observedAttributes() {
		return ['language'];
	}

	get language() {
		return this.getAttribute('language');
	}

	async connectedCallback() {
		await this.setConstants();
		if (!getState().user) return navigateTo(userReferences.signin());
		this.appendChild(<ChangePasswordForm node={this} />);
	}

	attributeChangedCallback(name, oldValue, newValue) {
		if (name === 'language' && oldValue && newValue !== oldValue) {
			this.updateLanguage();
		}
	}

	setConstants = async () => (this.constants = await loadConstants(this.language));

	getConstants(key) {
		return this.constants[key];
	}

	async updateLanguage() {
		await this.setConstants();
		this.querySelectorAll('[data-key]').forEach(ele => {
			ele.innerHTML = this.getConstants(ele.dataset.key);
		});
		setState({ language: this.language });
	}

	handleSubmit = async e => {
		e.preventDefault();
		this.checkInputs();
		if (this.isValid) {
			await changePasswordRequest({
				username: this.username,
				currentPassword: this.current.value,
				newPassword: this.new.value,
				retypedPassword: this.confirm.value,
			})
				.then(data => {
					if (data.ok === false) return errorPublisher(data.message);
					messagePublisher(data.message);
					navigateTo('/');
				})
				.catch(error => console.log(error));
			this.clearDataInputs();
			this.clearStyleInputs();
		}
		this.isValid = true;
	};

	checkInputs = () => {
		const getInput = name => this.querySelector(`input[name=${name}]`);
		['current', 'new', 'confirm'].forEach(input => {
			let state = this[input];
			if (!state.test(state.value)) {
				this.isValid = false;
				return setInvalidFor(getInput(input), state.message());
			}
			setValidFor(getInput(input));
		});
	};

	clearDataInputs() {
		this.querySelectorAll('input[type=password]').forEach(input => {
			input.value = '';
			this[input.name].value = '';
		});
	}

	clearStyleInputs() {
		this.querySelectorAll('.form-control').forEach(div => div.classList.remove('valid', 'invalid'));
	}

	revealPassword = ({ target }) => (target.parentElement.querySelector('input').type = 'text');

	hidePassword = ({ target }) => (target.parentElement.querySelector('input').type = 'password');
}
!customElements.get('change-password') && customElements.define('change-password', ChangePassword);
