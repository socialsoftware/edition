import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default ({ scroll }) => {
  const [top, setTop] = useState(true);
  const Articles = lazy(() => import(`./Articles-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Articles scroll={scroll} posY={top ? 0 : window.scrollY} />;
};
