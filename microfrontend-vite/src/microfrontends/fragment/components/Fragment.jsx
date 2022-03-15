import parserHTML from 'html-react-parser';
import { lazy } from 'react';
import { Link, useParams } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import { isEditorial, isLineByLine, isSingle } from '../dataExtraction';
const ImageViewer = lazy(() => import('./ImageViewer'));
const getTranscript = (fragment) =>
  fragment.transcript ??
  fragment.setTranscriptionSideBySide ??
  fragment.writerLineByLine;

export default ({ fragment, messages }) => {
  const { xmlid, urlid } = useParams();
  return (
    <>
      <h4 className={isLineByLine(fragment) ? '' : 'text-center'}>
        {fragment.title}
        {isLineByLine(fragment) && (
          <>
            <ReactTooltip
              id="lineByLine-info"
              type="light"
              place="bottom"
              effect="solid"
              border={true}
              borderColor="rgba(0,0,0,.2)"
              className="fragment-nav-tooltip"
              getContent={() => messages['line_by_line_info']}
            />
            <span
              data-tip=""
              data-for="lineByLine-info"
              className="glyphicon glyphicon-info-sign gray-color"
            ></span>
          </>
        )}
        {isEditorial(fragment) && isSingle(fragment) && (
          <Link to={`/reading/fragment/${xmlid}/inter/${urlid}`}>
            <span className="glyphicon glyphicon-eye-open"></span>
          </Link>
        )}
      </h4>
      {getTranscript(fragment) && (
        <div className="row">
          {fragment.surface?.graphic && (
            <ImageViewer surface={fragment.surface} />
          )}
          <div
            className={`well ${fragment.surface?.graphic && 'col-md-6'}`}
            style={{
              fontFamily: fragment.fontFamily,
              fontSize: fragment.fontSize,
            }}
          >
            <p></p>
            <>{parserHTML(getTranscript(fragment))}</>
            <p></p>
          </div>
        </div>
      )}
    </>
  );
};
