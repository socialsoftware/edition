import { lazy, useEffect, useState } from 'react';
import { useStore } from '../../../../store';

export default ({scroll}) => {
  const { language } = useStore();
  const [top, setTop] = useState(true);
  const Videos = lazy(() => import(`./Videos-${language}.jsx`));

  useEffect(() => setTop(false));
  
  return (
    <Videos
      scroll={scroll}
      posY={top ? 0 : window.scrollY}
    />
  );
};
