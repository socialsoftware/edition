import Fragment from './Fragment';
import FragmentMetaData from './FragmentMetaData';
import Checkboxes from './Checkboxes';

export default ({ messages, language, fragmentInter }) => {
  return (
    <>
      <Checkboxes
        messages={messages}
        language={language}
        checkboxes={
          fragmentInter?.type === 'AUTHORIAL'
            ? ['diff', 'del', 'ins', 'sub', 'note', 'fac']
            : ['diff']
        }
      />
      <br />
      <div id="fragment-transcript">
        <Fragment fragment={fragmentInter} />
      </div>
      <br />
      <FragmentMetaData
        messages={messages}
        language={language}
        sourceInter={
          fragmentInter?.type === 'AUTHORIAL'
            ? fragmentInter?.metaData[0]
            : fragmentInter?.metaData[1]
        }
        type={fragmentInter?.type}
      />
    </>
  );
};
