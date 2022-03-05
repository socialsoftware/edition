import parserHTML from 'html-react-parser';
import { Link, useParams } from 'react-router-dom';
export default ({ fragment, fontFamily, fontSize, expert, virtual }) => {
  const {xmlid, urlid} = useParams();
  return (
    <div id="fragment-transcript">
      <h4 className="text-center">
        {fragment?.title}
        {expert && (
          <Link to={`/reading/${xmlid}/${urlid}`}>
            <span className="glyphicon glyphicon-eye-open"></span>
          </Link>
        )}
      </h4>
      <br />
      {fragment?.transcript && (
        <div
          className="well"
          style={{ fontFamily: fontFamily, fontSize: fontSize }}
        >
          <p></p>
          {parserHTML(fragment.transcript)}
          <p></p>
        </div>
      )}
    </div>
  );
};
