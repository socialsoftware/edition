import fs from 'fs';
import path, { resolve } from 'path';
import tar from 'tar';
import { staticPath } from './constants.js';

const removeStaticAssets = ({ name }) => {
  fs.readdirSync(staticPath).forEach((dir) => {
    if (dir === name) {
      fs.rmSync(path.resolve(staticPath, name), {
        recursive: true,
        force: true,
      });
      return;
    }
  });
};

const addStaticAssets = ({ from, name }) => {
  const dest = path.resolve(staticPath, name);
  fs.cpSync(from, dest, {
    recursive: true,
    force: true,
  });
};

async function extractTarball(fileInfo, id) {
  const dest = resolve(fileInfo.destination, id);
  const source = resolve(fileInfo.destination, id, fileInfo.originalname);
  fs.rmSync(dest, { recursive: true, force: true });
  fs.mkdirSync(dest);
  fs.renameSync(fileInfo.path, source);

  await tar.extract({
    cwd: dest,
    file: source,
  });

  fs.rmSync(resolve(fileInfo.destination, id, fileInfo.originalname));
}

const getIndexHtml = (path = staticPath) => {
  try {
    return fs.readFileSync(resolve(path, 'index.html'), 'utf8');
  } catch (error) {
    return;
  }
};

export { extractTarball, addStaticAssets, removeStaticAssets, getIndexHtml };
