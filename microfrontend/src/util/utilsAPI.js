import { API_BASE_URL, ACCESS_TOKEN } from '../constants/index';
import axios from 'axios'

export function login(loginRequest) {
    return axios.post(API_BASE_URL + `/api/auth/signin`,loginRequest)
}

export function signup(signupRequest) {
    return axios.post(API_BASE_URL + `/api/auth/signup`,signupRequest)
}

/* export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
} */
export function getUserContributions (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/user/${val}`)
}

export function getEdition (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/acronym/${val}`)
}

export function getExpertEditionList (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/expert/acronym/${val}`)
}

export function getVirtualEditionList (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/acronym/${val}`)
}

export function getTaxonomyList (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/acronym/${val}/taxonomy`)
}

export function getCategoryList (val, val2) {
    return axios.get(API_BASE_URL + `/api/microfrontend/acronym/${val}/category/${val2}`)
}


export function getFragmentList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/fragments`)
}
export function getSourceList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/sources`)
}

export function getVirtualEditionMap (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/getVirtualEditions`)
}

export function getReadingExperts () {
    return axios.get(API_BASE_URL + `/api/microfrontend/reading`)
}
export function getStartReadingFragment (acronym, data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/edition/${acronym}/start`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}
export function getCurrentReadingFragment (val, val1) {
    return axios.get(API_BASE_URL + `/api/microfrontend/reading/fragment/${val}/inter/${val1}`)
}


export function getCurrentReadingFragmentJson(val, val1, data) {

    return axios.post(API_BASE_URL + `/api/microfrontend/reading/fragment/${val}/interJson/${val1}`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        } 
    )
}

export function getNextReadingFragment (val, val1, data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${val}/inter/${val1}/next`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}
export function getPrevReadingFragment (val, val1, data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${val}/inter/${val1}/prev`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}

export function resetPrevRecom (data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/inter/prev/recom/reset`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}
export function getPrevRecom (data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/inter/prev/recom`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}

export function getTwitterCitations () {
    return axios.get(API_BASE_URL + `/api/microfrontend/citations`)
}

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return axios.get(API_BASE_URL + `/api/user`, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function getSimpleSearchList(data) {

    return axios.post(API_BASE_URL + `/api/microfrontend/simple/result`, data, {
        headers: {
            'Content-Type': 'text/plain;charset=UTF-8',
        }
        } 
    )
}

export function getAdvancedSearchList(data) {

    return axios.post(API_BASE_URL + `/api/microfrontend/advanced/result`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        } 
    )
}