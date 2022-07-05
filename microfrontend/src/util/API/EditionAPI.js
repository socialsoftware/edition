import { API_BASE_URL } from '../../constants/index.production';
import axios from 'axios'

export function getUserContributions (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/user/${val}`)
}

export function getEdition (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/acronym/${val}`)
}

export function getExpertEditionList (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/expert/acronym/${val}`)
}

export function getVirtualEditionList (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/virtual/acronym/${val}`)
}

export function getTaxonomyList (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/acronym/${val}/taxonomy`)
}

export function getCategoryList (val, val2) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/acronym/${val}/category/${val2}`)
}

export function getClassificationContent (val, val2) {
    return axios.get(API_BASE_URL + `/api/microfrontend/edition/game/${val}/classificationGame/${val2}`,
        )
}

