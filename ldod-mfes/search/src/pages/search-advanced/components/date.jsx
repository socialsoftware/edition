import DateSelect from './dateWebComp';

export default ({ root, form }) => {
	const date = new DateSelect(root, form);
	date.hidden = false;
	date.beginDate = root.data.dates?.beginDate;
	date.endDate = root.data.dates?.endDate;
	return date;
};
