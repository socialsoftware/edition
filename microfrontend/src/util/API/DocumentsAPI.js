import { API_BASE_URL, ACCESS_TOKEN } from '../../constants/index';
import axios from 'axios'

export function getFragmentList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/documents/fragments`)
}
export function getSourceList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/documents/sources`)
}