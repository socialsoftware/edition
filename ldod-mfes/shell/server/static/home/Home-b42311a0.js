import{parseHTML as n}from"shared/utils.js";const e=new CSSStyleSheet;e.replaceSync(".container {\n    padding-right: 15px;\n    padding-left: 15px;\n    margin-right: auto;\n    margin-left: auto;\n}\n\n.container::after {\n    display: table;\n    content: '';\n    clear: both;\n}\n\n@media (min-width: 768px) {\n    .container {\n        width: 720px;\n    }\n}\n\n@media (min-width: 992px) {\n    .container {\n        width: 940;\n    }\n}\n\n@media (min-width: 1200px) {\n    .container {\n        width: 1140px;\n    }\n}\n\n.font-monospace {\n    font-family: 'Space-Mono';\n}\n\nimg {\n    vertical-align: middle;\n    border: 0;\n}\n\n.visible-xs-inline {\n    display: none !important;\n}\n\n@media (max-width: 767px) {\n    .hidden-xs {\n        display: none !important;\n    }\n}\n\n@media (max-width: 767px) {\n    .visible-xs-inline {\n        display: inline !important;\n    }\n}\n\n.bottom-info {\n    position: relative;\n    float: left;\n    margin-top: 40px;\n    font-size: 10px;\n}\n\n.bottom-bar {\n    position: relative;\n    width: 100%;\n    height: 20px;\n    border-bottom: 40px solid #fc1b27;\n}");class t extends HTMLElement{constructor(){super();this.attachShadow({mode:"open"}).adoptedStyleSheets=[e],this.constants=void 0}static get observedAttributes(){return["language"]}get language(){return this.getAttribute("language")}set language(n){this.setAttribute("language",n)}async connectedCallback(){await this.setConstants(),this.render()}disconnectedCallback(){}async attributeChangedCallback(n,e,t){e&&e!==t&&(await this.setConstants(),this.shadowRoot.querySelectorAll(".update-language").forEach((n=>n.innerHTML=this.constants[n.id])))}render(){const e=n(html`
      <div class="container">
        <div id="info" class="bottom-info font-monospace update-language">
          ${this.constants.info}
        </div>
      </div>
    `),t=n(html`<div class="bottom-bar"></div>`);this.shadowRoot.append(e,t)}async setConstants(){this.constants=await this.loadConstants(this.language)}loadConstants=async n=>(await function(n){switch(n){case"../../../resources/home/constants/constants-en.js":return import("./constants-en-95c2516b.js");case"../../../resources/home/constants/constants-es.js":return import("./constants-es-16872f63.js");case"../../../resources/home/constants/constants-pt.js":return import("./constants-pt-0aac0533.js");default:return new Promise((function(e,t){("function"==typeof queueMicrotask?queueMicrotask:setTimeout)(t.bind(null,new Error("Unknown variable dynamic import: "+n)))}))}}(`../../../resources/home/constants/constants-${n}.js`)).default}customElements.define("home-info",t);const a=[{editor:"Jerónimo Pizarro",text:"E eu offereço-te este livro porque sei que elle é bello e inutil.",number:"17",path:"Fr449/inter/Fr449_WIT_ED_CRIT_P"},{editor:"Jacinto do Prado Coelho",text:"Senti-me agora respirar como se houvesse practicado uma cousa nova, ou atrazada.",number:"188",path:"Fr157/inter/Fr157_WIT_ED_CRIT_C"},{editor:"Teresa Sobral Cunha",text:"Em mim foi sempre menor a intensidade das sensações que a intensidade da sensação delas.",number:"283",path:"Fr309/inter/Fr309_WIT_ED_CRIT_SC"},{editor:"Richard Zenith",text:"O silêncio que sai do som da chuva espalha-se, num crescendo de monotonia cinzenta, pela rua estreita que fito.",number:"41",path:"Fr175/inter/Fr175_WIT_ED_CRIT_Z"},{editor:"Jerónimo Pizarro",text:"A grande terra, que serve os mortos, serviria, menos maternalmente, esses papeis.",number:"387",path:"Fr159.b/inter/Fr159_b_WIT_ED_CRIT_P_1"},{editor:"Teresa Sobral Cunha",text:"Em cada pingo de chuva a minha vida falhada chora na natureza.",number:"266",path:"Fr390/inter/Fr390_WIT_ED_CRIT_SC"},{editor:"Jacinto do Prado Coelho",text:"Como nos dias em que a trovoada se prepara e os ruidos da rua fallam alto com uma voz solitária.",number:"45",path:"Fr042/inter/Fr042_WIT_ED_CRIT_C"},{editor:"Teresa Sobral Cunha",text:"Ninguém estava quem era, e o patrão Vasques apareceu à porta do gabinete para pensar em dizer qualquer coisa.",number:"441",path:"Fr043/inter/Fr043_WIT_ED_CRIT_SC"},{editor:"Richard Zenith",text:"'Vem aí uma grande trovoada', disse o Moreira, e voltou a página do Razão.",number:"183",path:"Fr044/inter/Fr044_WIT_ED_CRIT_Z"},{editor:"Jerónimo Pizarro",text:"E então, em plena vida, é que o sonho tem grandes cinemas.",number:"262",path:"Fr149/inter/Fr149_WIT_ED_CRIT_P"},{editor:"Jerónimo Pizarro",text:"Lêr é sonhar pela mão de outrem.",number:"586",path:"Fr554/inter/Fr554_WIT_ED_CRIT_P"},{editor:"Jacinto do Prado Coelho",text:"Devo ao ser guarda-livros grande parte do que posso sentir e pensar como a negação e a fuga do cargo.",number:"133",path:"Fr198/inter/Fr198_WIT_ED_CRIT_C"},{editor:"Teresa Sobral Cunha",text:"Durmo sobre os cotovelos onde o corrimão me doe, e sei de nada como um grande prometimento.",number:"380",path:"Fr030/inter/Fr030_WIT_ED_CRIT_SC"},{editor:"Richard Zenith",text:"Sentado á janela, contemplo com os sentidos todos esta coisa nenhuma da vida universal que está lá fora.",number:"50",path:"Fr118/inter/Fr118_WIT_ED_CRIT_Z"},{editor:"Jerónimo Pizarro",text:"Já me cansa a rua, mas não, não me cansa - tudo é rua na vida.",number:"284",path:"Fr523/inter/Fr523_WIT_ED_CRIT_P"},{editor:"Jacinto do Prado Coelho",text:"Mergulhou na sombra como quem entra na porta onde chega.",number:"485",path:"Fr306a/inter/Fr306a_WIT_ED_CRIT_C"},{editor:"Jacinto do Prado Coelho",text:"Para mim os pormenores são coisas, vozes, lettras.",number:"163",path:"Fr255/inter/Fr255_WIT_ED_CRIT_C"},{editor:"Teresa Sobral Cunha",text:"Entre mim e a vida há um vidro ténue.",number:"171",path:"Fr447/inter/Fr447_WIT_ED_CRIT_SC"},{editor:"Richard Zenith",text:"Não toquemos na vida nem com as pontas dos dedos.",number:"284",path:"Fr452/inter/Fr452_WIT_ED_CRIT_Z"},{editor:"Jerónimo Pizarro",text:"Não era isto, porém, que eu queria dizer.",number:"394",path:"Fr264/inter/Fr264_WIT_ED_CRIT_P"},{editor:"Jacinto do Prado Coelho",text:"Minha alma está hoje triste até ao corpo.",number:"167",path:"Fr269/inter/Fr269_WIT_ED_CRIT_C"},{editor:"Jacinto do Prado Coelho",text:"Eu não sei quem tu és, mas sei ao certo o que sou?",number:"254",path:"Fr285/inter/Fr285_WIT_ED_CRIT_C"},{editor:"Teresa Sobral Cunha",text:"Pasmo sempre quando acabo qualquer coisa.",number:"711",path:"Fr009/inter/Fr009_WIT_ED_CRIT_SC"},{editor:"Richard Zenith",text:"É uma oleografia sem remédio.",number:"25",path:"Fr010/inter/Fr010_WIT_ED_CRIT_Z"},{editor:"Jerónimo Pizarro",text:"Toda a vida é um somno.",number:"197",path:"Fr027/inter/Fr027_WIT_ED_CRIT_P"},{editor:"Richard Zenith",text:"Não consegui nunca ver-me de fora.",number:"338",path:"Fr028/inter/Fr028_WIT_ED_CRIT_Z"},{editor:"Jacinto do Prado Coelho",text:"Jogar ás escondidas com a nossa consciencia de viver.",number:"370",path:"Fr437/inter/Fr437_WIT_ED_CRIT_C"},{editor:"Teresa Sobral Cunha",text:"A arte livra-nos ilusoriamente da sordidez de sermos.",number:"456",path:"Fr163/inter/Fr163_WIT_ED_CRIT_SC"},{editor:"Richard Zenith",text:"As coisas sonhadas só têm o lado de cá.",number:"346",path:"Fr510/inter/Fr510_WIT_ED_CRIT_Z"},{editor:"Jerónimo Pizarro",text:"Sou uma placa photographica prolixamente impressionavel.",number:"59",path:"Fr456/inter/Fr456_WIT_ED_CRIT_P"}],o=parseInt(2*Math.random())+1,i=import.meta.url.includes("src")?"../../../":"",r=(n,e,t)=>new URL(`${i}resources/home/svg/${n}-${t}-${e}-${o}.svg`,import.meta.url).href,s=(n,e,t)=>new URL(`${i}resources/home/svg/${n}-${t}-${e}-${o}-h.svg`,import.meta.url).href,d=a[parseInt(Math.random()*a.length)],l=[{mod:"reading",path:"/reading"},{mod:"documents",path:"/source/list"},{mod:"editions",path:"/edition"},{mod:"search",path:"/search/simple"},{mod:"virtual",path:"/virtualeditions"}];var m=(e,t)=>{const a=d.path.split("/")[0],o=d.path.split("/")[2];return n(html`
    <div>
      <div class="container ldod-default">
        <a
          is="nav-to"
          to="/reading/fragment/${a}/inter/${o}"
          class="home-frag-link"
        >
          <div class="raw col-xs-12 frag-excerpt">
            <span class="frag-number font-egyptian">${d.number}</span>
            <span class="frag-editor font-condensed">${d.editor}</span>
          </div>
        </a>
        <div class="frag-excerpt-text font-grotesque">
          <p>${d.text}</p>
        </div>
        <hr class="line-points" />
        <div class="about font-monospace">
          <p id="about" class="update-language">${t.about}</p>
        </div>
        <hr class="line-x" style="background: url(${new URL(`${i}resources/home/webp/xxx.webp`,import.meta.url).href})  repeat-x 0 0" />
        <div class="menu-boxes hidden-xs col-xs-12">
          ${l.reduce(((n,{mod:t,path:a},o)=>n.concat(c(t,a,++o,"D",e))),"")}
        </div>
        <div class="menu-boxes visible-xs-inline col-xs-12">
          ${l.reduce(((n,{mod:t,path:a},o)=>n.concat(c(t,a,++o,"M",e))),"")}
        </div>
      </div>
      <home-info language=${e} class="language"></home-info>
    </div>
  `)};const c=(n,e,t,a,o)=>{const i=`0${t}`;return html` ${"01"!==i?html`<hr class="line-points" />`:""}
    <a is="nav-to" to="${e}">
      <div class="div-link">
        <img
          id="${n}"
          version=${a}
          key="${i}"
          class="not-hover"
          src=${r(a,i,o)}
          alt="${n}"
        />
        <img
          id="${n}"
          version=${a}
          key="${i}"
          class="hover"
          src=${s(a,i,o)}
          alt="${n}"
        />
      </div>
    </a>`},u=new CSSStyleSheet;u.replaceSync("p {\n    margin-block-start: 0;\n    margin-block-end: 0;\n}\n\n.container {\n    padding-right: 15px;\n    padding-left: 15px;\n    margin-right: auto;\n    margin-left: auto;\n}\n\n.container::after {\n    display: table;\n    content: '';\n    clear: both;\n}\n\n@media (min-width: 768px) {\n    .container {\n        width: 720px;\n    }\n}\n\n@media (min-width: 992px) {\n    .container {\n        width: 940;\n    }\n}\n\n@media (min-width: 1200px) {\n    .container {\n        width: 1140px;\n    }\n}\n\na[is='nav-to'] {\n    cursor: pointer;\n    background-color: transparent;\n}\n\na[is='nav-to']:active,\na[is='nav-to']:hover {\n    outline: 0;\n}\n\n:after,\n:before {\n    box-sizing: border-box;\n}\n\n.ldod-default {\n    font-family: 'Work-Sans';\n    color: black;\n    font-size: 15px;\n    line-height: 1.5;\n}\n\n.ldod-default .frag-excerpt {\n    color: #fc1b27;\n}\n\n.font-grotesque {\n    font-family: 'Work-Sans';\n}\n\n.font-egyptian {\n    font-family: 'Ultra';\n}\n\n.font-condensed {\n    font-family: 'League-Gothic';\n}\n\n.ldod-default .frag-number {\n    font-size: 80px;\n    padding-left: 8%;\n}\n\n@media (max-width: 767px) {\n    .ldod-default .frag-number {\n        font-size: 35px;\n    }\n}\n\n.ldod-default .frag-editor {\n    position: relative;\n    font-weight: 600;\n    font-size: 40px;\n    padding-left: 2%;\n    text-transform: uppercase;\n    vertical-align: top;\n    top: 16px;\n}\n\n@media (max-width: 767px) {\n    .ldod-default .frag-editor {\n        font-size: 20px;\n        top: 6px;\n    }\n}\n\n.col-xs-12 {\n    width: 100%;\n    float: left;\n    position: relative;\n    min-height: 1px;\n    padding-right: 15px;\n    padding-left: 15px;\n}\n\n.line-points {\n    border-top: 3px dotted #e71924;\n    margin-bottom: 30px;\n    margin-top: 30px;\n}\n\n@media (max-width: 767px) {\n    .line-points {\n        margin-bottom: 20px;\n        margin-top: 20px;\n    }\n}\n\n.frag-excerpt:hover {\n    color: #0c4ef6;\n}\n\n.frag-excerpt-text p {\n    font-weight: 600;\n    font-size: 50px;\n    line-height: 58px;\n}\n\n@media (max-width: 767px) {\n    .ldod-default .frag-excerpt-text p {\n        font-weight: 600;\n        font-size: 32px;\n        line-height: 36px;\n    }\n}\n\n.ldod-default .about {\n    margin-top: 30px;\n    margin-bottom: 40px;\n}\n\n.font-monospace {\n    font-family: 'Space-Mono';\n}\n\n.ldod-default .about p {\n    color: #0c4ef6;\n    font-size: 36px;\n    line-height: 48px;\n}\n\np {\n    margin: 0 0 10px;\n}\n\n.ldod-default .line-x {\n    height: 20px;\n    border: 0;\n}\n\nhr {\n    margin-top: 20px;\n    margin-bottom: 20px;\n    border: 0;\n    border-top: 1px solid #eee;\n}\n\nhr {\n    height: 0;\n    -webkit-box-sizing: content-box;\n    -moz-box-sizing: content-box;\n    box-sizing: content-box;\n}\n\n.ldod-default .menu-boxes {\n    position: relative;\n    display: block;\n    padding: 0;\n    margin-top: 20px;\n}\n\n.ldod-default .menu-boxes img {\n    position: relative;\n    width: 100%;\n}\n\nimg {\n    vertical-align: middle;\n    border: 0;\n}\n\n@media (max-width: 767px) {\n    .ldod-default .about p {\n        font-size: 20px;\n        line-height: 25px;\n        margin-top: 0px;\n        margin-bottom: 0px;\n    }\n}\n\n@media (max-width: 767px) {\n    .ldod-default .menu-boxes {\n        margin-top: 7px;\n    }\n}\n\n.visible-xs-inline {\n    display: none !important;\n}\n\n@media (max-width: 767px) {\n    .hidden-xs {\n        display: none !important;\n    }\n}\n\n@media (max-width: 767px) {\n    .visible-xs-inline {\n        display: inline !important;\n    }\n}\n\n.ldod-default .div-link img:nth-child(2) {\n    display: none;\n}\n\n.ldod-default .div-link:hover img:nth-child(2) {\n    display: inline;\n}\n\n.ldod-default .div-link:hover img:nth-child(1) {\n    display: none;\n}");var h=u;window.html=String.raw;const p=new CSSStyleSheet({}),g=async n=>(await function(n){switch(n){case"../../../resources/home/constants/constants-en.js":return import("./constants-en-95c2516b.js");case"../../../resources/home/constants/constants-es.js":return import("./constants-es-16872f63.js");case"../../../resources/home/constants/constants-pt.js":return import("./constants-pt-0aac0533.js");default:return new Promise((function(e,t){("function"==typeof queueMicrotask?queueMicrotask:setTimeout)(t.bind(null,new Error("Unknown variable dynamic import: "+n)))}))}}(`../../../resources/home/constants/constants-${n}.js`)).default;class x extends HTMLElement{constructor(){super(),this.constants=void 0,this.onChangedLanguageEvent=this.onChangedLanguageEvent.bind(this);this.attachShadow({mode:"open"}).adoptedStyleSheets=[p]}static get observedAttributes(){return["language"]}get language(){return this.getAttribute("language")}set language(n){this.setAttribute("language",n)}async connectedCallback(){p.replaceSync(h),p.cssRules.length||(this.shadowRoot.adoptedStyleSheets=[h]),window.addEventListener("ldod-language",this.onChangedLanguageEvent),await this.setConstants(),this.render()}disconnectedCallback(){window.removeEventListener("ldod-language",this.onChangedLanguageEvent)}onChangedLanguageEvent({detail:{language:n}}){this.setAttribute("language",n)}languageUpdate=()=>this.shadowRoot.querySelectorAll(".language").forEach((n=>n.language=this.language));async attributeChangedCallback(n,e,t){"language"===n&&(await this.setConstants(),this.componentsTextContextUpdate(),this.boxesUpdate(),this.languageUpdate())}componentsTextContextUpdate(){this.shadowRoot.querySelectorAll(".update-language").forEach((n=>n.innerHTML=this.constants[n.id]))}boxesUpdate(){this.shadowRoot.querySelectorAll("div.div-link>img.not-hover").forEach((n=>n.setAttribute("src",r(n.getAttribute("version"),n.getAttribute("key"),this.language)))),this.shadowRoot.querySelectorAll("div.div-link>img.hover").forEach((n=>n.setAttribute("src",s(n.getAttribute("version"),n.getAttribute("key"),this.language))))}async setConstants(){this.constants=await g(this.language)}render(){this.shadowRoot.appendChild(m(this.language,this.constants))}}customElements.define("home-mfe",x);const b=(e,t)=>{const a=n(html`<home-mfe language=${e}></home-mfe>`);document.querySelector(t).appendChild(a)},f=()=>{document.querySelector("home-mfe").remove()};export{x as default,b as mount,f as unMount};
