import { lazy, useState, useEffect } from 'react';
import { useStore } from '../../../../store';

export default ({messages}) => {
  const { language} = useStore();
  const Conduct = lazy(() => import(`./Conduct-${language}.jsx`))
  const [top, setTop] = useState(true);

  useEffect(() => setTop(false));
  return (
    <>
      <h1 className="text-center">{messages['header_conduct']}</h1>
      <p>&nbsp;</p>
      <Conduct posY={top ? 0 : window.scrollY}/>
    </>
  );
};
