import { lazy, useEffect, useState } from 'react';
import { useStore } from '../../../../store';

export default () => {
  const { language } = useStore();
  const [top, setTop] = useState(true);

  useEffect(() => setTop(false));

  const Archive = lazy(() => import(`./Archive-${language}.jsx`));

  return (
    <div className="ldod-default">
      <p>&nbsp;</p>
      <Archive posY={top ? 0 : window.scrollY} />
    </div>
  );
};
