import { lazy, useEffect, useState, Suspense } from 'react';

export default ({ scroll, language }) => {
  const [top, setTop] = useState(true);
  const Videos = lazy(() => import(`./Videos-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);

  return (
    <Suspense>
      <Videos scroll={scroll} posY={top ? 0 : window.scrollY} />
    </Suspense>
  );
};
