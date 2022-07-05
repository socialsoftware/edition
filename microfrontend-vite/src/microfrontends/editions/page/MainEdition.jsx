import { Route, Routes, useParams } from "react-router-dom"
import { lazy } from 'react';

const VirtualEdition = lazy(() => import('../page/VirtualEdition'));
const ExpertEdition = lazy(() => import('../page/ExpertEdition'));
const Category = lazy(() => import('../page/Category'));
const Taxonomies = lazy(() => import('./Taxonomies'));

const isVirtualAcronym = (acronym) => acronym?.startsWith('LdoD-');
const getCurrentAcronym = (params) => params['*']?.split('/')?.[0]

export default ({messages}) => {
  const  params  = useParams();

  return (
    <div>
      <Routes>
        <Route
          path=":acronym"
          element={
            isVirtualAcronym(getCurrentAcronym(params)) ? (
              <VirtualEdition
                acronym={getCurrentAcronym(params)}
                messages={messages}
              />
            ) : (
              <ExpertEdition messages={messages} />
            )
          }
        />
        <Route path=":acronym/category/:category" element={<Category messages={messages}/>} />
        <Route path=":acronym/taxonomy" element={<Taxonomies messages={messages}/>} />
      </Routes>
    </div>
  );
}

