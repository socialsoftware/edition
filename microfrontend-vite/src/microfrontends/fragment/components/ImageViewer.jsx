import OpenSeadragon from 'openseadragon';
import { useEffect, useState } from 'react';
import { fragmentStateSelector, setCheckboxesState } from '../fragmentStore';

const baseURL = 'https://ldod.uc.pt/facs';
const openImages = '../../../../viewer-images/';

export default ({ surface }) => {
  const [viewer, setViewer] = useState();
  const cb = fragmentStateSelector('checkboxesState')

  useEffect(() => {
    setViewer(initViewer());
    return () => viewer && viewer.destroy();
  }, [cb.pbText]);



  const handleChangeImage = (id) => {
    viewer && viewer.destroy();
    setCheckboxesState('pbText', id);
  };

  const initViewer = () =>
    OpenSeadragon({
      id: 'fac-viewer',
      tileSources: { url: `${baseURL}/${surface.graphic}`, type: 'image' },
      prefixUrl: openImages,
      animationTime: 0.5,
      blendTime: 0.1,
      constrainDuringPan: true,
      maxZoomPixelRatio: 2,
      minZoomLevel: 1,
      visibilityRatio: 0.5,
      zoomPerScroll: 2,
      showNavigator: true,
      autoHideControls: false,
    });

  return (
    <div className="col-md-6" style={{ marginBottom: '20px' }}>
      {(surface?.prevGraphic || surface?.prevPb) && (
        <div title="Previous image">
          <button
            className="button-arrow prev-fac"
            onClick={() => handleChangeImage(surface?.prevPb)}
          >
            <span className="glyphicon glyphicon-arrow-left arrow-image"></span>
          </button>
        </div>
      )}
      {(surface?.nextGraphic || surface?.nextPb) && (
        <div title="Next image">
          <button
            className="button-arrow next-fac"
            onClick={() => handleChangeImage(surface?.nextPb)}
          >
            <span className="glyphicon glyphicon-arrow-right arrow-image"></span>
          </button>
        </div>
      )}
      <div id="fac-viewer" className="image-viewer" />
    </div>
  );
};
