export default ({ numberOfFragments, title }) => {
  return (
    <h3
      id="title"
      data-key="manageFragments"
      data-args={numberOfFragments}
      class="text-center">
      {title}
    </h3>
  );
};
