import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ language }) => {
  const [top, setTop] = useState(true);
  const Ack = lazy(() => import(`./Ack-${language}.jsx`));

  console.log('teste');

  useEffect(() => {
    setTop(false);
  }, []);
  return (
    <Suspense fallback={<Loading />}>
      <Ack posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
