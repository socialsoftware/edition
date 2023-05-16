/** @format */

export default ({ user, root }) => {
	return (
		<div>
			<form role="form" onSubmit={root.onUpdate}>
				<div class="form-floating">
					<input
						id="firstname"
						class="form-control"
						type="text"
						placeholder="firstname"
						name="firstName"
						value={user.firstName}
					/>
					<label data-users-key="firstName">{root.getConstant('firstName')}</label>
				</div>
				<div class="form-floating">
					<input
						id="lastname"
						class="form-control"
						type="text"
						name="lastName"
						placeholder="lastname"
						value={user.lastName}
					/>
					<label data-users-key="lastName">{root.getConstant('lastName')}</label>
				</div>
				<div class="form-floating">
					<input type="hidden" value={user.username} name="oldUsername" />
					<input
						class="form-control"
						id="username"
						type="text"
						name="newUsername"
						placeholder="username"
						value={user.username}
					/>
					<label for="username" data-users-key="username">
						{root.getConstant('username')}
					</label>
				</div>
				<div class="form-floating">
					<input
						id="newPassword"
						class="form-control"
						type="text"
						name="newPassword"
						placeholder={root.getConstant('newPassword')}
					/>
					<label for="newPassoword" data-users-key="newPassword">
						{root.getConstant('newPassword')}
					</label>
				</div>
				<div class="form-floating">
					<input
						id="email"
						class="form-control"
						type="text"
						name="email"
						placeholder="email"
						value={user.email}
					/>
					<label for="email" data-users-key="email">
						{root.getConstant('email')}
					</label>
				</div>

				<div>
					<div>
						<label data-users-key="user">{root.getConstant('user')}</label>
						<div>
							<label class="switch">
								<input
									name="user"
									type="checkbox"
									value="true"
									checked={user.roles.includes('ROLE_USER')}
								/>
								<span class="slider round"></span>
							</label>
						</div>
					</div>
					<div>
						<label data-users-key="admin">{root.getConstant('admin')}</label>
						<div>
							<label class="switch">
								<input
									name="admin"
									value="true"
									type="checkbox"
									checked={user.roles.includes('ROLE_ADMIN')}
								/>
								<span class="slider round"></span>
							</label>
						</div>
					</div>
					<div>
						<label data-users-key="enabled">{root.getConstant('enabled')}</label>
						<div>
							<label class="switch">
								<input
									name="enabled"
									type="checkbox"
									value="true"
									checked={user.enabled}
								/>
								<span class="slider round"></span>
							</label>
						</div>
					</div>
				</div>

				<button type="submit" class="btn btn-primary" data-users-key="update">
					{root.getConstant('update')}
				</button>
			</form>
		</div>
	);
};
