import { getLang } from '../store';

class Messages {
  constructor() {
    this.lang = getLang();
  }
}

let messages = new Messages();
