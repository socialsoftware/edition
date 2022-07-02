import { lazy, useState, useEffect, Suspense } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ messages, language }) => {
  const Conduct = lazy(() => import(`./Conduct-${language}.jsx`));
  const [top, setTop] = useState(true);

  useEffect(() => {
    setTop(false);
  }, []);
  return (
    <>
      <h1 className="text-center">{messages[language]['header_conduct']}</h1>
      <p>&nbsp;</p>
      <Suspense fallback={<Loading />}>
        <Conduct posY={top ? 0 : window.scrollY} />
      </Suspense>
    </>
  );
};
