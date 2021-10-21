import { API_BASE_URL, ACCESS_TOKEN } from '../../constants/index.production';
import axios from 'axios'


///////////////////// VIRTUAL ////////////////

export function getVirtualFragmentWithXml (xmlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getVirtualFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/inter/${urlId}`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getVirtualFragmentWithXmlNoUser (xmlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/NoUser`, arraySelected, {
        headers: {
            'Content-Type': 'application/json',
        }
    } )
}

export function getVirtualFragmentWithXmlAndUrlNoUser (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/inter/${urlId}/noUser`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
            }
        } 
    )
}

export function getNextVirtualFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/inter/${urlId}/nextFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getNextVirtualFragmentWithXmlAndUrlNoUSER (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/inter/${urlId}/nextFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: null
            }
        } 
    )
}

export function getPrevVirtualFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/inter/${urlId}/prevFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getPrevVirtualFragmentWithXmlAndUrlNoUSER (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/${xmlId}/inter/${urlId}/prevFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
            }
        } 
    )
}

export function getVirtualIntersByArrayExternalId(fragmentExternalId, arrayInterId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/inter`, 
        `fragment=${fragmentExternalId}&inters[]=${arrayInterId}&selectedVE=${arraySelected}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getVirtualIntersByArrayExternalIdNoUser(fragmentExternalId, arrayInterId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/virtual/inter/noUser`, 
        `fragment=${fragmentExternalId}&inters[]=${arrayInterId}&selectedVE=${arraySelected}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}
//////////////////////////////////////////////
export function getFragmentWithXml (xmlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}`, arraySelected, 
    {
        headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    }
    )
}
export function getFragmentWithXmlNoUser (xmlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/NoUser`, arraySelected, {
        headers: {
            'Content-Type': 'application/json',
        }
    } )
}

export function getFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}
export function getFragmentWithXmlAndUrlNoUser (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/noUser`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
            }
        } 
    )
}

export function getNextFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/nextFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getNextFragmentWithXmlAndUrlNoUSER (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/nextFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: null
            }
        } 
    )
}

export function getPrevFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/prevFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getPrevFragmentWithXmlAndUrlNoUSER (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/prevFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: null
            }
        } 
    )
}



export function getIntersByArrayExternalId(fragmentExternalId, arrayInterId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter`, 
        `fragment=${fragmentExternalId}&inters[]=${arrayInterId}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function getInterWithDiff(interID, displayDiff) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter/editorial`, 
        `interp[]=${interID}&diff=${displayDiff}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function getAuthorialInterWithDiffs(interID, obj) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter/authorial`, 
        `interp[]=${interID}&diff=${obj["diff"]}&del=${obj["del"]}&ins=${obj["ins"]}&subst=${obj["subst"]}&notes=${obj["notes"]}&facs=${obj["facs"]}&pb=${obj["pb"]}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function getInterCompare(interID, obj) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter/compare`, 
        `inters[]=${interID}&line=${obj["line"]}&spaces=${obj["spaces"]}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function addToEdition(veId, interId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/restricted/addinter/${veId}/${interId}`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}