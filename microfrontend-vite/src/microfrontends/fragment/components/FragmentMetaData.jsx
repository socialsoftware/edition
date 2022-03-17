import { getExpertData, getSourceData } from '../../documents/models/Fragment';
import { isEditorial } from '../dataExtraction';
import {
  fragmentStateSelector,
  setDocPath,
  toggleShow,
} from '../fragmentStore';
import DisplayDocModal from '../../../shared/DisplayDocModal';

function displayDocument(filename) {
  toggleShow();
  setDocPath(filename);
}

export default ({ fragment, messages }) => {
  const metaData = fragment.metaData ?? {};
  const showModal = fragmentStateSelector('showModal');
  const docPath = fragmentStateSelector('docPath');

  return (
    <div className="well">
      {isEditorial(fragment)
        ? getExpertData(metaData, messages)
        : getSourceData(metaData, messages, displayDocument)}
      <DisplayDocModal
        toggleShow={toggleShow}
        docPath={docPath}
        showModal={showModal}
      />
    </div>
  );
};
