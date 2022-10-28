export default ({ node, data }) => {
  return (
    <div id="generatedTopicsContainer">
      <ldod-table
        id="virtual-topicsTable"
        classes="table  table-hover"
        headers={node.constants.topicsHeaders}
        data-rows={data.topics.length}
        data={data.topics.map((topic, key) => {
          return {
            id: String(key),
            data: () => ({
              topics: <strong>{topic.name}</strong>,
              fragments: topic.inters.map((inter) => (
                <>
                  <em>{inter.title}</em>
                  {` (${inter.percentage}) `}
                </>
              )),
            }),
            search: JSON.stringify(topic),
          };
        })}
        constants={node.constants[node.language]}
        data-searchkey="id"></ldod-table>
    </div>
  );
};
