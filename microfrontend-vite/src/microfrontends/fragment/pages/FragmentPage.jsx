import { useEffect, useState } from 'react';
import { Route, Routes, useParams } from 'react-router-dom';
import { getNoAuthFragment } from '../api/fragment';
import FragmentNav from '../components/FragmentNav';
import FragmentSource from '../components/FragmentSource';
import Fragment from '../components/Fragment';
import { fragmentStore } from '../fragmentStore';
import FragmentExpert from '../components/FragmentExpert';
import FragmentVirtual from '../components/FragmentVirtual';
const selector = (state) => state.fragmentNavData;

export default ({ messages, language }) => {
  const [mounted, isMounted] = useState();
  const { xmlid } = useParams();

  const fragmentNavData = fragmentStore(selector);

  useEffect(() => {
    getNoAuthFragment(xmlid);
  }, []);

  useEffect(() => {
    isMounted(fragmentNavData && true);
  }, [fragmentStore()]);

  return (
    <>
      {mounted && (
        <div style={{ marginBottom: '30px' }}>
          <Routes>
            <Route
              index
              element={
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
              }
            />
            <Route
              path="/inter/:urlid"
              element={
                <>
                  <FragmentSource messages={messages} language={language} />
                  <FragmentNav
                    messages={messages}
                    language={language}
                    fragmentNavData={fragmentNavData}
                  />
                </>
              }
            />
            <Route
              path="/expert/:urlid"
              element={
                <>
                  <FragmentExpert messages={messages} language={language} />
                  <FragmentNav
                    messages={messages}
                    language={language}
                    fragmentNavData={fragmentNavData}
                  />
                </>
              }
            />
            <Route
              path="/virtual/:urlid"
              element={
                <>
                  <FragmentVirtual messages={messages} language={language} />
                  <FragmentNav
                    messages={messages}
                    language={language}
                    fragmentNavData={fragmentNavData}
                  />
                </>
              }
            />
          </Routes>
        </div>
      )}
    </>
  );
};
