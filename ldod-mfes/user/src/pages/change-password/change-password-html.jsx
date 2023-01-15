import { getState } from '../../store';
import { hidePassword, revealPassword } from '../common-functions';
import constants from '../constants';
export default (language, root) => {
	return (
		<>
			<h2 data-user-key="changePassword">{constants[language].changePassword}</h2>
			<form id="change-password-form" class="needs-validation" novalidate onSubmit={root.onSubmit}>
				<input type="hidden" name="username" value={getState().user?.username || ''} />
				<div class="input-group">
					<div id="current-password" class="form-floating">
						<input
							class="form-control"
							name="currentPassword"
							type="password"
							placeholder="current password"
							required
						/>

						<label for="current-password" data-user-key="currentPassword">
							{constants[language].currentPassword}
						</label>
					</div>
					<button id="user-password-btn" class="btn" type="button">
						<span
							data-input="div#current-password > input"
							id="user-password-icon"
							is="ldod-span-icon"
							icon="eye"
							hover-fill="#0d6efd"
							onPointerDown={revealPassword}
							onPointerUp={hidePassword}></span>
					</button>
				</div>
				<div class="input-group">
					<div id="new-password" class="form-floating">
						<input
							class="form-control"
							name="newPassword"
							type="password"
							placeholder="password"
							required
							pattern="[0-9a-zA-Z]{6,}"
						/>
						<div class="invalid-tooltip" data-user-key="passwordLength">
							{constants[language].passwordLength}
						</div>
						<label for="new-password" data-user-key="newPassword">
							{constants[language].newPassword}
						</label>
					</div>
					<button id="user-password-btn" class="btn" type="button">
						<span
							data-input="div#new-password > input"
							id="user-password-icon"
							is="ldod-span-icon"
							icon="eye"
							hover-fill="#0d6efd"
							onPointerDown={revealPassword}
							onPointerUp={hidePassword}></span>
					</button>
				</div>
				<div class="input-group">
					<div id="confirm-password" class="form-floating">
						<input
							class="form-control"
							name="retypedPassword"
							type="password"
							placeholder="password"
							required
							pattern="[0-9a-zA-Z]{6,}"
						/>
						<div class="invalid-tooltip" data-user-key="passwordLength">
							{constants[language].passwordLength}
						</div>
						<label for="confirm-password" data-user-key="confirmPassword">
							{constants[language].confirmPassword}
						</label>
					</div>
					<button id="user-password-btn" class="btn" type="button">
						<span
							data-input="div#confirm-password > input"
							id="user-password-icon"
							is="ldod-span-icon"
							icon="eye"
							hover-fill="#0d6efd"
							onPointerDown={revealPassword}
							onPointerUp={hidePassword}></span>
					</button>
				</div>
				<button type="submit" class="btn btn-primary" data-user-key="update">
					{constants[language].update}
				</button>
			</form>
		</>
	);
};
