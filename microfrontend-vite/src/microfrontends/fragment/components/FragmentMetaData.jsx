import { setDocPath, toggleShow } from '../fragmentStore';
import DisplayDocModal from './DisplayDocModal';
import { getSourceData, getExpertData } from '../../documents/models/Fragment';
export default ({ sourceInter, messages, language, type }) => {
  const displayDocument = (filename) => {
    toggleShow();
    setDocPath(filename);
  };
  return (
    <div className="well">
      {type === 'EDITORIAL'
        ? getExpertData(sourceInter, messages?.[language])
        : getSourceData(sourceInter, messages?.[language], displayDocument)}
      <DisplayDocModal />
    </div>
  );
};
