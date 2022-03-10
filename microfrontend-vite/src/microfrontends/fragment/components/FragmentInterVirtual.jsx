import Fragment from './Fragment';
import parserHTML from 'html-react-parser';
import Taxonomy from './Taxonomy';
import FragmentInterVirtualComp from './FragmentInterVirtualComp';

export default ({ messages, fragmentInter }) => {
  return (
    <>
      {fragmentInter.taxonomies ? (
        <>
          <h4>{`${parserHTML(fragmentInter.editionTitle ?? '') ?? ''} - ${
            messages.uses
          } ${fragmentInter.usesReference}`}</h4>

          <br />
          <div id="fragment-transcript">
            <Fragment fragment={fragmentInter}/>
          </div>
          <Taxonomy taxonomies={fragmentInter.taxonomies} />
        </>
      ) : (
        <FragmentInterVirtualComp
          messages={messages}
          fragmentInter={fragmentInter}
        />
      )}
    </>
  );
};
