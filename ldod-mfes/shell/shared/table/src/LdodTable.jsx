export class LdodTable extends HTMLElement {
  constructor() {
    super();
    const shadow = this.attachShadow({ mode: 'open' });
  }
  static get observedAttributes() {}
  connectedCallback() {
    this.shadowRoot.appendChild(
      <Table headers={this.headers} dataByCols={this.data} />
    );
  }
  attributeChangedCallback() {}
  disconnectedCallback() {}
}
!customElements.get('ldod-table') &&
  customElements.define('ldod-table', LdodTable);

const Table = ({ headers, dataByCols }) => {
  return (
    <div>
      <div></div>
      <div>
        <table>
          <thead>
            <tr>
              {headers?.map((header, index) => {
                return <th index={index}>{header}</th>;
              })}
            </tr>
          </thead>
          <tbody>
            {dataByCols?.map((col, index) => {
              return (
                <tr index={index}>
                  {headers?.map((header, index) => (
                    <td index={index}>{col[header]}</td>
                  ))}
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};
