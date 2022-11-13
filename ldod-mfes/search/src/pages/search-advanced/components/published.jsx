import DateSelect from './dateWebComp';

export default ({ root, form }) => {
  const published = new DateSelect(root, form);
  published.hidden = false;
  published.beginDate = root.data.publicationsDates.beginDate;
  published.endDate = root.data.publicationsDates.endDate;
  return published;
};
