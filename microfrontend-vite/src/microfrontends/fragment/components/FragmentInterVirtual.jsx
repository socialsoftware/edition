import Fragment from './Fragment';
import parserHTML from 'html-react-parser';
import Taxonomy from './Taxonomy';
import FragmentInterVirtualComp from './FragmentInterVirtualComp';

export default ({ messages, language, fragmentInter }) => {
  return (
    <>
      {fragmentInter && fragmentInter?.taxonomies ? (
        <>
          <h4>{`${parserHTML(fragmentInter.editionTitle ?? '') ?? ''} - ${
            messages?.[language]['uses']
          } ${fragmentInter.usesReference}`}</h4>

          <br />
          <div id="fragment-transcript">
            <Fragment fragment={fragmentInter} isNormal={false}/>
          </div>
          <Taxonomy taxonomies={fragmentInter.taxonomies ?? []} />
        </>
      ) : (
        <FragmentInterVirtualComp
          messages={messages}
          language={language}
          fragmentInter={fragmentInter}
        />
      )}
    </>
  );
};
