import{navigateTo as r}from"http://localhost:9000/ldod-mfes/shared/router.js";import{t as m,l as c,e as i}from"./index.c7cd3be2.js";import"http://localhost:9000/ldod-mfes/shared/vanilla-jsx.js";import"http://localhost:9000/ldod-mfes/shared/store.js";import"http://localhost:9000/ldod-mfes/shared/fetcher.js";const M=async(e,p)=>{const o=(t,a)=>c(e).then(s=>i(s[t],a));let n=`/auth?token=${new URL(document.location).searchParams.get("token")}`;m(n).then(({message:t})=>o(t)).catch(({message:t})=>o(t),"error"),r("/")},d=()=>console.log("unmount");export{M as mount,d as unMount};
