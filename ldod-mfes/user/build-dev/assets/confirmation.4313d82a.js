import{navigateTo as r}from"http://localhost:9000/ldod-mfes/shared/router.js";import{j as i,l as m,e as c}from"./index.148fe27f.js";import"http://localhost:9000/ldod-mfes/shared/vanilla-jsx.js";import"http://localhost:9000/ldod-mfes/shared/store.js";import"http://localhost:9000/ldod-mfes/shared/fetcher.js";const M=async(n,p)=>{const o=(t,a)=>m(n).then(s=>c(s[t],a));let e=`/sign-up-confirmation?token=${new URL(document.location).searchParams.get("token")}`;await i(e).then(({message:t})=>o(t)).catch(({message:t})=>o(t),"error"),r("/user/signin")},d=()=>console.log("unmount"),v="/sign-up-confirmation";export{M as mount,v as path,d as unMount};
