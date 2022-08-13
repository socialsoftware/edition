import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ scroll, language }) => {
  const [top, setTop] = useState(true);
  const Faq = lazy(() => import(`./Faq-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);
  return (
    <Suspense fallback={<Loading />}>
      <Faq scroll={scroll} posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
