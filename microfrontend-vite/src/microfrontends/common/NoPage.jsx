import messages from './resources/constants';
import { useStore } from '../../store';

export default () => (
  <div className="container">
        {messages?.[useStore().language]['pagenotfound_message']}
  </div>
);
