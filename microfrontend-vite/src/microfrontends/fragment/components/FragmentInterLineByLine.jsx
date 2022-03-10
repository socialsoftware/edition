import Fragment from './Fragment';
import Checkboxes from './Checkboxes';
import VariationsTable from './VariationsTable';
import { getAuthorialsInter } from '../fragmentStore';

export default ({ messages, fragmentInter, getVariationsHeaders }) => {
  return (
    <>
      <Checkboxes
        messages={messages}
        checkboxes={
          getAuthorialsInter().length > 2 ? ['align'] : ['line', 'align']
        }
      />
      <br />
      <div id="fragment-transcript">
        <Fragment fragment={fragmentInter} messages={messages}/>
      </div>
      <br />
      <div>
        <VariationsTable
          messages={messages}
          variations={fragmentInter.variations ?? []}
          headers={getVariationsHeaders(fragmentInter) ?? []}
        />
      </div>
    </>
  );
};
