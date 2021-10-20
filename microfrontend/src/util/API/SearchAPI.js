import { API_BASE_URL } from '../../constants/index.production';
import axios from 'axios'

export function getSimpleSearchList(data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/search/simple/result`, data, {
        headers: {
            'Content-Type': 'text/plain;charset=UTF-8',
        }
        } 
    )
}

export function getAdvancedSearchList(data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/search/advanced/result`, data, {
        headers: {
            'Content-Type': 'application/json',
        }
        } 
    )
}

export function getVirtualEditionMap (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/search/getVirtualEditions`)
}