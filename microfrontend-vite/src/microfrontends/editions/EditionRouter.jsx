import { useParams } from 'react-router-dom';
import { lazy } from 'react';
import messages from './resources/constants';
import { useStore } from '../../store';
import { editionStore } from './editionStore';
import './resources/edition.css';

const VirtualEdition = lazy(() => import('./page/VirtualEdition'));
const ExpertEdition = lazy(() => import('./page/ExpertEdition'));
const selector = (sel) => (state) => state[sel];

export default () => {
  const language = useStore(selector('language'));
  const edition = editionStore(selector('edition'));
  const { acronym } = useParams();

  return (
    <div className="container" style={{marginBottom: "20px"}}>
      {acronym?.startsWith('LdoD-') ? (
        <VirtualEdition
          acronym={acronym}
          messages={messages?.[language]}
          edition={edition}
        />
      ) : (
        <ExpertEdition
          acronym={acronym}
          messages={messages?.[language]}
          edition={edition}
        />
      )}
    </div>
  );
};
