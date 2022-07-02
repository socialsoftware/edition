import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ scroll, language }) => {
  const [top, setTop] = useState(true);
  const Team = lazy(() => import(`./Team-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);
  object;

  return (
    <Suspense fallback={<Loading />}>
      <Team scroll={scroll} posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
