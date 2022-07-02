import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const staticPath = `${path.dirname(fileURLToPath(import.meta.url))}/static`;

export const removeStaticAssets = ({ name }) => {
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

export const addStaticAssets = ({ from, name }) => {
  fs.cpSync(from, path.resolve(staticPath, name), {
    recursive: true,
    force: true,
  });
};
