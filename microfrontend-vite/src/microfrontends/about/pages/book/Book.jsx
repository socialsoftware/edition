import { lazy, useEffect, useState } from 'react';
import { getLanguage } from '../../../../store';
const image = new URL(
  `../../resources/assets/LiterarySimulation_BookCover.webp`,
  import.meta.url
).href
export default ({ scroll }) => {
  const [top, setTop] = useState(true);
  const Book = lazy(() => import(`./Book-${getLanguage()}.jsx`));

  useEffect(() => setTop(false));

  return <Book posY={top ? 0 : window.scrollY} image={image} />;
};
