import { lazy, useState, useEffect, Suspense } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ scroll, language }) => {
  const Tutorials = lazy(() => import(`./Tutorials-${language}.jsx`));
  const [top, setTop] = useState(true);

  useEffect(() => {
    setTop(false);
  }, []);

  return (
    <Suspense fallback={<Loading />}>
      <Tutorials scroll={scroll} posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
