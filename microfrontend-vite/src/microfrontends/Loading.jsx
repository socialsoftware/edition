import { messages } from '../resources/constants';
import { getLanguage } from '../store';

export default () => (
  <div className="container">
        {messages[getLanguage()]['loading']}
  </div>
);
