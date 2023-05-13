/** @format */

import '@ui/modal-bs.js';
import { getState, readingStore, resetReadingStore } from '../../../store';
import modalStyle from './modal-stytle.css?inline';

const onChangeWeight = ({ target }) => {
	readingStore.setState({ [target.name]: target.value });
};

const onReset = root => {
	resetReadingStore(root.currentInter);
	root.recommendationModal.toggleAttribute('show');
};

export default ({ root }) => {
	return (
		<>
			<style>{modalStyle}</style>
			<ldod-bs-modal dialog-class="modal-xl" id="reading-recommendation--modal">
				<div slot="header-slot">
					<h3 class="modal-title text-center" data-reading-key="recomModalTitle">
						{root.getConstants('recomModalTitle')}
					</h3>
				</div>
				<div slot="body-slot">
					<h3 class="text-center" data-reading-key="sortCriteria">
						{root.getConstants('sortCriteria')}
					</h3>
					<div class="criterias-wrapper">
						{['heteronym', 'date', 'text', 'taxonomy'].map(criteria => {
							return (
								<>
									<div>
										<span data-reading-key={criteria}>
											{root.getConstants(criteria)}
										</span>
										<input
											min="0"
											max="1"
											step="0.2"
											value={getState()[`${criteria}Weight`]}
											onChange={onChangeWeight}
											id={`${criteria}-range`}
											type="range"
											name={`${criteria}Weight`}
										/>
									</div>
								</>
							);
						})}
					</div>
				</div>
				<div slot="footer-slot">
					<button
						type="button"
						data-reading-key="reset"
						class="btn btn-danger"
						onClick={() => onReset(root)}>
						{root.getConstants('reset')}
					</button>
					<button
						type="button"
						class="btn btn-primary"
						data-reading-key="submit"
						onClick={root.onRecommendationSubmit}>
						{root.getConstants('submit')}
					</button>
				</div>
			</ldod-bs-modal>
			<ldod-tooltip
				data-ref="span#reading-recommendationTooltip"
				data-reading-tooltip-key="recommendationTooltipInfo"
				placement="top"
				light
				width="350px"
				content={root.getConstants('recommendationTooltipInfo')}></ldod-tooltip>
		</>
	);
};
