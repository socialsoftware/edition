import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ language }) => {
  const [top, setTop] = useState(true);
  const Encoding = lazy(() => import(`./Encoding-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);

  return (
    <Suspense fallback={<Loading />}>
      <Encoding posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
