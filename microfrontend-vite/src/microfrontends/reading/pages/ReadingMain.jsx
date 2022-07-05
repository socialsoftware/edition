import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import Reading from './Reading';
import { readingStateSelector } from '../readingStore';

const Fragment = lazy(() => import('./ReadingFragment'));

export const rightArrowUrl = new URL(
  '../resources/assets/arrow-right.svg',
  import.meta.url
).href;

export const leftArrowUrl = new URL(
  '../resources/assets/arrow-left.svg',
  import.meta.url
).href;

export const xmlId = (data) => data.fragment.fragmentXmlId;

export const urlId = (data) => data.expertEditionInterDto.urlId;

export default ({ messages }) => {
  const experts = readingStateSelector('experts');
  const fragment = readingStateSelector('fragment');

  return (
    <>
      <div className="ldod-default row">
        <div className="main-content">
          <div className="row reading-grid">
            <Routes>
              <Route
                index
                element={<Reading messages={messages} experts={experts} />}
              />
              <Route
                path="/fragment/:xmlid/inter/:urlid"
                element={<Fragment messages={messages} experts={experts} fragment={fragment} />}
              />
            </Routes>
          </div>
        </div>
      </div>
    </>
  );
};
