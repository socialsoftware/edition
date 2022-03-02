import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';

export default ({ scroll }) => {
  const [top, setTop] = useState(true);
  const Faq = lazy(() => import(`./Faq-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Faq scroll={scroll} posY={top ? 0 : window.scrollY} />;
};
