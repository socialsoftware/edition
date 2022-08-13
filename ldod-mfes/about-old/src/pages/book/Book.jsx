import { lazy, Suspense, useEffect, useState } from 'react';
import Loading from '../../../../shared/Loading';
const image = new URL(
  `../../resources/assets/LiterarySimulation_BookCover.webp`,
  import.meta.url
).href;
export default ({ language }) => {
  const [top, setTop] = useState(true);
  const Book = lazy(() => import(`./Book-${language}.jsx`));

  useEffect(() => {
    setTop(false);
  }, []);

  return (
    <Suspense fallback={<Loading />}>
      <Book posY={top ? 0 : window.scrollY} image={image} />
    </Suspense>
  );
};
