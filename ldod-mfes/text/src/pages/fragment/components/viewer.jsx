import { isDev } from '../../edition/edition-router';
import navImages from './nav-images';

const base = 'http://ldod.uc.pt/facs/';
const isFullScreen = () => document.fullscreenElement;
const isFullScreenSupported = () => document.exitFullscreen;

const loadOpenSeadragon = async (obj) => {
  const OpenSeadragon = await import('openseadragon');
  const viewer = new OpenSeadragon.Viewer({
    ...obj,
  });

  viewer.setFullScreen = function (bool) {
    if (!isFullScreenSupported()) return this.setFullPage(bool);
    if (!isFullScreen()) return this.container.requestFullscreen();
    document.exitFullscreen();
  };

  viewer.addHandler(
    'page',
    document.querySelector('ldod-fragment').handleChangeFac
  );
};

export default ({ surfaceList }) => {
  const element = <div id="fac-viewer" style={{ height: '554px' }}></div>;
  loadOpenSeadragon({
    element,
    tileSources: surfaceList.map((imgPath) => ({
      type: 'image',
      url: base.concat(imgPath.graphic),
    })),
    prefixUrl: isDev() ? '/' : `${import.meta.env.VITE_NODE_HOST}/text/`,
    sequenceMode: true,
    showNavigator: true,
    navImages,
    autoHideControls: false,
    visibilityRatio: 1,
    constrainDuringPan: true,
    showRotationControl: true,
    showFullPageControl: true,
  });

  return element;
};
