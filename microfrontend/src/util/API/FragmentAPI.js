import { API_BASE_URL, ACCESS_TOKEN } from '../../constants/index';
import axios from 'axios'

export function getFragmentWithXml (xmlId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}`)
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
    console.log(obj);
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