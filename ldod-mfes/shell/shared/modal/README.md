# LdoD Modal

## Developed with vanilla javascript (Web Component)

## Uses Bootstrap modal CSS

### Usage

```html
<ldod-modal dialog-class="modal-lg" show>
  <div slot="header-slot">Modal Header</div>
  <div slot="body-slot">Modal Body</div>
  <div slot="footer-slot">Modal Footer</div>
</ldod-modal>
```

### Attributes

- dialog-class

  - Type: String
  - Description: Bootstrap modal css to apply with modal-dialog class

- show
  - Type: Boolean
  - Description: toggle visibility of ldod-modal
