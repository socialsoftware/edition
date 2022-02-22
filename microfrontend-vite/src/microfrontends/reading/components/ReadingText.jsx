export default ({ text, title }) => {
  return (
    <div className="reading__text col-xs-12 no-pad style-point reading-book-title">
      <h1>{title}</h1>
      <div dangerouslySetInnerHTML={{ __html: text }} />
    </div>
  );
};
