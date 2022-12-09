export default ({ node, title }) => {
  return (
    <h1 id="title" class="text-center flex-center">
      <div data-key="users">{title}</div>
      <span>&nbsp;({node.usersLength})</span>
    </h1>
  );
};
