import messages from '../resources/constants';
import { setError, storeStateSelector, useStore } from '../store';
import { useEffect} from 'react';


export default () => {
  const language = storeStateSelector('language');
    
  useEffect(() => {
    return () => setError();
  },[])
  
  return (
  <div className="container">
        {messages[language]['pagenotfound_message']}
  </div>
);
  }
