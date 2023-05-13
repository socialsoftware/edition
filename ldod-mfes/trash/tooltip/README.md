# LdoD Tooltip

## Developed with vanilla javascript (custom element) and [Popper](https://popper.js.org/).

### Usage

```js
import 'path/to/shared/dist/tooltip.js';

<button tooltip-ref="switch-button"></button> // element to be tooltipped

<ldod-tooltip
  placement="top"
  data-ref="[tooltip-ref='switch-button']"
  content="Some content"></ldod-tooltip>;
```

### Attributes

- placement

  - Type: String
  - Default `"top"`
  - See [Popper placement](https://popper.js.org/)

- data-ref
  - Type: String
  - Description: CSS selector for elemento to be tooltipped
