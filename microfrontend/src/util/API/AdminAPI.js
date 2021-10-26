import { API_BASE_URL, ACCESS_TOKEN } from '../../constants/index.production';
import axios from 'axios'

export function loadCorpus(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/load/corpus`, 
        file, 
        {
            headers: {
                'Content-type': 'application/json',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function loadFragment(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/load/fragmentsAtOnce`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function loadSeveralFragment(files) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/load/fragmentsStepByStep`, 
        files, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function loadUsers(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/load/users`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}
export function loadVirtualCorpus(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/load/virtual-corpus`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}
export function loadVirtualFragments(files) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/load/virtual-fragments`, 
        files, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function exportRandom() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/exportRandom`, 
    {
        responseType:"blob"
    })
}
export function exportUsers() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/export/users`, 
    {
        responseType:"blob"
    })
}
export function exportAll() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/exportAll`, 
    {
        responseType:"blob"
    })
}
export function exportVirtualEditions() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/export/virtualeditions`, 
    {
        responseType:"blob"
    })
}
export function getFragmentsByQuery(query) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/exportSearch`, 
        `query=${query}`, 
    )
}
export function exportFragmentsByQuery(query) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/exportSearchResult`, 
        `query=${query}`, 
    )
}

export function getFragmentDeleteList() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/fragment/list`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteSingleFragment(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/fragment/delete`, 
        `externalId=${externalId}`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}
export function deleteAllFragment() {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/fragment/deleteAll`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getUserList() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/user/list`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function changeActiveUser(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/user/active`,
        `externalId=${externalId}`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteUser(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/user/delete`,
        `externalId=${externalId}`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteUserSessions() {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/sessions/delete`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function switchAdmin() {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/switch`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getUserForm(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/user/edit`,
        {   
            params: {
                externalId: externalId
            },
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function setUserForm(form) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/user/edit`,
        form,
        {   
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getAdminVirtualList() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/virtual/list`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteAdminVirtualEdition(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/admin/virtual/delete`,
        `externalId=${externalId}`,
    {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function getTweets() {
    return axios.get(API_BASE_URL + `/api/microfrontend/admin/tweets`,
        {
            headers:{
                'Content-type': 'application/json',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}