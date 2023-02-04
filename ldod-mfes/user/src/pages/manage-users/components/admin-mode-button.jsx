/** @format */

import { switchModeRequest } from '@src/api-requests.js';

export default ({ root, buttonLabel, tooltipContent }) => {
	const updateClass = () =>
		root.getMode() === 'admin' ? 'btn btn-danger ellipsis' : 'btn btn-success ellipsis';

	const onSwitchMode = () => {
		switchModeRequest().then(res => {
			res && root.setMode(res.ok);
			const button = root.shadowRoot.querySelector('button#user-switch-mode-button');
			button.querySelector('span[label]').textContent = root.getConstant(
				`${root.getMode()}Mode`
			);
			button.setAttribute('class', updateClass());
		});
	};

	return (
		<div id="adminMode" class="row btn-row">
			<button
				id="user-switch-mode-button"
				tooltip-ref="switch-button"
				type="button"
				class={updateClass()}
				onClick={onSwitchMode}>
				<span class="icon icon-edit"></span>
				<span label>{buttonLabel}</span>
			</button>
			<ldod-tooltip
				placement="top"
				data-ref="[tooltip-ref='switch-button']"
				data-users-tooltip-key="changeLdodMode"
				content={tooltipContent}></ldod-tooltip>
		</div>
	);
};
