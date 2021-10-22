
const initialState = {
    recommendation: {
        read: [],
        heteronymWeight: 0,
        dateWeight: 0,
        textWeight: 1,
        taxonomyWeight: 0,
        currentInterpretation: null,
        prevRecomendation: null
    },
    selectedVEAcr : ["LdoD-JPC-anot", "LdoD-Jogo-Class", "LdoD-Mallet", "LdoD-Twitter"],
    language: "pt",
    modules: [
        {
            name: "header_about",
            active: true,
            pages: [{id:"header_archive", route:"/about/archive"},
                    {id:"header_videos", route:"/about/videos"},
                    {id:"header_tutorials", route:"/about/tutorials"},
                    {id:"header_faq", route:"/about/faq"},
                    {id:"header_encoding", route:"/about/encoding"},
                    {id:"header_bibliography", route:"/about/articles"},
                    {id:"header_conduct", route:"/about/conduct"},
                    {id:"header_privacy", route:"/about/privacy"},
                    {id:"header_team", route:"/about/team"},
                    {id:"header_acknowledgements", route:"/about/acknowledgements"},
                    {id:"header_contact", route:"/about/contact"},
                    {id:"header_copyright", route:"/about/copyright"}]
        },
        {
            name: "general_reading",
            active: true,
            pages: [{id:"general_reading_sequences", route:"/reading"},
                    {id:"general_reading_visual", route:"/reading/ldod-visual"},
                    {id:"general_citations_twitter", route:"/reading/citations"}]
        },
        {
            name: "header_documents",
            active: true,
            pages: [{id:"authorial_source", route:"/documents/source/list"},
                    {id:"fragment_codified", route:"/documents/fragments"}]
        },
        {
            name: "header_editions",
            active: true,
            pages: [{id:"general_editor_prado", route:"/edition/acronym/JPC"},
                    {id:"general_editor_cunha", route:"/edition/acronym/TSC"},
                    {id:"general_editor_zenith", route:"/edition/acronym/RZ"},
                    {id:"general_editor_pizarro", route:"/edition/acronym/JP"},
                    {id:"header_title", route:"/edition/acronym/LdoD-Arquivo"}]
        },
        {           
            name: "header_search",
            active: true,
            pages: [{id:"header_search_simple", route:"/search/simple"},
                    {id:"header_search_advanced", route:"/search/advanced"}]
        },
        {
            name: "header_virtual",
            active: true,
            pages: [{id:"header_virtualeditions", route:"/virtual/virtualeditions"},
                    {id:"general_classificationGame", route:"/virtual/classificationGames"}]
        },
        {
            name: "header_admin",
            active: false,
            pages: [{id:"load", route:"/admin/loadForm"},
                    {id:"general_export", route:"/admin/exportForm"},
                    {id:"fragment_delete", route:"/admin/fragment/list"},
                    {id:"user_manage", route:"/admin/user/list"},
                    {id:"virtual_editions_manage", route:"/admin/virtual/list"},
                    {id:"twitter_manage", route:"/admin/tweets"}]
        }
    ]
}
// eslint-disable-next-line import/no-anonymous-default-export
export default function(state = initialState, action) {
    switch (action.type) {
        case "SET_USER" : {
            if(action.payload.roles.includes("ADMIN")){
                var aux = [...state.modules]
                aux[6].active=true
            }
            return {
                ...state,
                modules : aux
            }
            
        }
        case "LOGOUT" : {
            let aux = [...initialState.modules]
            aux[6].active=false
            let aux1 = [...initialState.selectedVEAcr]
            aux1 = ["LdoD-JPC-anot", "LdoD-Jogo-Class", "LdoD-Mallet", "LdoD-Twitter"]
            return {
                ...state,
                modules : aux,
                selectedVEAcr: aux1
            }
        }
        case "SET_REC" : {
            return {
                ...state,
                recommendation : action.payload
            }
        }
        case "UPDATE_SELECTED" : {
            let newArray = [...state.selectedVEAcr]
            if(newArray.includes(action.payload)){
                newArray.splice(newArray.indexOf(action.payload), 1)
            }
            else{
                newArray.push(action.payload)
            }
            return {
                ...state,
                selectedVEAcr : newArray
            }
            
        }
        case "REMOVE_FROM_SELECTED" : {
            console.log(action.payload)
            let newArray = [...state.selectedVEAcr]
            newArray.splice(newArray.indexOf(action.payload), 1)
            return {
                ...state,
                selectedVEAcr: newArray
            }
        }
        case "RESET" : {
            return initialState
        }
        

        default: 
            return state;
    }
}
