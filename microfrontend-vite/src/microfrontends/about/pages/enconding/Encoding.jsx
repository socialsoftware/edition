import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default () => {
  const [top, setTop] = useState(true);
  const Encoding = lazy(() => import(`./Encoding-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Encoding posY={top ? 0 : window.scrollY} />;
};
