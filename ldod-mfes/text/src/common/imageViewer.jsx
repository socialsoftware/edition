import PhotoSwipeLightbox from 'photoswipe/lightbox';
import PhotoSwipe from 'photoswipe';
import style from 'photoswipe/style.css?inline';
document.querySelector('#textContainer').appendChild(<style>{style}</style>);
export const imageViewer = (gallery) => {
  const lightbox = new PhotoSwipeLightbox({
    gallery,
    children: 'a',
    pswpModule: PhotoSwipe,
  });
  lightbox.init();
};
