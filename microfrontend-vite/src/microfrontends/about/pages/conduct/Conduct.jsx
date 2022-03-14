import { lazy, useState, useEffect } from 'react';

export default ({ messages, language }) => {
  const Conduct = lazy(() => import(`./Conduct-${language}.jsx`));
  const [top, setTop] = useState(true);

  useEffect(() => setTop(false));
  return (
    <>
      <h1 className="text-center">
        {messages[language]['header_conduct']}
      </h1>
      <p>&nbsp;</p>
      <Conduct posY={top ? 0 : window.scrollY} />
    </>
  );
};
