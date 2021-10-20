import { API_BASE_URL, ACCESS_TOKEN } from '../../constants/index.production';
import axios from 'axios'

export function getPublicAllEditions() {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/public/getAllEditions`, {
        headers: {
            Authorization: null
        }
    })
}

export function getAllEditions() {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/getAllEditions`, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function submitParticipation(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants/submit`, null, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function cancelParticipation(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants/cancel`, null, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function createEdition(acronym, title, pub, use) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/create/${acronym}/${title}/${pub}/${use}`, null, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function getManagePage(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/manage/${externalId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function getEditInfo(externalId, obj) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/edit/${externalId}`, 
        `acronym=${obj.acronym}&title=${obj.title}&synopsis=${obj.synopsis}&pub=${obj.pub}&management=${obj.management}&vocabulary=${obj.vocabulary}&annotation=${obj.annotation}&mediasource=${obj.mediasource}&begindate=${obj.begindate}&enddate=${obj.enddate}&geolocation=${obj.geolocation}&frequency=${obj.frequency}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getParticipantsPage(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function addNewMember(externalId, username) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants/add`, 
        `username=${username}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function removeMember(externalId, memberId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants/remove`, 
        `userId=${memberId}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function changeRole(externalId, username) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants/role`, 
        `username=${username}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function approveMember(externalId, username) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/participants/approve`, 
        `username=${username}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function getRecommendationPage(externalId) {
    console.log(externalId)
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/recommendation/${externalId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function setCriteriaChange(data, externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/linear/${externalId}`, 
        data, 
        {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function saveNewInters(acronym, inters, externalId) {
    console.log(externalId);
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/linear/save/${externalId}`, 
        `acronym=${acronym}&inter[]=${inters}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function getManualData(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/recommendation/${externalId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function reorderCurrentList(externalId, inters) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/reorder/${externalId}`, 
        `fraginters=${inters}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function getTaxonomyData(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/taxonomy`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function createVirtualCategory(externalId, name) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/category/create`, 
        `externalId=${externalId}&name=${name}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}



export function mergeOrDeleteVirtualCategory(taxonomyId, type, externalId, categoriesId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/category/mulop`, 
        `taxonomyId=${taxonomyId}&type=${type}&externalId=${externalId}&categories[]=${categoriesId}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function generateTopics(externalId, topics, words, threshold, iterations) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/taxonomy/generateTopics`, 
        `numTopics=${topics}&numWords=${words}&thresholdCategories=${threshold}&numIterations=${iterations}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function addGeneratedTopics(externalId, dto) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/${externalId}/taxonomy/createTopics`, 
        dto, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function getCategoryData(categoryId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/category/${categoryId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function deleteVirtualCategory(categoryId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/category/delete`, 
        `categoryId=${categoryId}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function updateVirtualCategory(categoryId, name) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/category/update`, 
        `categoryId=${categoryId}&name=${name}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function extractCategories(categoryId, inters) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/category/extract`, 
        `categoryId=${categoryId}&inters[]=${inters}`, 
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function getFragInterData(fragInterId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/restricted/fraginter/${fragInterId}`, 
    {
        headers: {
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function deleteVirtualEdition(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/restricted/delete`, 
        `externalId=${externalId}`,
        {
            headers: {
                Accept: 'application/json',
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
    })
}