import Checkboxes from './Checkboxes';
import Title from './Title';
import { getExpertEdition } from '../../fragments/components/expertEdition';
import EditorialInterTranscription from './EditorialInterTranscription';

export default ({ node, inter }) => {
  return (
    <>
      <Checkboxes node={node} checkboxes={['diff']} />
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <Title title={node.data.title} />
        <a
          is="nav-to"
          to={`/reading/fragment/${node.xmlId}/inter/${inter.urlId}`}>
          <span style={{ marginTop: '25px' }} class="icon icon-eye"></span>
        </a>
      </div>
      <EditorialInterTranscription node={node} key={0} />
      <br />
      <div id="interMetaIndo" class="well">
        {getExpertEdition([inter], node)}
      </div>
    </>
  );
};
