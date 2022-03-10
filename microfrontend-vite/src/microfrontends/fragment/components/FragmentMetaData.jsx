import { getExpertData, getSourceData } from '../../documents/models/Fragment';
import { isEditorial, isPublication } from '../dataExtraction';
import { setDocPath, toggleShow } from '../fragmentStore';
import DisplayDocModal from './DisplayDocModal';

function displayDocument(filename) {
  toggleShow();
  setDocPath(filename);
}

export default ({ fragment, messages }) => {
  const metaData = fragment.metaData ?? {}
  return (
    <div className="well">
      {isEditorial(fragment)
        ? getExpertData(metaData, messages)
        : getSourceData(metaData, messages, displayDocument)}
      <DisplayDocModal />
    </div>
  );
};
