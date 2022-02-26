import { lazy, useState, useEffect } from 'react';
import { useStore } from "../../../../store";

export default ({scroll}) => {
  
  const { language } = useStore();
  const Tutorials = lazy(() => import(`./Tutorials-${language}.jsx`));
  const [top, setTop] = useState(true);

  useEffect(() => setTop(false));

  return (
    <Tutorials 
    scroll={scroll}
    posY={top ? 0 : window.scrollY}/>
  );
}