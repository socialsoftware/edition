import { 
    ACCESS_TOKEN, 
    API_BASE_URL, 
    API_LOGIN_URL,
    API_USER_URL, 
    API_SERVICES_URL, 
    FRAGMENT_LIST_SIZE
} 
from './Constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response =>
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

/*
   This method is used only for testing requests
*/
export function ping(){
    return request({
        url: API_BASE_URL + "/ping",
        method: 'GET'
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + API_LOGIN_URL,
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function socialLogin(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/social",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function social() {
    return request({
        url: 'http://localhost:8080/api/auth/social',
        method: 'GET',
        credentials: 'include',
    });
}

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + API_USER_URL,
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: API_BASE_URL + API_USER_URL + '/' + username,
        method: 'GET'
    });
}

export function getVirtualEditionIndex(acronym) {
    return request({
        url: API_BASE_URL + API_SERVICES_URL + "/edition/" + acronym + "/index",
        method: 'GET'
    });
}

export function getVirtualEditionFragment(acronym, urlId) {
    return request({
        url: API_BASE_URL + API_SERVICES_URL + "/edition/" + acronym + "/inter/" + urlId,
        method: 'GET'
    });
}
