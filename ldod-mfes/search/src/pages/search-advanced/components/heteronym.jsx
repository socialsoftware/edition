import HeteronymSelect from './heteronymWebComp';

export default ({ root, form }) => {
  const heteronym = new HeteronymSelect(root, form);
  heteronym.hidden = false;
  return heteronym;
};
