import { useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
// TODO: Fecthing from BE server in a MFE decoupled architecture
import { userSelectedVE } from '../../../store';
import { getFragmentInter } from '../api/fragment';
import FragmentInter from '../components/FragmentInter';
import FragmentInterLine from '../components/FragmentInterLineByLine';
import FragmentInterSide from '../components/FragmentInterSideBySide';
import FragmentInterVirtual from '../components/FragmentInterVirtual';
import FragmentNav from '../components/FragmentNav';
import { isLineByLine, isSideBySide, isVirtual } from '../dataExtraction';
import {
  fragmentStateSelector,
  resetCheckboxesState,
  setFragmentInter,
  setFragmentNavData,
  setSelectedVE
} from '../fragmentStore';

const getVariationsHeaders = (fragmentInter) =>
  fragmentInter.inters?.map(({ shortName, title }) => ({
    shortName,
    title,
  }));

export default ({ messages }) => {
  const { xmlid, urlid } = useParams();
  const { state } = useLocation();
  const fragmentNavData = fragmentStateSelector('fragmentNavData');
  const fragmentInter = fragmentStateSelector('fragmentInter');
  const checkboxes = fragmentStateSelector('checkboxesState');

  useEffect(() => {
    setSelectedVE(userSelectedVE());
    checkboxes &&
      xmlid &&
      urlid &&
      getFragmentInter(xmlid, urlid, state);
  }, [urlid, messages, checkboxes, state]);

  useEffect(() => {
    return () => {
      setSelectedVE();
      setFragmentInter();
      setFragmentNavData();
      resetCheckboxesState();
    };
  }, []);


  return (
    <div>
      {fragmentInter && (
        <>
          <div className="col-md-9" style={{ marginBottom: '40px' }}>
            <div id="fragment-inter" className="row">
              {isVirtual(fragmentInter) ? (
                <FragmentInterVirtual
                  messages={messages}
                  fragmentInter={fragmentInter}
                />
              ) : (
                <>
                  {isSideBySide(fragmentInter) ? (
                    <FragmentInterSide
                      messages={messages}
                      fragmentInter={fragmentInter}
                      getVariationsHeaders={getVariationsHeaders}
                    />
                  ) : (
                    <>
                      {isLineByLine(fragmentInter) ? (
                        <>
                          <FragmentInterLine
                            messages={messages}
                            fragmentInter={fragmentInter}
                            getVariationsHeaders={getVariationsHeaders}
                          />
                        </>
                      ) : (
                        <FragmentInter
                          messages={messages}
                          fragmentInter={fragmentInter}
                        />
                      )}
                    </>
                  )}
                </>
              )}
            </div>
          </div>
          <FragmentNav messages={messages} fragmentNavData={fragmentNavData} />
        </>
      )}
    </div>
  );
};
