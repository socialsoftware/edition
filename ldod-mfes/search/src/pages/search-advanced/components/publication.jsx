import DateSelect from './dateWebComp';

export default ({ root, form }) => {
	const published = new DateSelect(root, form);
	published.hidden = false;
	published.beginDate = root.data.publicationDates?.beginDate;
	published.endDate = root.data.publicationDates?.endDate;
	return published;
};
