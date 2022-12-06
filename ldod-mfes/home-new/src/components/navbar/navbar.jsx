import scssStyle from '../../scss/styles.scss?inline';
import style from './style.css?inline';
import 'bootstrap/js/dist/collapse';
export default () => {
  return (
    <>
      <style>
        {scssStyle}
        {style}
      </style>
      <nav class="navbar navbar-expand-md fixed-top">
        <div class="container-md">
          <a class="navbar-brand" href="#">
            Ldod Archive
          </a>
          <button
            class="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
      </nav>
      <div class="container-md">
        <div class="navbar-collapse collapse">
          <ul>
            <li>
              Language
            </li>
          </ul>
        </div>
      </div>
    </>
  );
};
