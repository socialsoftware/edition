export default ({ info }) => (
  <div className="bottom-info font-monospace">
    <img
      className="hidden-xs"
      src={new URL('./resources/assets/logotipos.webp', import.meta.url).href}
      width="100%"
      alt="logotipos"
    />
    <img
      className="visible-xs-inline "
      src={new URL('./resources/assets/logotiposm.webp', import.meta.url).href}
      width="100%"
      alt="logotipos"
    />
    <br />
    <br />
    <br />
    {info}
  </div>
);
