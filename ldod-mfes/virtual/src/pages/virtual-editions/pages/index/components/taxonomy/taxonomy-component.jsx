/** @format */

import { isDev } from '@src/utils';
import references from '@src/references';
import { DeleteButton, MergeButton } from './merge-delete-buttons';
import TaxonomyTable from './taxonomy-table';

const onPublicContent = (target, node) => {
	const popover = node.shadowRoot.querySelector('ldod-popover');
	popover.element = () => (
		<>
			<ul class="drop-menu" style={{ minWidth: `${target.clientWidth}px` }}>
				<li>
					<a
						target="_blank"
						class="drop-item"
						is="nav-to"
						to={`${isDev() ? '' : '/ldod-mfes'}${references.virtualEdition(
							node.taxonomy.veAcronym
						)}`}>
						{node.taxonomy.veAcronym}
					</a>
				</li>
			</ul>
		</>
	);

	popover.target = target;
	popover.toggleAttribute('show');
};

const onUsedIn = (target, node) => {
	const popover = node.shadowRoot.querySelector('ldod-popover');
	popover.element = () => (
		<>
			<ul class="drop-menu" style={{ minWidth: `${target.clientWidth}px` }}>
				{node.taxonomy.usedIn.map(ed => (
					<li key={crypto.randomUUID()}>
						<a
							target="_blank"
							class="drop-item"
							is="nav-to"
							to={`${isDev() ? '' : '/ldod-mfes'}${references.virtualEdition(ed)}`}>
							{ed}
						</a>
					</li>
				))}
			</ul>
		</>
	);

	popover.target = target;
	popover.toggleAttribute('show');
};

export default ({ node }) => {
	return (
		<div style={{ padding: '20px' }} id="taxonomyComponent">
			<div>
				<div class="flex-center" style={{ marginBottom: '10px' }}>
					<button
						style={{ width: '200px' }}
						id="publicContent"
						class="btn btn-sm btn-secondary"
						type="button"
						open-popover
						onClick={({ target }) => onPublicContent(target, node)}>
						{node.getConstants('publicContent')}
						<ldod-popover data-distance="0"></ldod-popover>
					</button>

					{node.taxonomy.usedIn.length && (
						<button
							style={{ width: '200px' }}
							id="usedIn"
							class="btn btn-sm btn-secondary"
							type="button"
							open-popover
							onClick={({ target }) => onUsedIn(target, node)}>
							{node.getConstants('usedIn')}
						</button>
					)}
				</div>
				<div id="virtual-categoryActionsContainer" class="flex-center">
					<form onSubmit={node.onAddCategory}>
						<div id="catInputName" class="input-group">
							<input
								style={{ width: '200px' }}
								required
								id="addCategoryInput"
								name="name"
								type="text"
								class="form-control"
								placeholder={node.getConstants('categoryName')}
								aria-label="New category's name"
								aria-describedby="category-add-button"
							/>
							<button
								style={{ width: '200px' }}
								id="addCategory"
								type="submit"
								class="btn btn-sm btn-primary">
								<span
									is="ldod-span-icon"
									icon="plus"
									fill="#fff"
									size="16px"></span>
								<span data-virtual-key="addCategory">
									{node.getConstants('addCategory')}
								</span>
							</button>
						</div>
					</form>
					<MergeButton node={node} />
					<button
						style={{ width: '200px' }}
						id="generateTopics"
						type="button"
						class="btn btn-sm btn-primary"
						onClick={node.onGenerateTopics}>
						<span is="ldod-span-icon" icon="gear" size="16px" fill="#fff"></span>
						<span data-virtual-key="generateTopics">
							{node.getConstants('generateTopics')}
						</span>
					</button>
					<DeleteButton node={node} />
				</div>
				{['addCategory', 'generateTopics'].map(id => (
					<ldod-tooltip
						key={crypto.randomUUID()}
						data-ref={`button#${id}`}
						data-virtual-tooltip-key={`${id}Info`}
						placement="top"
						width="300px"
						content={node.getConstants(`${id}Info`)}></ldod-tooltip>
				))}
			</div>
			<TaxonomyTable node={node} />
		</div>
	);
};
