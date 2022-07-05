import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ language }) => {
  const [top, setTop] = useState(true);
  const Privacy = lazy(() => import(`./Privacy-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);
  return (
    <Suspense fallback={<Loading />}>
      <Privacy posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
