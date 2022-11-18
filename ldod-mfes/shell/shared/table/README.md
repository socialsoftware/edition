# LdoD table

## Developed with vanilla javascript (custom element and Bootstap table CSS)

### Functionalities

- Search functionality activated by `data-searchkey` attribute;
- Renders first 20 rows and then on scroll in intervals of 20.

### Usage

```jsx
import 'path/to/shared/dist/table.js';

<ldod-table
  id="table-id"
  classes="table table-striped table-bordered"
  headers={headers}
  data={data}
  language="en"
  constants={constants}
  data-searchkey="externalId"></ldod-table>;
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
  - Description: Object keys have to match headers keys

- data-rows
  Type: String or Number
  Default: 20
  Description: number of visible rows pretended

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

### Properties

allRows - returns an array with all tr elements

searchedRows - returns an arrays with all tr elements that has `searched` attribute

unSearchedRows - returns the oppostie of the above property.

### Events

`ldod-table-searched` emitted when a searched action is performed. It passes the following data on detail object:

```js
{
  id: String, // id of the ldod-table
  size: Number, // number of matched rows
}
```

`ldod-table-increased` emitted when new rows are added to the table (scroll down action)

Note: both events have the options `composed` and `bubbles` to true
