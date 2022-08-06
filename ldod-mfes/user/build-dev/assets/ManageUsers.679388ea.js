import{createElement as e,createFragment as v}from"http://localhost:9000/ldod-mfes/shared/vanilla-jsx.js";import{u as k,k as w,r as x,m as S,n as U,o as C}from"./index.148fe27f.js";import"http://localhost:9000/ldod-mfes/shared/tooltip.js";import"http://localhost:9000/ldod-mfes/shared/modal.js";import"http://localhost:9000/ldod-mfes/shared/store.js";import"http://localhost:9000/ldod-mfes/shared/router.js";import"http://localhost:9000/ldod-mfes/shared/fetcher.js";const g="/assets/edit.61525bd6.svg",L="/assets/edit-primary.2bdd1028.svg",A="/assets/trash.094d9a32.svg",M=`.table{width:100%;max-width:100%;margin-bottom:1rem;text-align:start}thead td{font-weight:700}.table th,.table td{padding:.75rem;vertical-align:middle;border-top:1px solid #eceeef}.table thead th{vertical-align:middle;border-bottom:2px solid #eceeef}.table tbody+tbody{border-top:2px solid #eceeef}.table .table{background-color:#fff}.table-sm th,.table-sm td{padding:.3rem}.table-bordered{border-collapse:collapse;border-spacing:0;border:1px solid #eceeef}.table-bordered th,.table-bordered td{border:1px solid #eceeef}.table-bordered thead th,.table-bordered thead td{border-bottom-width:2px}.table-striped tbody tr:nth-of-type(odd){background-color:#0000000d}.table-hover tbody tr:hover{background-color:#00000013}.table-active,.table-active>th,.table-active>td{background-color:#00000013}.table-hover .table-active:hover{background-color:#00000013}.table-hover .table-active:hover>td,.table-hover .table-active:hover>th{background-color:#00000013}.table-success,.table-success>th,.table-success>td{background-color:#dff0d8}.table-hover .table-success:hover{background-color:#d0e9c6}.table-hover .table-success:hover>td,.table-hover .table-success:hover>th{background-color:#d0e9c6}.table-info,.table-info>th,.table-info>td{background-color:#d9edf7}.table-hover .table-info:hover{background-color:#c4e3f3}.table-hover .table-info:hover>td,.table-hover .table-info:hover>th{background-color:#c4e3f3}.table-warning,.table-warning>th,.table-warning>td{background-color:#fcf8e3}.table-hover .table-warning:hover{background-color:#faf2cc}.table-hover .table-warning:hover>td,.table-hover .table-warning:hover>th{background-color:#faf2cc}.table-danger,.table-danger>th,.table-danger>td{background-color:#f2dede}.table-hover .table-danger:hover{background-color:#ebcccc}.table-hover .table-danger:hover>td,.table-hover .table-danger:hover>th{background-color:#ebcccc}.thead-inverse th{color:#fff;background-color:#292b2c}.thead-default th{color:#464a4c;background-color:#eceeef}.table-inverse{color:#fff;background-color:#292b2c}.table-inverse th,.table-inverse td,.table-inverse thead th{border-color:#fff}.table-inverse.table-bordered{border:0}.table-responsive{display:block;width:100%;overflow-x:auto;-ms-overflow-style:-ms-autohiding-scrollbar}.table-responsive.table-bordered{border:0}.table{width:100%;margin-bottom:1rem;color:#212529}.table th,.table td{padding:.75rem;vertical-align:middle;border-top:1px solid #dee2e6}.table thead th{vertical-align:middle;border-bottom:2px solid #dee2e6}.table tbody+tbody{border-top:2px solid #dee2e6}.table-sm th,.table-sm td{padding:.3rem}.table-bordered,.table-bordered th,.table-bordered td{border:1px solid #dee2e6}.table-bordered thead th,.table-bordered thead td{border-bottom-width:2px}.table-borderless th,.table-borderless td,.table-borderless thead th,.table-borderless tbody+tbody{border:0}.table-striped tbody tr:nth-of-type(odd){background-color:#0000000d}.table-hover tbody tr:hover{color:#212529;background-color:#00000013}.table-primary,.table-primary>th,.table-primary>td{background-color:#b8daff}.table-primary th,.table-primary td,.table-primary thead th,.table-primary tbody+tbody{border-color:#7abaff}.table-hover .table-primary:hover{background-color:#9fcdff}.table-hover .table-primary:hover>td,.table-hover .table-primary:hover>th{background-color:#9fcdff}.table-secondary,.table-secondary>th,.table-secondary>td{background-color:#d6d8db}.table-secondary th,.table-secondary td,.table-secondary thead th,.table-secondary tbody+tbody{border-color:#b3b7bb}.table-hover .table-secondary:hover{background-color:#c8cbcf}.table-hover .table-secondary:hover>td,.table-hover .table-secondary:hover>th{background-color:#c8cbcf}.table-success,.table-success>th,.table-success>td{background-color:#c3e6cb}.table-success th,.table-success td,.table-success thead th,.table-success tbody+tbody{border-color:#8fd19e}.table-hover .table-success:hover{background-color:#b1dfbb}.table-hover .table-success:hover>td,.table-hover .table-success:hover>th{background-color:#b1dfbb}.table-info,.table-info>th,.table-info>td{background-color:#bee5eb}.table-info th,.table-info td,.table-info thead th,.table-info tbody+tbody{border-color:#86cfda}.table-hover .table-info:hover{background-color:#abdde5}.table-hover .table-info:hover>td,.table-hover .table-info:hover>th{background-color:#abdde5}.table-warning,.table-warning>th,.table-warning>td{background-color:#ffeeba}.table-warning th,.table-warning td,.table-warning thead th,.table-warning tbody+tbody{border-color:#ffdf7e}.table-hover .table-warning:hover{background-color:#ffe8a1}.table-hover .table-warning:hover>td,.table-hover .table-warning:hover>th{background-color:#ffe8a1}.table-danger,.table-danger>th,.table-danger>td{background-color:#f5c6cb}.table-danger th,.table-danger td,.table-danger thead th,.table-danger tbody+tbody{border-color:#ed969e}.table-hover .table-danger:hover{background-color:#f1b0b7}.table-hover .table-danger:hover>td,.table-hover .table-danger:hover>th{background-color:#f1b0b7}.table-light,.table-light>th,.table-light>td{background-color:#fdfdfe}.table-light th,.table-light td,.table-light thead th,.table-light tbody+tbody{border-color:#fbfcfc}.table-hover .table-light:hover{background-color:#ececf6}.table-hover .table-light:hover>td,.table-hover .table-light:hover>th{background-color:#ececf6}.table-dark,.table-dark>th,.table-dark>td{background-color:#c6c8ca}.table-dark th,.table-dark td,.table-dark thead th,.table-dark tbody+tbody{border-color:#95999c}.table-hover .table-dark:hover{background-color:#b9bbbe}.table-hover .table-dark:hover>td,.table-hover .table-dark:hover>th{background-color:#b9bbbe}.table-active,.table-active>th,.table-active>td{background-color:#00000013}.table-hover .table-active:hover{background-color:#00000013}.table-hover .table-active:hover>td,.table-hover .table-active:hover>th{background-color:#00000013}.table .thead-dark th{color:#fff;background-color:#343a40;border-color:#454d55}.table .thead-light th{color:#495057;background-color:#e9ecef;border-color:#dee2e6}.table-dark{color:#fff;background-color:#343a40}.table-dark th,.table-dark td,.table-dark thead th{border-color:#454d55}.table-dark.table-bordered{border:0}.table-dark.table-striped tbody tr:nth-of-type(odd){background-color:#ffffff0d}.table-dark.table-hover tbody tr:hover{color:#fff;background-color:#ffffff13}@media (max-width: 575.98px){.table-responsive-sm{display:block;width:100%;overflow-x:auto;-webkit-overflow-scrolling:touch}.table-responsive-sm>.table-bordered{border:0}}@media (max-width: 767.98px){.table-responsive-md{display:block;width:100%;overflow-x:auto;-webkit-overflow-scrolling:touch}.table-responsive-md>.table-bordered{border:0}}@media (max-width: 991.98px){.table-responsive-lg{display:block;width:100%;overflow-x:auto;-webkit-overflow-scrolling:touch}.table-responsive-lg>.table-bordered{border:0}}@media (max-width: 1199.98px){.table-responsive-xl{display:block;width:100%;overflow-x:auto;-webkit-overflow-scrolling:touch}.table-responsive-xl>.table-bordered{border:0}}.table-responsive{display:block;width:100%;overflow-x:auto;-webkit-overflow-scrolling:touch}.table-responsive>.table-bordered{border:0}
`,f=({id:a,headers:t,data:o,constants:s,classes:l})=>e(v,null,e("style",null,M),e("table",{id:a,class:l},e("thead",null,e("tr",null,t.map(d=>e("td",{"data-key":d},s(d))))),e("tbody",null,o.map(d=>e("tr",null,t.map(i=>e("td",null,typeof d[i]=="function"?d[i]():d[i]))))))),b={sessionListHeaders:["userName","firstName","lastName","lastRequest","sessionId"],usersListHeaders:["userName","firstName","lastName","enabled","listOfRoles","lastLogin","active","actions"],pt:{TRUE:"Sim",FALSE:"N\xE3o",users:"Utilizadores",user:"Utilizador",admin:"Administrador",sessions:"Sess\xF5es",userName:"Nome de utilizador",firstName:"Primeiro nome",lastName:"Apelido",lastRequest:"\xDAltimo Pedido",sessionId:"ID da Sess\xE3o",enabled:"Confirmado",listOfRoles:"Papeis",lastLogin:"\xDAltimo acesso",actions:"Ac\xE7\xF5es",active:"Ativo",edit:"Editar utilizador",remove:"Eliminar utilizador",editUserHeader:"Alterar utilizador",update:"Atualizar",email:"Endere\xE7o eletr\xF3nico",newPassword:"Nova Senha",toggleActiveMode:"Alterar estado ativo",changeLdodMode:"Alterar modo do LdoD",deleteUserSessions:"Eliminar sess\xF5es de utilizador",userMode:"Modo utilizador",adminMode:"Modo administrador"},en:{TRUE:"Yes",FALSE:"No",users:"Users",user:"User",admin:"Administrator",sessions:"Sessions",userName:"Username",firstName:"First Name",lastName:"Last Name",lastRequest:"Last Request",sessionId:"Session ID",enabled:"Enabled",listOfRoles:"Roles",lastLogin:"Last login",actions:"Actions",active:"Active",edit:"Edit user",remove:"Delete user",editUserHeader:"Edit user",update:"Update",email:"Email",newPassword:"New Password",toggleActiveMode:"Toggle active state",changeLdodMode:"Change LdoD mode",deleteUserSessions:"Delete user sessions",userMode:"User mode",adminMode:"Administrator mode"},es:{TRUE:"S\xED",FALSE:"No",users:"Usuarios",user:"Usuario",admin:"Administrador",sessions:"Sesiones",userName:"Nombre de usuario",firstName:"Primer nombre",lastName:"Apellido",lastRequest:"\xDAltima Petici\xF3n",sessionId:"ID de sesi\xF3n",enabled:"Confirmado",listOfRoles:"Funciones",lastLogin:"\xDAltimo acceso",actions:"Acciones",active:"Activo",edit:"Editar usuario",remove:"Eliminar usuario",editUserHeader:"Editar usuario",update:"Actualizar",email:"Correo electr\xF3nico",newPassword:"Nueva contrase\xF1a",toggleActiveMode:"Cambiar estado activo",changeLdodMode:"Cambiar el modo LdoD",deleteUserSessions:"Eliminar sesiones de usuario",userMode:"Modo de usuario",adminMode:"Modo administrador"}},u=()=>document.querySelector("manage-users");function m(a){return b[u().language][a]}const N=async()=>{const a=Array.from(document.querySelector("ldod-modal form")).reduce((t,{name:o,value:s,type:l,checked:d})=>{if(l==="checkbox"){let i=o;return o.startsWith("role_")&&(i=o.split("role_")[1]),t[i]=d,t}return t[o]=s,t},{});k(a).then(t=>{r().userList=t.userList,u().toggleAttribute("show"),u().render()})},q=()=>e("ldod-modal",{"dialog-class":"modal-lg"},e("span",{"data-key":"editUserHeader",slot:"header-slot"},m("editUserHeader")),e("div",{slot:"body-slot"}),e("div",{slot:"footer-slot"},e("button",{class:"btn btn-primary",onClick:N,"data-key":"update"},m("update"))));function n(a){return b[document.querySelector("manage-users").language][a]}const E=()=>e("div",null,e("form",{role:"form",class:"form"},e("div",{class:"form-control form-flex"},e("label",{"data-key":"firstName"},n("firstName")),e("input",{key:"firstname",type:"text",autoComplete:"first-name",name:"firstName",value:h()?.firstName})),e("div",{class:"form-control form-flex"},e("label",{"data-key":"lastName"},n("lastName")),e("input",{key:"lastname",type:"text",name:"lastName",value:h()?.lastName})),e("div",{class:"form-control form-flex"},e("input",{type:"hidden",value:h()?.userName,name:"oldUsername"}),e("label",{"data-key":"userName"},n("userName")),e("input",{key:"userName",type:"text",name:"newUsername",value:h()?.userName})),e("div",{class:"form-control form-flex"},e("label",{"data-key":"email"},n("email")),e("input",{key:"email",type:"text",name:"email",value:h()?.email})),e("div",{class:"form-control form-flex"},e("label",{"data-key":"user"},n("user")),e("div",null,e("label",{class:"switch"},e("input",{name:"role_user",type:"checkbox"}),e("span",{class:"slider round"})))),e("div",{class:"form-control form-flex"},e("label",{"data-key":"admin"},n("admin")),e("div",null,e("label",{class:"switch"},e("input",{name:"role_admin",type:"checkbox"}),e("span",{class:"slider round"})))),e("div",{class:"form-control form-flex"},e("label",{"data-key":"enabled"},n("enabled")),e("div",null,e("label",{class:"switch"},e("input",{name:"enabled",type:"checkbox"}),e("span",{class:"slider round"})))),e("div",{class:"form-control form-flex"},e("label",{"data-key":"newPassword"},n("newPassword")),e("input",{key:"newPassword",type:"text",name:"newPassword"})))),R=()=>document.querySelector("manage-users"),$=()=>R().language;function c(a){return b[$()][a]}const D=async a=>{const t=await w(a);r().userList.forEach(o=>{if(o.externalId===a){o.active=t.ok;return}}),T(t.ok,a)},T=(a,t)=>{const o=p(a,t);document.querySelector(`#active-${t}`).replaceWith(o)},H=async({target:a})=>{const t=await x(a.dataset.id);r().userList=t.userList;let o=a.parentNode;for(;o.tagName!=="TR";)o=o.parentNode;o.remove(),document.querySelector("manage-users").updateUsersLength()},P=async({target:a})=>{const t=r().userList.find(({externalId:l})=>l===a.dataset.id);_(t);const o=document.querySelector("manage-users ldod-modal");o.toggleAttribute("show");const s=o.querySelector("div[slot='body-slot']");s.innerHTML="",s.appendChild(e(E,null)),s.querySelectorAll("input[type='checkbox']").forEach(l=>{l.checked=l.name.startsWith("role")?t?.listOfRoles.includes(l.name.toUpperCase()):t?.enabled})},p=(a,t)=>e("div",{id:`active-${t}`,class:"text-center"},e("button",{id:`button-active-${t}`,class:`btn ${a?"btn-success":"btn-secondary"} btn-sm`,onClick:()=>D(t)},e("span",{"data-key":String(a).toUpperCase()},c(String(a).toUpperCase()))),e("ldod-tooltip",{placement:"top","data-ref":`#button-active-${t}`,"data-tooltipkey":"toggleActiveMode",content:c("toggleActiveMode")})),z=a=>e("div",{class:"text-center"},e("img",{id:`edit-icon-${a}`,"data-id":a,src:L,class:"btn-icon action",onClick:P}),e("img",{id:`trash-icon-${a}`,"data-id":a,src:A,class:"btn-icon action",onClick:H}),e("ldod-tooltip",{"data-ref":`#edit-icon-${a}`,"data-tooltipkey":"edit",placement:"top",content:c("edit")}),e("ldod-tooltip",{"data-ref":`#trash-icon-${a}`,"data-tooltipkey":"remove",placement:"top",content:c("remove")})),I=()=>e("div",null,e(q,null),e("div",{id:"userList",class:"row"},e(f,{id:"users-list-table",classes:"table table-responsive-sm table-striped table-bordered",headers:b.usersListHeaders,data:r().userList.map(a=>({...a,enabled:e("div",{"data-key":String(a.enabled).toUpperCase()},c(String(a.enabled).toUpperCase())),active:p(a.active,a.externalId),actions:z(a.externalId)})),constants:a=>c(a)})));class F extends HTMLElement{constructor(){super()}static get observedAttributes(){return["data","language"]}get language(){return this.getAttribute("language")}get usersLength(){return r()?r().userList.length:"loading"}getMode(){return r().ldoDAdmin?"admin":"user"}setMode(t){r().ldoDAdmin=t}getConstants(t){return b[this.language][t]}connectedCallback(){this.render()}attributeChangedCallback(t,o,s){this.handlers[t](o,s)}disconnectedCallback(){}render(){this.innerHTML="",this.appendChild(this.getComponent())}handlers={language:(t,o)=>{t&&o!==t&&this.handleChangeLanguage()},data:()=>this.render()};handleChangeLanguage(){this.querySelectorAll("[data-key").forEach(t=>{t.textContent=this.getConstants(t.dataset.key)}),this.querySelectorAll("[data-tooltipkey]").forEach(t=>{t.setAttribute("content",this.getConstants(t.dataset.tooltipkey))}),this.querySelectorAll("[dynamic]").forEach(t=>{t.textContent=this.getConstants(t.dynamicKey())})}onSwitchMode=()=>{S().then(t=>{t&&this.setMode(t.ok),this.querySelector("#adminMode>button>span").innerHTML=this.getConstants(`${this.getMode()}Mode`)})};onDeleteSessions=()=>{U().then(t=>{r().sessionList=t.sessionList,this.querySelector("div#sessions-list").replaceChildren(this.getSessionsTable())})};getSessionsTable=()=>e(f,{id:"sessions-list-table",classes:"table table-responsive-sm table-striped table-bordered",headers:b.sessionListHeaders,data:r().sessionList,constants:t=>this.getConstants(t)});updateUsersLength=()=>this.querySelector("h1>span").innerHTML=`&nbsp;(${this.usersLength})`;getComponent(){return e("div",{class:"container"},r()&&e(v,null,e("h1",{class:"text-center",style:{display:"flex",justifyContent:"center"}},e("div",{"data-key":"users"},this.getConstants("users")),e("span",null,"\xA0(",this.usersLength,")")),e(I,null),e("h1",{class:"text-center","data-key":"sessions"},this.getConstants("sessions")),e("div",{id:"adminMode",class:"row btn-row"},e("button",{id:"switch-button",type:"button",class:"btn btn-danger ellipsis",onClick:this.onSwitchMode},e("img",{src:g,class:"btn-icon"}),e("span",{dynamic:!0,dynamicKey:()=>`${this.getMode()}Mode`},this.getConstants(`${this.getMode()}Mode`))),e("ldod-tooltip",{placement:"top","data-ref":"#switch-button","data-tooltipkey":"changeLdodMode",content:this.getConstants("changeLdodMode")})),e("div",{id:"deleteSessions",class:"row btn-row"},e("button",{id:"delete-sessions-button",type:"button",class:"btn btn-danger ellipsis",onClick:this.onDeleteSessions},e("img",{src:g,class:"btn-icon"}),e("span",{"data-key":"deleteUserSessions"},this.getConstants("deleteUserSessions"))),e("ldod-tooltip",{placement:"top","data-ref":"#delete-sessions-button","data-tooltipkey":"deleteUserSessions",content:this.getConstants("deleteUserSessions")})),e("div",{id:"sessions-list",class:"row"},this.getSessionsTable())))}}!customElements.get("manage-users")&&customElements.define("manage-users",F);const y=a=>{let t=a;return[()=>t,l=>t=l]},[r,O]=y(),[h,_]=y(),Q=async(a,t)=>{C().then(o=>{O(o),document.querySelector(`${t}>manage-users`).setAttribute("data","")}),document.querySelector(t).appendChild(e("manage-users",{language:a}))},X=()=>document.querySelector("manage-users")?.remove(),Z="/manage-users";export{Q as mount,Z as path,_ as setUser,O as setUsersData,X as unMount,h as user,r as usersData};
