/** @format */

import { hidePassword, revealPassword } from '../common-functions';
import constants from '../constants';
import '@ui/modal-bs.js';

let conductCode;

function showConductCode(root) {
	!conductCode &&
		import('about').then(({ loadConductCode }) => {
			loadConductCode();
			conductCode = true;
		});
	root.shadowRoot.querySelector('ldod-bs-modal')?.toggleAttribute('show', true);
}
export default (language, root) => {
	return (
		<>
			<h2 data-user-key="register">{constants[language].register}</h2>
			<form id="signup-form" class="needs-validation" novalidate onSubmit={root.onSubmit}>
				<div id="signup-form-firstname" class="form-floating">
					<input
						class="form-control"
						name="firstName"
						type="text"
						placeholder="firstname"
						required
						pattern="^[-'a-zA-ZÀ-ÖØ-öø-ÿ]+$"
					/>
					<label for="login-form-username" data-user-key="firstname">
						{constants[language].firstname}
					</label>
					<div class="invalid-tooltip" data-user-key="alphabetic">
						{constants[language].alphabetic}
					</div>
				</div>
				<div id="signup-form-lastname" class="form-floating">
					<input
						class="form-control"
						name="lastName"
						type="text"
						placeholder="lastname"
						required
						pattern="^[-'a-zA-ZÀ-ÖØ-öø-ÿ]+$"
					/>
					<label for="signup-form-lastname" data-user-key="lastname">
						{constants[language].lastname}
					</label>
					<div class="invalid-tooltip" data-user-key="alphabetic">
						{constants[language].alphabetic}
					</div>
				</div>

				<div id="signup-form-username" class="form-floating">
					<input
						class="form-control"
						name="username"
						type="text"
						placeholder="username"
						pattern="^[a-zA-Z0-9]+$"
						required
					/>
					<label for="signup-form-username" data-user-key="username">
						{constants[language].username}
					</label>
					<div class="invalid-tooltip" data-user-key="alphanumeric">
						{constants[language].alphanumeric}
					</div>
				</div>

				<div class="input-group">
					<div id="signup-form-password" class="form-floating">
						<input
							class="form-control"
							name="password"
							type="password"
							placeholder="password"
							required
							pattern="[0-9a-zA-Z]{6,}"
						/>
						<div class="invalid-tooltip" data-user-key="passwordLength">
							{constants[language].passwordLength}
						</div>
						<label for="signup-form-password" data-user-key="password">
							{constants[language].password}
						</label>
					</div>

					<button id="user-password-btn" class="btn" type="button">
						<span
							data-input="div#signup-form-password > input"
							id="user-password-icon"
							is="ldod-span-icon"
							icon="eye"
							hover-fill="#0d6efd"
							onPointerDown={revealPassword}
							onPointerUp={hidePassword}></span>
					</button>
				</div>

				<div id="signup-form-email" class="form-floating">
					<input
						class="form-control"
						name="email"
						type="email"
						placeholder="email"
						required
					/>
					<label for="signup-form-email" data-user-key="email">
						{constants[language].email}
					</label>
					<div class="invalid-tooltip" data-user-key="invalidEmail">
						{constants[language].invalidEmail}
					</div>
				</div>
				<div class="form-check">
					<input
						id="conduct-check"
						class="form-check-input"
						name="conduct"
						type="checkbox"
						value="true"
						required
					/>
					<a
						id="conduct-code"
						class="link-primary"
						data-user-key="conduct"
						onClick={() => showConductCode(root)}>
						{constants[language].conduct}
					</a>
					<ldod-bs-modal
						dialog-class="modal-xl modal-dialog-scrollable"
						id="modal-conduct-code"
						static>
						<h4 data-user-key="conductCode" slot="header-slot">
							{constants[language].conductCode}
						</h4>
						<div slot="body-slot">
							<ldod-conduct language={language}></ldod-conduct>
						</div>
					</ldod-bs-modal>
				</div>
				<input type="hidden" name="socialId" />
				<input type="hidden" name="socialMedia" />
				<button type="submit" class="btn btn-primary" data-user-key="register">
					{constants[language].register}
				</button>
			</form>
		</>
	);
};
