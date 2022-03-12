import messages from './resources/constants';
import { setError, useStore } from '../../store';
import { useEffect} from 'react';


export default () => {
  
  useEffect(() => {
    return () => setError();
  },[])
  
  return (
  <div className="container">
        {messages?.[useStore().language]['pagenotfound_message']}
  </div>
);
  }
