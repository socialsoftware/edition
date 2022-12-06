export default ({ citationsTitle, numberOfCitations }) => {
  return (
    <h3
      id="title"
      data-key="title"
      data-args={numberOfCitations}
      class="text-center">
      {citationsTitle}
    </h3>
  );
};
