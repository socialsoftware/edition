import { API_BASE_URL, ACCESS_TOKEN } from '../../constants/index.production';
import axios from 'axios'


export function login(loginRequest) {
    return axios.post(API_BASE_URL + `/api/auth/signin`,loginRequest)
}

export function signup(signupRequest) {
    return axios.post(API_BASE_URL + `/api/auth/signup`,signupRequest)
}

export function changePassword(changeRequest) {
    return axios.post(API_BASE_URL + `/api/auth/changePassword`,changeRequest)
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