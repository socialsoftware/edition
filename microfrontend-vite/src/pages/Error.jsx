import { storeStateSelector } from '../store';
import messages from '../resources/constants';
export default () => {
const language = storeStateSelector('language');
  
  return (
    <>
      <p className="error">{messages[language].error}</p>
    </>
  );
};
