import { staticPath } from "./constants.js";
import fs from "fs"

export async function processReferences() {
  let mfes = global.globalMfes;
  const references = await JSON.parse(mfes).reduce(async (refs, mfe) => {
    let result = await refs;
    const entryPoint = `${staticPath}/${mfe}/${mfe}.js`;
    const api = (await import(entryPoint).catch(e => { }))
    if (!api) return

    result = { ...result, [mfe]: api.default.references }
    return result
  }, Promise.resolve({}))



  fs.writeFileSync(`${staticPath}/references.js`, `
      if (typeof window !== "undefined") {
        window.references = {
          ${Object.keys(references).map((key) => {
    if (!references[key]) return `${key}: ${null}`
    return `${key}: {
        ${Object.entries(references[key]).map(([fnKey, fn]) => {
      return `${fnKey}: ${fn}`
    }).join(",\n")}
      }`;
  }).join(",\n")}
        }
      }`)

  return await references;
}