import Fragment from './Fragment';
import Checkboxes from './Checkboxes';
import VariationsTable from './VariationsTable';

export default ({ messages, language, fragmentInter }) => {
  return (
    <>
      <Checkboxes
        messages={messages}
        language={language}
        checkboxes={['align']}
      />
      <br />
      <div id="fragment-transcript">
        <Fragment fragment={fragmentInter} isNormal={false} />
      </div>
      <br />
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
    </>
  );
};
