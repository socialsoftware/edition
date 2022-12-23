export default ({ node, checkboxes }) => {
  const handleOnChange = ({ target }) => {
    target.toggleAttribute('checked');
    node.handleTranscriptionCheckboxChange();
  };
  return (
    <div id="text-checkBoxesContainer" class="interCheckboxes">
      {Object.entries(node.transcriptionCheckboxes).map(
        ([cb, checked], index) => {
          return (
            <>
              {checkboxes.includes(cb) && (
                <div id={cb} key={index} class="">
                  <input
                    id="checkboxTranscript"
                    type="checkbox"
                    name={cb}
                    checked={checked}
                    onChange={handleOnChange}
                  />
                  <label data-key={cb} for={`checkbox-${cb}`}>
                    {node.getConstants(cb)}
                  </label>
                </div>
              )}
            </>
          );
        }
      )}
    </div>
  );
};
