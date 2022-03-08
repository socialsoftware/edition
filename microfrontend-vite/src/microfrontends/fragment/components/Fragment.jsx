import parserHTML from 'html-react-parser';
import { lazy } from 'react';
import { Link, useParams } from 'react-router-dom';
const ImageViewer = lazy(() => import('./ImageViewer'));

export default ({ fragment, isNormal = true }) => {
  const { xmlid, urlid } = useParams();

  return (
    <>
      <h4 className="text-center">
        {fragment?.title}
        {fragment?.type === 'EDITORIAL' && isNormal && (
          <Link to={`/reading/${xmlid}/${urlid}`}>
            <span className="glyphicon glyphicon-eye-open"></span>
          </Link>
        )}
      </h4>
      <br />
      {fragment?.transcript && (
        <div className="row">
          {fragment?.surface?.graphic && (
            <ImageViewer surface={fragment?.surface} />
          )}
          <div
            className={`well ${fragment?.surface?.graphic && 'col-md-6'}`}
            style={{
              fontFamily: fragment?.fontFamily,
              fontSize: fragment?.fontSize,
            }}
          >
            <p></p>
            <>
              {parserHTML(fragment?.transcript)}
            </>
            <p></p>
          </div>
        </div>
      )}
    </>
  );
};
