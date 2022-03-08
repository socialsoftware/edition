import { Route, Routes, useParams } from "react-router-dom"
import { lazy, useEffect } from 'react';

const VirtualEdition = lazy(() => import('../page/VirtualEdition'));
const ExpertEdition = lazy(() => import('../page/ExpertEdition'));
const Category = lazy(() => import('../page/Category'));


export default ({messages}) => {
  const  params  = useParams();
  const acronym = params?.['*']?.split('/')?.[0]

  useEffect(() => {
    console.log(acronym);
  },[acronym])

  return (
    <div>
      <Routes>
        <Route
          path=":acronym"
          element={
            acronym?.startsWith('LdoD-') ? (
              <VirtualEdition
                acronym={acronym}
                messages={messages}
              />
            ) : (
              <ExpertEdition
                acronym={acronym}
                messages={messages}
              />
            )
          }
        />
        <Route path=":acronym/category/:category" element={<Category />} />
      </Routes>
    </div>
  );
}