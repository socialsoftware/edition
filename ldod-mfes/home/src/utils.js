export const isMFEAvailable = (mfe) => {
  if (!window.mfes) return true;
  return window.mfes.indexOf(mfe) !== -1;
};

export const checkUserCompCompliance = async () => {
  let isCompliant;
  await import('user').then(() => {
    let userComponent = customElements.get('user-component');
    let userComponentElement = new userComponent();
    isCompliant = userComponentElement instanceof HTMLLIElement;
  });
  return isCompliant;
};
