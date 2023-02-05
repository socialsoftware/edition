/** @format */

export default root => {
	return (
		<div class="comment">
			<div class="comment-header">
				<div class="flex-5">
					<span class="comment-icon comment-user"></span>
					<span>{root.annotation?.data.username}</span>
				</div>
				<div actions class="flex-5">
					{root.isEditable && (
						<span class="comment-icon comment-trash" onClick={root.onRemove}></span>
					)}
				</div>
			</div>
			<div comment-body class="comment-body">
				<div id="content">
					{!root.annotation?.data.human && <p>{root.annotation.data.text}</p>}
				</div>
			</div>
			<div comment-footer class="comment-footer">
				{root.isEditable && (
					<>
						<button id="comment-save-btn" class="comment-btn" onClick={root.onSave}>
							Save
						</button>
						<button id="comment-close-btn" class="comment-btn" onClick={root.onClose}>
							Close
						</button>
					</>
				)}
			</div>
		</div>
	);
};
