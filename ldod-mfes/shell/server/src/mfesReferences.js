import { staticPath } from "./constants.js";
import fs from "fs"
import { loadMfes } from "./mfes.js";

export async function processReferences() {
  let mfes = loadMfes();
  const references = await mfes.reduce(async (refs, mfe) => {
    let result = await refs;
    const entryPoint = `${staticPath}/${mfe}/${mfe}.js`;
    const api = (await import(entryPoint).catch(e => { }))
    if (!api) return

    result = { ...result, [mfe]: api.default.references }
    return result
  }, Promise.resolve({}))



  fs.writeFileSync(`${staticPath}/references.js`, `
      console.time("loading refs to window");
      if (typeof window !== "undefined") {
        window.references = {
          ${Object.keys(references || []).map((key) => {
    if (!references[key]) return `${key}: ${null}`
    return `${key}: {
        ${Object.entries(references[key]).map(([fnKey, fn]) => {
      return `${fnKey}: ${fn}`
    }).join(",\n")}
      }`;
  }).join(",\n")}
        }
      }
      console.timeEnd("loading refs to window");
      `)

  global.globalReferences = await references;
}