import { lazy, useEffect, useState } from 'react';
import { useStore } from '../../../../store';

export default () => {
  const { language } = useStore();
  const [top, setTop] = useState(true);
  const Videos = lazy(() => import(`./Videos-${language}.jsx`));

  useEffect(() => setTop(false));

  const scroll = (e, ref) => {
    e.preventDefault();
    const section = document.querySelector(ref);
    section.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };
  return (
    <Videos
      scroll={(e, ref) => scroll(e, ref)}
      posY={top ? 0 : window.scrollY}
    />
  );
};
