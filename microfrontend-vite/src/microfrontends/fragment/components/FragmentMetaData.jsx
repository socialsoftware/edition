import { setDocPath, toggleShow } from '../fragmentStore';
import DisplayDocModal from './DisplayDocModal';
import { getSourceData, getExpertData } from '../../documents/models/Fragment';
export default ({ sourceInter, messages, language, expert }) => {
  const displayDocument = (filename) => {
    toggleShow();
    setDocPath(filename);
  };

  return (
    <div className="well">
      {sourceInter
        ? expert
          ? getExpertData(sourceInter, messages?.[language])
          : getSourceData(sourceInter, messages?.[language], displayDocument)
        : null}
      <DisplayDocModal />
    </div>
  );
};
