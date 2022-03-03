import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getNoAuthFragment, getNoAuthVirtualFragment } from '../api/fragment';
import Fragment from '../components/Fragment';
import { fragmentStore } from '../fragmentStore';
const selector = (sel) => (state) => state[sel];

export default ({ messages, language }) => {
  const fragment = fragmentStore(selector('fragment'));
  const virtualFragment = fragmentStore(selector('virtualFragment'));
  const { xmlid } = useParams();

  useEffect(() => {
    getNoAuthFragment(xmlid);
    getNoAuthVirtualFragment(xmlid);
  }, []);

  return (
    <div>
      <Fragment
        messages={messages}
        language={language}
        frag={fragment}
        virtualFrag={virtualFragment}
      />
    </div>
  );
};
