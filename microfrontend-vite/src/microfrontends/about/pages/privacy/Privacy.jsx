import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default () => {
  const [top, setTop] = useState(true);
  const Privacy = lazy(() => import(`./Privacy-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Privacy  posY={top ? 0 : window.scrollY} />;
};
