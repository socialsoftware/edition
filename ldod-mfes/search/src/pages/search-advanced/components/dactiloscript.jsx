import SourceSelect from './sourceWebComp';

export default ({ root, form }) => {
	const source = new SourceSelect(root, form);
	source.hidden = false;
	source.beginDate = root.data.typescriptDates?.beginDate;
	source.endDate = root.data.typescriptDates?.endDate;
	return source;
};
