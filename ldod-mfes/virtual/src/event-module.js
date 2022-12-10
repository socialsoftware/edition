export let selectedInters = ['LdoD-Arquivo', 'LdoD-Mallet', 'LdoD-Twitter'];

export let ldodEventPublisher;
export let ldodEventSubscriber;

function selectedVeHandler({ payload }) {
  selectedInters = payload.selected
    ? [...selectedInters, payload.name]
    : selectedInters.filter((ed) => ed !== payload.name);
}

if (typeof window !== 'undefined') {
  await import('shared/ldod-events.js').then((module) => {
    ldodEventPublisher = module.ldodEventPublisher;
    ldodEventSubscriber = module.ldodEventSubscriber;
  });
  ldodEventSubscriber('selected-ve', selectedVeHandler);
}
