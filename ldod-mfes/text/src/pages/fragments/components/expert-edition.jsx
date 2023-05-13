/** @format */

import { processFragmentAnchor } from '../../../utils';

const getPage = (startPage, endPage) =>
	startPage ? (endPage !== startPage ? `${startPage} - ${endPage}` : startPage) : '';

const getDate = (date, precision) =>
	date ? (
		precision ? (
			<>
				{date} (<i>{precision}</i>)
			</>
		) : (
			date
		)
	) : (
		''
	);

const getAnnexNote = notes => notes.map(note => note).join(',');

export const getExpertEdition = (inters, node) => {
	return inters?.reduce((prev, inter) => {
		const page = getPage(inter.startPage, inter.endPage);
		const date = getDate(inter.date, inter.precision);
		const annexNote = getAnnexNote(inter.annexNote);
		const curr = (
			<>
				<strong data-key="colTitle">{node.getConstants('colTitle')}</strong>
				{inter.title}
				<br />
				<strong data-key="heteronym">{node.getConstants('heteronym')}</strong>
				{inter.heteronym ?? (
					<span data-key="notAssigned">{node.getConstants('notAssigned')}</span>
				)}
				<br />
				{inter.volume && (
					<>
						<strong data-key="volume">{node.getConstants('volume')}</strong>
						{inter.volume}
						<br />
					</>
				)}
				{inter.number && (
					<>
						<strong data-key="number">{node.getConstants('number')}</strong>
						{inter.number}
						<br />
					</>
				)}
				{page && (
					<>
						<strong data-key="page">{node.getConstants('page')}</strong>
						{page}
						<br />
					</>
				)}
				{date && (
					<>
						<strong data-key="date">{node.getConstants('date')}</strong>
						{date}
						<br />
					</>
				)}
				{inter.notes && (
					<>
						<strong data-key="notes">{node.getConstants('notes')}</strong>
						{inter.notes}
						<br />
					</>
				)}
				{annexNote && (
					<>
						<strong data-key="nota">{node.getConstants('nota')}</strong>
						{processFragmentAnchor(htmlRender(annexNote))}
						<br />
					</>
				)}
			</>
		);
		prev.append(curr, <p></p>);
		return prev;
	}, <></>);
};
