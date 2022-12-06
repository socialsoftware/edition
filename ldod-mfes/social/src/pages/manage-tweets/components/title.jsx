export default ({ numberOfCitations, title }) => {
  return (
    <h3
      id="title"
      data-key="title"
      data-args={numberOfCitations}
      class="text-center">
      {title}
    </h3>
  );
};
