import { lazy } from 'react';
import { getLanguage } from '../../../../store';

export default () => {
  const Copyright = lazy(() => import(`./Copyright-${getLanguage()}.jsx`));

  return (
    <>
      <h1 className="text-center">Copyright</h1>
      <p>&nbsp;</p>
      <Copyright scroll={scroll} posY={top ? 0 : window.scrollY} />
    </>
  );
};
