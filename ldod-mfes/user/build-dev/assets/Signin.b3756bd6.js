import{createElement as s}from"http://localhost:9000/ldod-mfes/shared/vanilla-jsx.js";import{G as u,c as i,e as o,a as d}from"./constants.030263b8.js";import{s as g,e as r,g as m,l as h,a as l,b as p,c as f,d as y,f as v}from"./index.148fe27f.js";import{navigateTo as C}from"http://localhost:9000/ldod-mfes/shared/router.js";import"http://localhost:9000/ldod-mfes/shared/store.js";import"http://localhost:9000/ldod-mfes/shared/fetcher.js";const w="/assets/google.8db5f960.svg";let n;function b(){const a=document.createElement("script");return a.src="https://accounts.google.com/gsi/client",a.addEventListener("load",q),a}function k(a,t){switch(n=t,a){case"google":document.head.appendChild(b());break}}const S=({credential:a})=>g("google",{accessToken:a}).then(t=>t&&r(n[t.message])).catch(t=>r(n[t.message],"error"));function q(){const a=window.google.accounts.id;a.initialize({...u,callback:S}),a.prompt()}class E extends HTMLElement{constructor(){super(),this.username="",this.password="",this.isValid=!0}get language(){return this.getAttribute("language")}static get observedAttributes(){return["language"]}async connectedCallback(){if(m().user)return C("/");await this.setConstants(),this.appendChild(this.getComponent())}attributeChangedCallback(t,e,c){t==="language"&&e&&c!==e&&this.updateLanguage()}disconnectedCallback(){}setConstants=async()=>this.constants=await h(this.language);getConstants(t){return this.constants[t]}async updateLanguage(){await this.setConstants(),["[key]"].forEach(t=>this.querySelectorAll(t).forEach(e=>{if(e instanceof HTMLInputElement){e.placeholder=this.getConstants(e.getAttribute("key")),e.title=this.getConstants("required");return}e.textContent=this.getConstants(e.getAttribute("key"))})),l({language:this.language})}clearDataInputs(){this.querySelectorAll("input").forEach(t=>{t.value="",this[t.name]=""})}clearStyleInputs(){this.querySelectorAll(".form-control").forEach(t=>t.classList.remove("valid"))}checkInputs=()=>{const t=e=>this.querySelector(`input[name=${e}]`);["username","password"].forEach(e=>{if(!this[e].trim())return this.isValid=!1,p(t(e),this.getConstants("required"));f(t(e))})};handleSubmit=t=>{t.preventDefault(),this.checkInputs(),this.isValid&&y({username:this.username,password:this.password}).catch(e=>{console.log(e),l({token:""})}),this.clearStyleInputs(),this.clearDataInputs(),this.isValid=!0};revealPassword=({target:t})=>{t.parentElement.querySelector("input[name=password]").type="text"};hidePassword=({target:t})=>{t.parentElement.querySelector("input[name=password]").type="password"};getComponent=()=>s("div",{class:"row"},s("div",{class:"login-form"},s("h2",{key:"signin-title"},this.getConstants("signin-title")),s("form",{role:"form",onSubmit:this.handleSubmit,class:"form"},s("div",{class:"col-md-offset-4 col-md-4"},s("div",{class:"form-control"},s("input",{key:"username",type:"text",autoComplete:"username",name:"username",value:this.username,onKeyUp:({target:{value:t}})=>this.username=t,placeholder:this.getConstants("username"),title:this.getConstants("required")}),s("img",{src:i,class:"icon-validation valid"}),s("img",{src:o,class:"icon-validation invalid"}),s("small",{key:"required"}))),s("div",{class:"col-md-offset-4 col-md-4"},s("div",{class:"form-control"},s("input",{key:"password",type:"password",autoComplete:"current-password",name:"password",value:this.password,onKeyUp:({target:{value:t}})=>this.password=t,placeholder:this.getConstants("password"),title:this.getConstants("required")}),s("img",{src:d,class:"icon",onMouseDown:this.revealPassword,onMouseUp:this.hidePassword}),s("img",{src:i,class:"icon-validation valid"}),s("img",{src:o,class:"icon-validation invalid"}),s("small",{key:"required"}))),s("div",{class:"col-md-offset-5 col-md-2"},s("button",{key:"sign-in",class:"btn btn-primary",type:"submit",style:{width:"100%"}},this.getConstants("sign-in")))),s("div",{style:{padding:"4px 16px"}},[["google",w]].map(([t,e])=>s("div",{class:"col-md-offset-5 col-md-2"},s("button",{class:`btn btn-outline-primary social ${t}`,type:"button",onClick:()=>k(t,this.constants),style:{width:"100%"}},s("img",{src:e,class:"social-icon"}),v(t)))))),s("div",{class:"row"},s("a",{key:"signup",is:"nav-to",to:"/user/signup"},this.getConstants("signup")??"")))}!customElements.get("sign-in")&&customElements.define("sign-in",E);const T=(a,t)=>{document.querySelector(t).appendChild(s("sign-in",{language:a}))},G=()=>document.querySelector("sign-in")?.remove(),P="/signin";export{T as mount,P as path,G as unMount};
