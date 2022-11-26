import { updateFetchedData } from '../../annotator';
import {
  createAnnotation,
  deleteAnnotation,
  updateAnnotation,
} from '../../apiRequests';
import style from './style.css?inline';
import snowStyle from '/node_modules/quill/dist/quill.snow.css?inline';

const loadModules = async () => {
  if (!MultipleSelect) await import('shared/select-pure.js');

  if (!createPopper)
    createPopper = (await import('shared/popper.js')).createPopper;
  if (!Quill) Quill = (await import('quill/dist/quill.min.js')).default;
};

const anyTagIsEqual = (tags, tag) => tags.some((t) => t === tag);

const onAutocomplete = (e, root) => {
  const assignedCats = root.annotation.data.tagList;
  const nonAssignedCats = root.categories.filter(
    (cat) => !assignedCats.includes(cat)
  );
  const target = e.target;

  const matches = nonAssignedCats.filter((cat) =>
    cat.toLowerCase().startsWith(target.value.toLowerCase())
  );

  Array.from(root.tagMultipleSelect.querySelectorAll('option-pure')).forEach(
    (option) => {
      if (
        option.hasAttribute('selected') ||
        matches.includes(option.getAttribute('value'))
      ) {
        option.hidden = false;
        return;
      }
      option.hidden = true;
    }
  );

  if (
    e.key === 'Enter' &&
    !anyTagIsEqual(matches, target.value) &&
    root.openVocab
  ) {
    root.tagMultipleSelect.shadowRoot
      .querySelectorAll('div.multi-selected-wrapper span.multi-selected')
      .forEach((span) => span.remove());

    root.tagMultipleSelect
      .querySelectorAll('option-pure[selected]')
      .forEach((option) => option.remove());

    !assignedCats.includes(target.value) && assignedCats.push(target.value);
    !root.categories.includes(target.value) &&
      root.categories.push(target.value);

    root.tagMultipleSelect.append(
      ...assignedCats.map((cat) => (
        <option-pure selected value={cat}>
          {cat}
        </option-pure>
      ))
    );
  }
};

let createPopper;
let Quill;
let MultipleSelect;

export class LdodAnnotation extends HTMLElement {
  constructor(interId, categories, openVocab) {
    super();
    this.interId = interId;
    this.categories = categories;
    this.openVocab = openVocab;
  }

  get isReadable() {
    return this.annotation?.data.canBeRead;
  }

  get isEditable() {
    return this.annotation?.data.canBeUpdated;
  }

  get contentEditor() {
    return this.querySelector('div#content');
  }

  get id() {
    return this.annotation?.id;
  }

  get tagMultipleSelect() {
    return this.querySelector('select-pure');
  }

  static get observedAttributes() {
    return ['updated'];
  }

  connectedCallback() {
    this.hidden = true;
    this.on = false;
    this.appendChild(
      <style>
        {style}
        {snowStyle}
      </style>
    );
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangedAtttribute[name]();
  }

  onChangedAtttribute = {
    updated: () => this.onHoverAnnotation(),
  };

  handleHover(ref, annotation) {
    this.ref = ref;
    this.annotation = annotation;
    this.toggleAttribute('updated', true);
  }

  onHoverAnnotation = async () => {
    await loadModules();
    if (!this.hasAttribute('updated')) return;
    this.toggleAttribute('updated', false);
    if (this.annotation.data.human && !this.isReadable) return;
    this.render();
    this.annotation.data.human && this.initEditor();
    this.initPopper();
  };

  disconnectedCallback() {
    this.removeEventListeners();
  }

  render() {
    this.querySelector('div.comment')?.remove();
    this.appendChild(
      <div class="comment">
        <div class="comment-header">
          <div class="flex-5">
            <span class="comment-icon comment-user"></span>
            <span>{this.annotation?.data.username}</span>
          </div>
          <div actions class="flex-5">
            {this.isEditable && (
              <span
                class="comment-icon comment-trash"
                onClick={this.onRemove}></span>
            )}
          </div>
        </div>
        <div comment-body class="comment-body">
          <div id="content">
            {!this.annotation?.data.human && <p>{this.annotation.data.text}</p>}
          </div>
        </div>
        <div comment-footer class="comment-footer">
          {this.isEditable && (
            <>
              <button
                id="comment-save-btn"
                class="comment-btn"
                onClick={this.onSave}>
                Save
              </button>
              <button
                id="comment-close-btn"
                class="comment-btn"
                onClick={this.onClose}>
                Close
              </button>
            </>
          )}
        </div>
      </div>
    );
    this.addEventListeners();
  }

  show = () => {
    this.hidden = false;
    this.on = true;
    this.popper.update();
  };

  hide = () => {
    this.on = false;
    setTimeout(() => {
      if (this.on) return;
      this.hidden = true;
    }, 1000);
  };

