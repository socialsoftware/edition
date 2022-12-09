import selectedVeSchema from './selecte-ve.schema.json';
export let selectedInters = ['LdoD-Arquivo', 'LdoD-Mallet', 'LdoD-Twitter'];

export let ldodEventBus;
function selectedVeHandler({ payload }) {
  selectedInters = payload.selected
    ? [...selectedInters, payload.name]
    : selectedInters.filter((ed) => ed !== payload.name);
}

if (typeof window !== 'undefined') {
  ldodEventBus = (await import('shared/ldod-events.js')).ldodEventBus;
  ldodEventBus.register('virtual:selected-ve', selectedVeSchema);
  ldodEventBus.subscribe('virtual:selected-ve', selectedVeHandler);
}

export const ldodEventPublisher = (name, payload) => {
  ldodEventBus?.publish(`ldod:${name}`, payload);
};
