export default ({ args, title, key }) => {
  return (
    <>
      <h3 id="title" data-key={key} data-args={args} class="text-center">
        {title}
      </h3>
    </>
  );
};
