import SourceSelect from './sourceWebComp';

export default ({ root, form }) => {
	const source = new SourceSelect(root, form);
	source.hidden = false;
	source.beginDate = root.data.manuscriptDates?.beginDate;
	source.endDate = root.data.manuscriptDates?.endDate;
	return source;
};
