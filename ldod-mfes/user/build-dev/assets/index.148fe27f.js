import{createElement as u,createFragment as U}from"http://localhost:9000/ldod-mfes/shared/vanilla-jsx.js";import{getPartialStorage as T,Store as A}from"http://localhost:9000/ldod-mfes/shared/store.js";import{navigateTo as g}from"http://localhost:9000/ldod-mfes/shared/router.js";import{fetcher as r}from"http://localhost:9000/ldod-mfes/shared/fetcher.js";const P=function(){const t=document.createElement("link").relList;if(t&&t.supports&&t.supports("modulepreload"))return;for(const n of document.querySelectorAll('link[rel="modulepreload"]'))i(n);new MutationObserver(n=>{for(const o of n)if(o.type==="childList")for(const h of o.addedNodes)h.tagName==="LINK"&&h.rel==="modulepreload"&&i(h)}).observe(document,{childList:!0,subtree:!0});function s(n){const o={};return n.integrity&&(o.integrity=n.integrity),n.referrerpolicy&&(o.referrerPolicy=n.referrerpolicy),n.crossorigin==="use-credentials"?o.credentials="include":n.crossorigin==="anonymous"?o.credentials="omit":o.credentials="same-origin",o}function i(n){if(n.ep)return;n.ep=!0;const o=s(n);fetch(n.href,o)}};P();const R="modulepreload",O=function(e){return"/"+e},y={},d=function(t,s,i){return!s||s.length===0?t():Promise.all(s.map(n=>{if(n=O(n),n in y)return;y[n]=!0;const o=n.endsWith(".css"),h=o?'[rel="stylesheet"]':"";if(document.querySelector(`link[href="${n}"]${h}`))return;const l=document.createElement("link");if(l.rel=o?"stylesheet":R,o||(l.as="script",l.crossOrigin=""),l.href=n,document.head.appendChild(l),o)return new Promise(($,q)=>{l.addEventListener("load",$),l.addEventListener("error",()=>q(new Error(`Unable to preload CSS for ${n}`)))})})).then(()=>t())},M=(e,t)=>{const s=e[t];return s?typeof s=="function"?s():Promise.resolve(s):new Promise((i,n)=>{(typeof queueMicrotask=="function"?queueMicrotask:setTimeout)(n.bind(null,new Error("Unknown variable dynamic import: "+t)))})};function Y(e,t){const s=e.parentElement,i=s.querySelector("small");s.className="form-control invalid",i.innerText=t}function Z(e){const t=e.parentElement;t.className="form-control valid"}function ee(e){return e.slice(0,1).toUpperCase().concat(e.slice(1,e.length))}const E=(e,t="message")=>window.dispatchEvent(new CustomEvent(`ldod-${t}`,{detail:{message:e}})),I=async e=>(await M(Object.assign({"./resources/messages-en.js":()=>d(()=>import("./messages-en.0bb34407.js"),[]),"./resources/messages-es.js":()=>d(()=>import("./messages-es.7269dda7.js"),[]),"./resources/messages-pt.js":()=>d(()=>import("./messages-pt.29ec40f0.js"),[])}),`./resources/messages-${e}.js`)).default;function f(e,t={},s=window){const i=new CustomEvent(e,t);s.dispatchEvent(i)}const a="http://localhost:8000/api",te=async e=>{await r.post(`${a}/auth/sign-in`,e).then(t=>{t.message&&f("ldod-error",{detail:{message:t.message}}),_(t.accessToken)})},j=async e=>{await r.get(`${a}/user`,null,e).then(t=>x(t)).catch(t=>t.message==="unauthorized"&&w())},ne=async e=>await r.post(`${a}/auth/sign-up`,e),se=async(e,t)=>r.post(`${a}/auth/${e}`,t).then(s=>{if(H(s))return g("/user/signup",globalThis,s),Promise.resolve({message:"googleAssociation"});N(s)&&_(s.accessToken)}),oe=async e=>await r.get(`${a}/auth${e}`),re=async e=>{if(!c().token)return E("Not authorized to access resource","error"),g("/");await r.get(`${a}/admin/user${e}`,null,c().token)},ae=async e=>r.post(`${a}/user/change-password`,e).then(t=>(g("/"),Promise.resolve(t))),ie=async()=>c().token?await r.get(`${a}/admin/user/list`,null,c().token):(E("Not authorized to access resource","error"),g("/")),ce=async()=>await r.post(`${a}/admin/user/switch`),ue=async()=>await r.post(`${a}/admin/user/sessions-delete`),le=async e=>await r.post(`${a}/admin/user/active/${e}`),de=async e=>await r.post(`${a}/admin/user/delete/${e}`),ge=async e=>await r.post(`${a}/admin/user/edit`,e);function N(e){return Object.keys(e).some(t=>t==="accessToken"||t==="tokenType")}function H(e){return Object?.keys(e).some(t=>t==="socialId")}function _(e){if(!e)return w();m({token:e}),f("ldod-token",{detail:{token:e}})}function w(){m({token:"",user:""}),f("ldod-logout"),g("/user/signin")}function x(e){m({user:e}),f("ldod-login",{detail:{user:e}})}const b=T("ldod-store",["token","language"]),D={token:void 0,language:b?.language,user:void 0,index:0},v=new A(D),c=()=>v.getState(),m=e=>v.setState(e),k=()=>`${c().user.firstName} ${c().user.lastName}`;v.subscribe(async(e,t)=>{t.token!==e.token&&e.token&&(await j(e.token),(location.pathname.endsWith("signin")||location.pathname.endsWith("signup"))&&g("/"))});m({token:b?.token});const S=()=>(m({index:c().index+1}),c().index),F=Object.freeze(Object.defineProperty({__proto__:null,store:v,getState:c,setState:m,userFullName:k,registerInstance:S},Symbol.toStringTag,{value:"Module"}));function z(){return u("a",{is:"nav-to",id:"login",class:"login update-language",to:"/user/signin"})}const V=()=>u(U,null,u("a",{id:"login",class:"dropdown-toggle"},k(),u("span",{class:"caret"})),u("ul",{class:"dropdown-menu"},u("li",null,u("a",{class:"update-language",id:"logout"})),u("li",null,u("a",{is:"nav-to",class:"update-language",id:"change-password",to:"/user/change-password"}))));class C extends HTMLLIElement{constructor(){super(),this.onUserLogout=this.onUserLogout.bind(this),this.onUserLogin=this.onUserLogin.bind(this),this.id=`user-component-${S()}`}get language(){return this.getAttribute("language")??"en"}set language(t){this.setAttribute("language",t)}static get observedAttributes(){return["language"]}async connectedCallback(){await this.setConstants(),this.render(),c().user&&this.onUserLogin()}attributeChangedCallback(t,s,i){s&&i&&s!==i&&t==="language"&&this.updateLanguage(i)}disconnectedCallback(){this.removeListeners()}setConstants=async()=>this.constants=await I(this.language);getConstants=t=>this.constants[t];render(){c().user?this.appendChild(V()):this.appendChild(z()),this.updateLanguage(),this.addListeners()}updateComponent(t){this.removeListeners(),this.render()}addListeners(){["logout","password"].forEach(t=>this.querySelector(`#${t}`)?.addEventListener("click",this.handlers(t).handler)),window.addEventListener("ldod-login",this.onUserLogin),window.addEventListener("ldod-logout",this.onUserLogout)}removeListeners(){["logout","password"].forEach(t=>this.querySelector(`#${t}`)?.removeEventListener("click",this.handlers(t)?.handler)),this.innerHTML="",window.removeEventListener("ldod-login",this.onUserLogin),window.removeEventListener("ldod-logout",this.onUserLogout)}onUserLogin(){this.updateComponent(),this.dispatchEvent(new CustomEvent("ldod-login",{bubbles:!0,composed:!0,detail:{user:c().user}}))}onUserLogout(){this.updateComponent(),this.dispatchEvent(new CustomEvent("ldod-logout",{composed:!0,bubbles:!0}))}async updateLanguage(){await this.setConstants(),this.querySelectorAll("a.update-language").forEach(t=>t.innerHTML=this.getConstants(t.id))}handlers(t){return{logout:{id:"logout",handler:()=>w()},password:{id:"password",handler:()=>console.log("password changed")}}[t]}}!customElements.get("user-component")&&customElements.define("user-component",C,{extends:"li"});const W=Object.freeze(Object.defineProperty({__proto__:null,UserComponent:C},Symbol.toStringTag,{value:"Module"}));await d(()=>Promise.resolve().then(()=>F),void 0).catch(e=>console.error(e));await d(()=>Promise.resolve().then(()=>W),void 0).catch(e=>console.error(e));let p;const L=async()=>{p=await d(()=>import("./userRouter.390de4a8.js"),[])},B={path:"/user",mount:async(e,t)=>{p||await L(),await p.mount(e,t)},unMount:async()=>{p||await L(),await p.unMount()}};B.mount("en","#root");window.addEventListener("ldod-url-changed",({detail:{path:e}})=>{e==="/"&&(history.pushState({},void 0,"/"),document.querySelector("ldod-outlet").childNodes.forEach(t=>t.remove()))});window.addEventListener("ldod-loading",({detail:{isLoading:e}})=>{const t=document.querySelector("div#loading");t.ariaHidden=String(!e)});window.addEventListener("ldod-message",({detail:{message:e}})=>{const t=document.querySelector("div#message");t.innerHTML=e});window.addEventListener("ldod-error",({detail:{message:e}})=>{const t=document.querySelector("div#error");t.innerHTML=e});document.querySelector("#clear-message").addEventListener("click",K);document.querySelectorAll("button.lang").forEach(e=>{e.addEventListener("click",()=>document.querySelectorAll("[language]").forEach(t=>{t.setAttribute("language",e.id)}))});function K(){document.getElementById("message").innerHTML="",document.getElementById("error").innerHTML=""}export{d as _,m as a,Y as b,Z as c,te as d,E as e,ee as f,c as g,ne as h,ae as i,oe as j,le as k,I as l,ce as m,ue as n,ie as o,de as r,se as s,re as t,ge as u};
