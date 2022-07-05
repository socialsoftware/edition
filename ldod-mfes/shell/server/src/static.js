import fs from 'fs';
import path from 'path';
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
  fs.cpSync(from, path.resolve(staticPath, name), {
    recursive: true,
    force: true,
  });
};

async function extractTarball(fileInfo, id, name) {
  fs.rmSync(`${fileInfo.destination}/${id}`, { recursive: true, force: true });
  fs.mkdirSync(`${fileInfo.destination}/${id}`, { recursive: true });
  fs.renameSync(
    fileInfo.path,
    `${fileInfo.destination}/${id}/${fileInfo.originalname}`
  );

  await tar.extract({
    cwd: `${fileInfo.destination}/${id}`,
    file: `${fileInfo.destination}/${name}/${fileInfo.originalname}`,
  });

  fs.rmSync(`./${fileInfo.destination}/${id}/${fileInfo.originalname}`);
}

export { extractTarball, addStaticAssets, removeStaticAssets };
