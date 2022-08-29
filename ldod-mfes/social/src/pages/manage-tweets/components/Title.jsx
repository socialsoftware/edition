export default ({ numberOfCitations, title }) => {
  return (
    <h3 data-key="title" data-args={numberOfCitations} class="text-center">
      {title}
    </h3>
  );
};
