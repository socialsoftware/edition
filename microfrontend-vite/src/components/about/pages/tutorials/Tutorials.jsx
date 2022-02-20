import { lazy, useState, useEffect } from 'react';
import { useStore } from "../../../../store";
import '../../../../resources/css/about-tutorials.css';

export default () => {
  
  const { language } = useStore();
  console.log(`./Tutorials-${language}.jsx`);
  const Tutorials = lazy(() => import(`./Tutorials-${language}.jsx`));
  const [top, setTop] = useState(true);

  useEffect(() => setTop(false));

  const scroll = (ref) => {
    const section = document.querySelector(ref);
    section.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };

  return (
    <Tutorials 
    scroll={(e, ref) => scroll(e, ref)}
    posY={top ? 0 : window.scrollY}/>
  );
}