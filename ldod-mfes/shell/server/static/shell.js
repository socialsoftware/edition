import{Store as e}from"shared/store.js";const t=new e({language:"en",token:""},{storageName:"ldod-store",keys:["language","token"]});window.addEventListener("ldod-language",(({detail:e})=>t.setState(e))),window.html=String.raw;const a=()=>t.getState().language;t.subscribe(((e,t)=>{e.language!==t.language&&(r.language=e.language)}));const n=JSON.parse(document.querySelector("script#importmap").textContent).imports;delete n["shared/"],document.querySelector("ldod-navbar").setAttribute("language",a());const o=await Object.keys(n).reduce((async(e,t)=>{const a=(await import(t))?.default??"",n=a.path;return n&&((await e)[n]=()=>a),await e}),Promise.resolve({})),r=document.createElement("ldod-router");r.id="shell",r.language=a(),r.routes=o,document.getElementById("root").appendChild(r);
