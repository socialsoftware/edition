import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ scroll, language }) => {
  const [top, setTop] = useState(true);
  const Articles = lazy(() => import(`./Articles-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);

  return (
    <Suspense fallback={<Loading />}>
      <Articles scroll={scroll} posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
