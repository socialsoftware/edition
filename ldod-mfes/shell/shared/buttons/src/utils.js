export const html = String.raw;

export function dispatchCustomEvent(
  node,
  payload,
  { type, bubbles, composed }
) {
  node.dispatchEvent(
    new CustomEvent(`ldod-${type}`, {
      detail: payload,
      bubbles,
      composed,
    })
  );
}
