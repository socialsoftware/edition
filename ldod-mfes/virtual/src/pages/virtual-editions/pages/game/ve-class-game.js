import { LdodVeClassGame } from './ldod-ve-class-game';

const mount = async (lang, ref) => {
  document.querySelector(ref).appendChild(new LdodVeClassGame(lang));
};

const unMount = () => document.querySelector('ldod-ve-class-game')?.remove();

const path = '/:veId/game/:gameId';

export { mount, unMount, path };
