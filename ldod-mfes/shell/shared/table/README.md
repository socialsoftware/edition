# LdoD table

## Developed with vanilla javascript (custom element and Bootstap table CSS)

### Functionalities

- Search functionality activated by `data-searchkey` attribute;
- Renders first 20 rows and then on scroll in intervals of 20.

### Usage

```jsx
<ldod-table
  id="table-id"
  classes="table table-striped table-bordered"
  headers={headers}
  data={data}
  language="en"
  constants={constants}
  data-searchkey="externalId"></ldod-table>
```

### Attributes

####

- classes

  - Type: String
  - Description: According table CSS from bootstrap

- headers

  - Type: Array of Strings (keys for constants)
  - Descriptions: Columns keys

- data

  - Type: Array of objects
  - Descriptions: Object keys have to match headers keys

- language
  - Type: String
  - Description: On language change all the table elements with data-key attribute will be change textContext

```js
this.querySelectorAll('[data-key]').forEach((ele) => {
  ele.textContent = this.getConstants(ele.dataset.key);
});
```

- constants

  - Type: Object
  - Description: Object with language keys and properties

```js
const constants = {
  en: {
    name: 'Name',
    age: 'Age',
  },
  pt: {
    name: 'Nome',
    age: 'Idade',
  },
  ...
};
```

- data-searchkey (optional)
  - Type: String
  - Description:
    - Activates search functionality on table;
    - Property from data objects (unique) which by it is possible to indentify each of the rows for search purposes
