import { lazy, useState, useEffect } from 'react';
import { getLanguage } from '../../../../store';

export default ({ messages }) => {
  const Conduct = lazy(() => import(`./Conduct-${getLanguage()}.jsx`));
  const [top, setTop] = useState(true);

  useEffect(() => setTop(false));
  return (
    <>
      <h1 className="text-center">
        {messages[getLanguage()]['header_conduct']}
      </h1>
      <p>&nbsp;</p>
      <Conduct posY={top ? 0 : window.scrollY} />
    </>
  );
};
