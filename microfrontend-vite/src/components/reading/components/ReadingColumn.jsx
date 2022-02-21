export default ({ editor }) => {
  const rightArrowUrl = new URL(
    '../../../resources/assets/arrow-right.svg',
    import.meta.url
  ).href;
  return (
    <div
      className="reading__column col-xs-12 col-sm-1 no-pad"
      style={{ height: '162px' }}
    >
      <h4>
        <a href="/reading/edition/TSC/start/">{editor}</a>
      </h4>
      <a href="/reading/edition/TSC/start/">
        <img src={rightArrowUrl} />
      </a>
    </div>
  );
};
