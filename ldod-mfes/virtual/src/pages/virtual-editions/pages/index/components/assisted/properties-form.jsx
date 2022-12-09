import style from './properties-style.css?inline';
import { useProperties } from './assisted-table';
import { setLinearVE } from '@src/restricted-api-requests';

const getRangeInputs = (node) =>
  Array.from(node.shadowRoot.querySelectorAll('input[type="range"]'));

const onChangeProperty = async (node) => {
  let props = getRangeInputs(node).map((input) => ({
    type: input.name,
    weight: input.value,
  }));

  node.properties.set(props);
  const data = await setLinearVE(node.parent.edition.externalId, {
    selected: node.parent.selected,
    properties: node.properties.get(node.parent.edition.acronym),
  });
  node.parent.updateData(data);
};

const getForm = (node, parent) => {
  return (
    <form>
      <h4 class="text-center">{parent.getConstants('criteria')}</h4>
      <div
        style={{
          display: 'grid',
          gap: '20px',
          margin: '20px',
          justifyContent: 'space-evenly',
          gridTemplateColumns: 'auto auto auto auto',
        }}>
        {['heteronym', 'date', 'text', 'taxonomy'].map((prop) => (
          <div class=" text-center">
            <label for={`range-${prop}`}>{parent.getConstants(prop)}</label>
            <input
              onChange={() => onChangeProperty(node)}
              id={`range-${prop}`}
              name={prop}
              type="range"
              max="1"
              min="0"
              step="0.2"
              value={`${parent.properties?.filter(({ type }) => prop === type)[0]
                ?.weight || 0
                }`}
            />
          </div>
        ))}
      </div>
    </form>
  );
};

export class PropertiesForm extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
  }

  static get observedAttributes() { }

  get properties() {
    const [get, set] = useProperties();
    return {
      get,
      set,
    };
  }

  connectedCallback() {
    this.properties.set();
    this.shadowRoot.appendChild(<style>{style}</style>);
    this.shadowRoot.appendChild(getForm(this, this.parent));
  }
  attributeChangedCallback() { }
  disconnectedCallback() { }
}
!customElements.get('properties-form') &&
  customElements.define('properties-form', PropertiesForm);
