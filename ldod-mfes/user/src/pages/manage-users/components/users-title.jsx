/** @format */

export default ({ root, title }) => {
	return (
		<h2 id="title" class="text-center">
			<span data-users-key="users">{title}</span>
			<span>&nbsp;({root.usersLength})</span>
		</h2>
	);
};
