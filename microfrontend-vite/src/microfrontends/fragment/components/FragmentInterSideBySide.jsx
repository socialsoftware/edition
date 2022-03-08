import Checkboxes from './Checkboxes';
import Fragment from './Fragment';
import FragmentMetaData from './FragmentMetaData';
import VariationsTable from './VariationsTable';

export default ({ messages, language, fragmentInter }) => {
  return (
    <div>
      <Checkboxes
        messages={messages}
        language={language}
        checkboxes={['line', 'align']}
      />
      <div
        id="fragmentComparison"
        className="row"
        style={{ marginLeft: '-15px', marginRight: '-15px' }}
      >
        <div className="row">
          {fragmentInter?.inters?.map(({ title }, index) => (
            <div key={index} id="fragmentTranscription" className="col-md-6">
              <Fragment
                isNormal={false}
                fragment={{
                  title,
                  transcript: fragmentInter?.transcript[index],
                  fontSize: fragmentInter?.fontSize,
                  fontFamily: fragmentInter?.fontFamily,
                }}
              />
            </div>
          ))}
        </div>
        <div className="row">
          {fragmentInter?.inters?.map((inter, index) => (
            <div id="interMeta" key={index} className="col-md-6">
              <FragmentMetaData
                sourceInter={
                  inter?.type === 'AUTHORIAL'
                    ? fragmentInter?.metaData[0]
                    : inter
                }
                messages={messages}
                language={language}
                type={inter?.type}
              />
            </div>
          ))}
        </div>
        <div>
          <VariationsTable
            messages={messages}
            language={language}
            variations={fragmentInter?.variations}
            headers={fragmentInter?.inters?.map(({ shortName, title }) => ({
              shortName,
              title,
            }))}
          />
        </div>
      </div>
    </div>
  );
};
