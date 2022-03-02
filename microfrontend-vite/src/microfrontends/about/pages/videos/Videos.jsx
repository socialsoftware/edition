import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default ({ scroll }) => {
  const [top, setTop] = useState(true);
  const Videos = lazy(() => import(`./Videos-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Videos scroll={scroll} posY={top ? 0 : window.scrollY} />;
};
