import { lazy, Suspense } from 'react';
import Loading from '../../../../shared/Loading';

export default ({ language }) => {
  const Copyright = lazy(() => import(`./Copyright-${language}.jsx`));

  return (
    <>
      <h1 className="text-center">Copyright</h1>
      <p>&nbsp;</p>
      <Suspense fallback={<Loading />}>
        <Copyright />
      </Suspense>
    </>
  );
};
