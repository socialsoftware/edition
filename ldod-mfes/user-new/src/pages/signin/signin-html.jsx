import constants from '../constants';
import { hidePassword, revealPassword } from '../common-functions';
import { userReferences } from '../../user-references';
import '@shared/router.js';

let gauth;

async function prefetchGoogleAuth() {
	if (gauth) return;
	gauth = (await import('./google-auth')).default;
}

async function googleAuth() {
	await gauth();
}

export default (language, root) => (
	<>
		<h2 data-user-key="title">{constants[language].title}</h2>
		<form id="login-form" class="needs-validation" novalidate onSubmit={root.onSubmit}>
			<div id="login-form-username" class="form-floating">
				<input class="form-control" name="username" type="text" placeholder="username" required />
				<label for="login-form-username" data-user-key="username">
					{constants[language].username}
				</label>
			</div>
			<div class="input-group">
				<div id="user-login-form" class="form-floating">
					<input class="form-control" name="password" type="password" placeholder="password" required />
					<label for="login-form-password" data-user-key="password">
						{constants[language].password}
					</label>
				</div>
				<button id="user-password-btn" class="btn" type="button">
					<span
						data-input="div#user-login-form > input"
						id="user-password-icon"
						is="ldod-span-icon"
						icon="eye"
						onPointerDown={revealPassword}
						onPointerUp={hidePassword}></span>
				</button>
			</div>

			<button type="submit" class="btn btn-primary" data-user-key="signin">
				{constants[language].signin}
			</button>
		</form>
		<button
			id="user-google-auth"
			type="button"
			class="btn btn-light"
			onPointerOver={prefetchGoogleAuth}
			onClick={googleAuth}>
			<div style={{ display: 'grid', gridTemplateColumns: '10% 80% 10%' }}>
				<span class="google-icon"></span>
				<span>Google</span>
			</div>
		</button>
		<a data-user-key="signup" is="nav-to" to={userReferences.signup()}>
			{constants[language].signup}
		</a>
	</>
);
