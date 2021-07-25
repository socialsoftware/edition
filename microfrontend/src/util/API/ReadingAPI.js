import { API_BASE_URL } from '../../constants/index.production';
import axios from 'axios'

export function getLdoDVisual () {
    return axios.get(API_BASE_URL + `/ldod-visual`)
}

export function getReadingExperts () {
    return axios.get(API_BASE_URL + `/api/microfrontend/reading`)
}
export function getStartReadingFragment (acronym, data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/reading/edition/${acronym}/start`, data, {
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
    return axios.post(API_BASE_URL + `/api/microfrontend/reading/fragment/${val}/inter/${val1}/next`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}
export function getPrevReadingFragment (val, val1, data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/reading/fragment/${val}/inter/${val1}/prev`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}


export function resetPrevRecom (data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/reading/inter/prev/recom/reset`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}
export function getPrevRecom (data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/reading/inter/prev/recom`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        })
}

export function getTwitterCitations () {
    return axios.get(API_BASE_URL + `/api/microfrontend/reading/citations`)
}
