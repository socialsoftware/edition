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

export function getFragmentList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/fragments`)
}
export function getSourceList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/sources`)
}

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return axios.get(API_BASE_URL + `/api/user`, {
        headers: {
            'Content-type' : 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}