  hideNow = () => {
    this.on = false;
    this.hidden = true;
  };

  addEventListeners = () => {
    document.addEventListener('click', this.onClickOutside);
  };

  removeEventListeners = () => {
    document.removeEventListener('click', this.onClickOutside);
  };

  onClickOutside = ({ target }) => {
    if (target.id === 'comment-close-btn') return;
    if (this.contains(target)) return;
    this.hideNow();
  };

  onClose = () => {
    this.hideNow();
  };

  initPopper = (ref = this.ref) => {
    this.hideNow();
    this.popper && this.popper.destroy();
    this.popper = createPopper(ref, this);
    this.addPopperEvents();
    this.show();
  };

  addPopperEvents = () => {
    this.addPopperShowEvents();
    this.addPopperHideEvents();
  };

  addPopperShowEvents = () => {
    this.addEventListener('pointerenter', this.show);
  };

  addPopperHideEvents = () => {
    this.ref?.addEventListener('pointerleave', this.hide);
    this.addEventListener('pointerleave', this.hide);
  };

  removePopperHideEvents = () => {
    this.ref?.removeEventListener('pointerleave', this.hide);
    this.removeEventListener('pointerleave', this.hide);
  };

  initEditor = () => {
    this.editor = new Quill(this.contentEditor, {
      theme: 'snow',
      modules: { toolbar: [['bold', 'italic', 'underline', 'link', 'clean']] },
      placeholder: 'Notes ...',
    });
    let contents = this.annotation?.data.contents;

    contents
      ? this.editor.setContents(JSON.parse(decodeURI(contents)))
      : this.editor.setText(this.annotation?.data.text || '');
    if (!this.isEditable) {
      this.editor.disable();
      this.annotation?.data.tagList.length &&
        this.contentEditor.appendChild(
          <p id="annotation-categories" class="categories-chipped">
            {this.annotation.data.tagList.map((ann) => (
              <span>{ann}</span>
            ))}
          </p>
        );
      return;
    }

    this.contentEditor.appendChild(
      <div style={{ marginTop: '10px' }}>
        {
          <select-pure
            id="annotations-categories"
            name="annotations-categories"
            multiple
            default-label="Categories">
            {this.openVocab && (
              <input
                id="select-pure-autocomplete"
                type="text"
                onKeyUp={(e) => onAutocomplete(e, this)}
                placeholder={'Search or enter new tag'}
              />
            )}
            {this.categories.map((cat) => {
              return (
                <option-pure
                  selected={this.annotation.data.tagList.includes(cat)}
                  value={cat}>
                  {cat}
                </option-pure>
              );
            })}
          </select-pure>
        }
      </div>
    );

    this.addEditorEvents();
    this.addTagSelectEvent();
  };

  addEditorEvents = () => {
    this.editor.root.onblur = () => {
      this.annotation.data.text = this.editor.getText();
      this.annotation.data.contents = encodeURI(
        JSON.stringify(this.editor.getContents())
      );
    };
  };

  addTagSelectEvent = () => {
    this.tagMultipleSelect.addEventListener('change', () => {
      const selected = Array.from(
        this.tagMultipleSelect.querySelectorAll('option-pure[selected]')
      ).map((opt) => opt.getAttribute('value'));
      this.annotation.data.tagList = selected;
    });
  };

  showOnCreation = async (virtualElement, ann) => {
    await loadModules();
    this.annotation = ann;
    this.render();
    this.initEditor();
    this.initPopper(virtualElement);
    this.removePopperHideEvents();
    this.removeEventListeners();
    this.hidden = false;
  };

  // API requests

  onSuccess = (data) => {
    //updateFetchedData(data);
    //this.hideNow();
    this.dispatchAnnotationEvent();
  };

  onError = (error) => {
    this.dispatchEvent(
      new CustomEvent('ldod-error', {
        detail: { message: error.message },
        bubbles: true,
        composed: true,
      })
    );
    console.error(error);
  };

  onSave = async () => {
    if (this.annotation.data.uri) await this.updateAnnotation(this.annotation);
    else
      await createAnnotation(this.interId, this.annotation.data)
        .then(this.onSuccess)
        .catch(this.onError);
  };

  updateAnnotation = async (annotation) => {
    await updateAnnotation(annotation.id, annotation.data)
      .then(this.onSuccess)
      .catch(this.onError);
  };

  onRemove = async () => {
    if (this.annotation.data.uri) {
      await deleteAnnotation(this.interId, this.annotation.id)
        .then(this.onSuccess)
        .catch(this.onError);
    }
  };

  dispatchAnnotationEvent = () => {
    this.dispatchEvent(
      new CustomEvent('ldod-annotation', {
        bubbles: true,
      })
    );
  };
}

!customElements.get('ldod-annotation') &&
  customElements.define('ldod-annotation', LdodAnnotation);
