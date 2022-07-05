import Checkboxes from './Checkboxes';
import Fragment from './Fragment';
import FragmentMetaData from './FragmentMetaData';
import VariationsTable from './VariationsTable';

export default ({ messages, fragmentInter, getVariationsHeaders }) => {
  return (
    <>
      <Checkboxes messages={messages} checkboxes={['line', 'align']} />
      <br />
      <div
        id="fragmentComparison"
        className="row"
        style={{ marginLeft: '-15px', marginRight: '-15px' }}
      >
        <div className="row">
          {fragmentInter.inters?.map((inter, index) => (
            <div key={index} id="fragmentTranscription" className="col-md-6">
              <Fragment fragment={inter} />
            </div>
          ))}
        </div>
        <div className="row">
          {fragmentInter.inters?.map((inter, index) => (
            <div id="interMeta" key={index} className="col-md-6">
              <FragmentMetaData fragment={inter} messages={messages} />
            </div>
          ))}
        </div>
        <div>
          <VariationsTable
            messages={messages}
            variations={fragmentInter.variations ?? []}
            headers={getVariationsHeaders(fragmentInter) ?? []}
          />
        </div>
      </div>
    </>
  );
};
