/**
 * 
 * @param {boolean} isLoading 
 * @param {object} options
 * @param {boolean} options.bubbles
 * @param {boolean} options.composed
 * @returns CustomEvent
 */
export const loadingEvent = (isLoading, options = {}) => {
  return new CustomEvent("ldod-loading", { detail: { isLoading }, ...options })
}

/**
 * 
 * @param {string} token 
 * @param {object} options
 * @param {boolean} options.bubbles
 * @param {boolean} options.composed
 * @returns CustomEvent
 */
export const logoutEvent = (token, options = {}) => {
  return new CustomEvent("ldod-logout", { detail: { token }, ...options })
}

/**
 * 
 * @param {string} message 
 * @param {object} options
 * @param {boolean} options.bubbles
 * @param {boolean} options.composed
 * @returns CustomEvent
 */
export const errorEvent = (message, options = {}) => {
  return new CustomEvent("ldod-error", { detail: { message }, ...options })
}


/**
 * 
 * @param {string} message 
 * @param {object} options
 * @param {boolean} options.bubbles
 * @param {boolean} options.composed
 * @returns CustomEvent
 */
export const messageEvent = (message, options = {}) => {
  return new CustomEvent("ldod-message", { detail: { message }, ...options })
}