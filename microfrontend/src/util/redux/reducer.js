
const initialState = {
    user : {},
    data: {},
    language: "pt",
    modules: [
        {
            name: "about",
            active: true,
            pt:"ACERCA",
            en: "ABOUT",
            es: "ACERCA",
            pages: ["ARQUIVO LDOD", "VÍDEOS", "TUTORIAIS E OFICINAS", "PERGUNTAS FREQUENTES"]
        },
        {
            name: "reading",
            active: true,
            pt:"LEITURA",
            en: "READING",
            es: "LECTURA",
            pages: ["SEQUENCIA DE LEITURA", "LIVRO VISUAL", "CITACOES NO TWITTER"]
        },
        {
            name: "documents",
            active: true,
            pt:"DOCUMENTOS",
            en: "DOCUMENTS",
            es: "DOCUMENTOS",
            pages: ["SEQUENCIA DE LEITURA", "LIVRO VISUAL", "CITACOES NO TWITTER"]
        },
        {
            name: "editions",
            active: true,
            pt:"EDIÇÕES",
            en: "EDITIONS",
            es: "ADICIONES",
            pages: ["SEQUENCIA DE LEITURA", "LIVRO VISUAL", "CITACOES NO TWITTER"]
        },
        {           
            name: "search",
            active: true,
            pt:"PESQUISA",
            en: "SEARCH",
            es: "BÚSQUEDA",
            pages: ["SEQUENCIA DE LEITURA", "LIVRO VISUAL", "CITACOES NO TWITTER"]
        },
        {
            name: "virtual",
            active: true,
            pt: "VIRTUAL",
            en: "VIRTUAL",
            es: "VIRTUAL",
            pages: ["SEQUENCIA DE LEITURA", "LIVRO VISUAL", "CITACOES NO TWITTER"]
        },
        {
            name: "admin",
            active: false,
            pt: "ADMIN",
            en: "ADMIN",
            es: "ADMIN",
            pages: ["SEQUENCIA DE LEITURA", "LIVRO VISUAL", "CITACOES NO TWITTER"]
        }
    ]
}
export default function(state = initialState, action) {
    switch (action.type) {
        case "SET_USER" : {
            return {
                ...state,
                user : action.payload.user
            }
            
        }
        case "SET_DATA" : {
            return {
                ...state,
                data : action.payload.data
            }
            
        }
        default: 
            return state;
    }
}
