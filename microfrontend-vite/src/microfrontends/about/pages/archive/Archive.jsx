import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ language }) => {
  const [top, setTop] = useState(true);

  useEffect(() => {
    setTop(false);
  }, []);

  const Archive = lazy(() => import(`./Archive-${language}.jsx`));

  return (
    <div className="ldod-default">
      <p>&nbsp;</p>
      <Suspense fallback={<Loading />}>
        {' '}
        <Archive posY={top ? 0 : window.scrollY} />
      </Suspense>
    </div>
  );
};
