import { useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import { getNoAuthFragmentInter } from '../api/fragment';
import FragmentNav from '../components/FragmentNav';
import FragmentInter from '../components/FragmentInter';
import FragmentInterSide from '../components/FragmentInterSideBySide';
import FragmentInterVirtual from '../components/FragmentInterVirtual';
import FragmentInterLine from '../components/FragmentInterLineByLine';

import {
  fragmentStore,
  resetCheckboxesState,
  setFragmentNavData,
  setFragmentInter,
  setAuthorialsInter,
} from '../fragmentStore';
const selector = (sel) => (state) => state[sel];

export default ({ messages, language }) => {
  const { xmlid, urlid } = useParams();
  const { state } = useLocation();
  const fragmentNavData = fragmentStore(selector('fragmentNavData'));
  const fragmentInter = fragmentStore(selector('fragmentInter'));
  const checkboxes = fragmentStore(selector('checkboxesState'));

  useEffect(() => {
    if (checkboxes && xmlid && urlid) {
      !state && getNoAuthFragmentInter(xmlid, urlid);
      state && getNoAuthFragmentInter(xmlid, urlid, state);
    }
  }, [urlid, language, checkboxes, state]);

  useEffect(() => {
    return () => {
      setFragmentInter();
      setFragmentNavData();
      resetCheckboxesState();
    };
  }, []);

  const isVirtual = fragmentInter?.type === 'VIRTUAL';
  const isSide = fragmentInter?.transcriptType === 'SIDE';
  const isLine = fragmentInter?.transcriptType === 'LINE';

  return (
    <div>
      {fragmentInter && (
        <>
          <div className="col-md-9" style={{ marginBottom: '40px' }}>
            <div id="fragment-inter" className="row">
              {isVirtual ? (
                <FragmentInterVirtual
                  messages={messages}
                  language={language}
                  fragmentInter={fragmentInter}
                />
              ) : (
                <>
                  {isSide ? (
                    <FragmentInterSide
                      messages={messages}
                      language={language}
                      fragmentInter={fragmentInter}
                    />
                  ) : (
                    <>
                      {isLine ? (
                        <>
                          <FragmentInterLine 
                          messages={messages}
                          language={language}
                          fragmentInter={fragmentInter}
                          />
                        </>
                      ) : (
                        <FragmentInter
                          messages={messages}
                          language={language}
                          fragmentInter={fragmentInter}
                        />
                      )}
                    </>
                  )}
                </>
              )}
            </div>
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
