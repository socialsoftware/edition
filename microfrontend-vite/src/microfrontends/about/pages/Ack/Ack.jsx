import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default ({ scroll }) => {
  const [top, setTop] = useState(true);
  const Ack = lazy(() => import(`./Ack-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Ack scroll={scroll} posY={top ? 0 : window.scrollY} />;
};
