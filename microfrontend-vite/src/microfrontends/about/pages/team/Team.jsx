import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default ({ scroll }) => {
  const [top, setTop] = useState(true);
  const Team = lazy(() => import(`./Team-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Team scroll={scroll} posY={top ? 0 : window.scrollY} />;
};
