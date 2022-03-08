import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getNoAuthFragment } from '../api/fragment';
import FragmentNav from '../components/FragmentNav';
import Fragment from '../components/Fragment';
import { fragmentStore,  setFragmentNavData, setFragmentInter, setAuthorialsInter, setSelectedVE, getVirtualEditionsAcronyms } from '../fragmentStore';
import { userSelectedVE } from '../../../store';
const selector = (state) => state.fragmentNavData;

export default ({ messages, language }) => {
  const fragmentNavData = fragmentStore(selector);
  const { xmlid } = useParams();

  useEffect(() => {
    setAuthorialsInter()
    setSelectedVE(userSelectedVE());
    xmlid && getNoAuthFragment(xmlid);
    console.log(getVirtualEditionsAcronyms());
    return () => {
      setFragmentNavData();
      setFragmentInter();
      setSelectedVE([]);
    }
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
