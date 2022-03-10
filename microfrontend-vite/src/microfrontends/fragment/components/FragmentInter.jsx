import Fragment from './Fragment';
import FragmentMetaData from './FragmentMetaData';
import Checkboxes from './Checkboxes';
import { isAuthorial } from '../dataExtraction';

const getCheckboxesAccordingFragType = (fragmentInter) =>
  isAuthorial(fragmentInter)
    ? ['diff', 'del', 'ins', 'sub', 'note', 'fac']
    : ['diff'];

const getMetaDataAccordingFragType = (fragmentInter) =>
  isAuthorial(fragmentInter)
    ? fragmentInter?.metaData[0]
    : fragmentInter?.metaData[1];

export default ({ messages, fragmentInter }) => {
  return (
    <>
      <Checkboxes
        messages={messages}
        checkboxes={getCheckboxesAccordingFragType(fragmentInter)}
      />
      <br />
      <div id="fragment-transcript">
        <Fragment fragment={fragmentInter} />
      </div>
      <br />
      <FragmentMetaData
        messages={messages}
        fragment={fragmentInter}
      />
    </>
  );
};
