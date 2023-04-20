import{ldodValidator as t}from"@shared/ldod-events.js";import"@shared/ldod-icons.js";import"@shared/tooltip.js";const e={id:"/grids",type:"array",items:{properties:{gridTitle:{type:"string"},gridData:{type:"array"}},required:["gridTitle"]}},i={id:"/lang",type:"object",properties:{title:{type:"string"},tooltipContent:{type:"string"},grids:{$ref:"/grids"}},required:["title","grids"]},n={type:"object",properties:{pt:{$ref:"/lang"},en:{$ref:"/lang"},es:{$ref:"/lang"}},required:["pt","en","es"]};t.addSchema(e,"/grids"),t.addSchema(i,"/lang");const a=document.createElement("template");a.innerHTML='\n<div class="grid-container">\n        </div>\n';class r extends HTMLElement{constructor(){super(),this.attachShadow({mode:"open"});const t=new CSSStyleSheet;t.replaceSync("div.wrapper{margin:1rem 0}div.title-container{display:flex;justify-content:center}div.title-container>h5{font-size:14px;margin-bottom:10px;margin-top:10px;font-family:inherit;font-weight:500;line-height:1.1;color:inherit}.icon-flex{margin:auto 0 auto 10px}span[is=ldod-span-icon]{cursor:pointer}div.grid-container{text-align:center}.grid{display:grid;align-items:center;align-self:center;grid-template-columns:10% 10% 25% 10% 25% 20%;row-gap:2px}.grid-4{grid-template-columns:10% 10% 60% 20%}.grid-6{grid-template-columns:10% 10% 25% 10% 25% 20%}div.caption{text-align:center;padding-top:8px;padding-bottom:8px;color:#777}input[type=checkbox]{width:.8rem;height:.8rem;cursor:pointer}\n"),this.shadowRoot.adoptedStyleSheets=[t]}get language(){return this.getAttribute("language")}get items(){return this.getAttribute("items")}static get observedAttributes(){return["language"]}connectedCallback(){var t,e;this.data&&(this.shadowRoot.innerHTML=`\n        <div class="wrapper">\n        ${t=this.data[this.language]?.title,e=this.data[this.language]?.tooltipContent,`\n        <div class="title-container">\n            <h5 data-key="title">${t}</h5>\n            ${e&&(t=>`\n<span id="title-info" is="ldod-span-icon" icon="circle-info" size="16pt" class="icon-flex"></span>\n            <ldod-tooltip \n                data-ref="span#title-info" \n                data-tooltip-key="tooltipContent" \n                placement="bottom" \n                light \n                width="200px"\n                content="${t}"></ldod-tooltip>\n        </div>\n`)(e)} \n        `}\n        </div>\n        `,this.renderData())}renderData(){if(!this.data)return;const e=(i=this.data,t.validate(i,n));var i;if(!e.valid)throw Error(`data entry malformed: ${e.errors.map((t=>t.message)).join(",")}`);this.shadowRoot.querySelectorAll(".grid-container").forEach((t=>t.remove())),this.data[this.language]?.grids?.forEach((t=>{const e=a.content.firstElementChild.cloneNode(!0);e.innerHTML=`\n            <div class="caption">${t.gridTitle}</div>\n            <div class="grid grid-${this.items||6}"></div>\n            `;const i=e.querySelector("div.grid");t.gridData?.forEach((t=>{const e=document.createElement("div");"string"==typeof t&&(e.innerHTML=t),t instanceof Node&&e.appendChild(t),i.appendChild(e)})),this.shadowRoot.querySelector("div.wrapper").appendChild(e)}))}attributeChangedCallback(t,e,i){this.changedAttribute[t](e,i)}changedAttribute={language:(t,e)=>{t!==e&&(this.shadowRoot.querySelectorAll("[data-key]").forEach((t=>{t.textContent=this.data[this.language][t.dataset.key]})),this.shadowRoot.querySelectorAll("[data-tooltip-key]").forEach((t=>{t.setAttribute("content",this.data[this.language][t.dataset.tooltipKey])})),this.renderData())}}}!customElements.get("ldod-navigation-panel")&&customElements.define("ldod-navigation-panel",r);export{r as default};
