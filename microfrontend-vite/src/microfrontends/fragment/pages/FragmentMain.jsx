import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { userSelectedVE } from '../../../store';
import { getNoAuthFragment } from '../api/fragment';
import Fragment from '../components/Fragment';
import FragmentNav from '../components/FragmentNav';
import {
  fragmentStateSelector, setAuthorialsInter, setFragmentInter, setFragmentNavData, setSelectedVE
} from '../fragmentStore';

export default ({ messages, language }) => {
  const fragmentNavData = fragmentStateSelector('fragmentNavData');
  const { xmlid } = useParams();

  useEffect(() => {
    setAuthorialsInter();
    setSelectedVE(userSelectedVE());
    xmlid && getNoAuthFragment(xmlid);
    return () => {
      setFragmentNavData();
      setFragmentInter();
      setSelectedVE([]);
    };
  }, []);

  return (
    <div style={{ marginBottom: '30px' }}>
      {fragmentNavData && (
        <>
          <div className="col-md-9">
            <Fragment fragment={fragmentNavData} />
          </div>
          <FragmentNav
            messages={messages}
            language={language}
            fragmentNavData={fragmentNavData}
          />
        </>
      )}
    </div>
  );
};
