import { getVirtualEdition } from '../api/edition';
import { getAcronym, setAcronym } from '../editionStore';

export default ({acronym}) => {

  acronym !== getAcronym() && getVirtualEdition(acronym)
  setAcronym(acronym);

  return (<div></div>)
}