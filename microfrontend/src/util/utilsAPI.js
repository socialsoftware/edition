import { API_BASE_URL, ACCESS_TOKEN } from '../constants/index';
import axios from 'axios'


/////////////////////// USER ///////////////////////
export function login(loginRequest) {
    return axios.post(API_BASE_URL + `/api/auth/signin`,loginRequest)
}

export function signup(signupRequest) {
    return axios.post(API_BASE_URL + `/api/auth/signup`,signupRequest)
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

/////////////////////// EDITION ///////////////////////
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

/////////////////////// DOCUMENTS ///////////////////////
export function getFragmentList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/fragments`)
}
export function getSourceList () {
    return axios.get(API_BASE_URL + `/api/microfrontend/sources`)
}


/////////////////////// READING ///////////////////////
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

/////////////////////// SEARCH ///////////////////////
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

export function getVirtualEditionMap (val) {
    return axios.get(API_BASE_URL + `/api/microfrontend/getVirtualEditions`)
}

/////////////////////// FRAGMENT ///////////////////////
export function getFragmentWithXml (xmlId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}`)
}

export function getFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
            }
        } 
    )
}

export function getNextFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/nextFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
            }
        } 
    )
}

export function getPrevFragmentWithXmlAndUrl (xmlId, urlId, arraySelected) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/${xmlId}/inter/${urlId}/prevFrag`, arraySelected, {
            headers: {
                'Content-Type': 'application/json',
            }
        } 
    )
}

export function getIntersByArrayExternalId(fragmentExternalId, arrayInterId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter`, 
        `fragment=${fragmentExternalId}&inters[]=${arrayInterId}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function getInterWithDiff(interID, displayDiff) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter/editorial`, 
        `interp[]=${interID}&diff=${displayDiff}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function getAuthorialInterWithDiffs(interID, obj) {
    console.log(obj);
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter/authorial`, 
        `interp[]=${interID}&diff=${obj["diff"]}&del=${obj["del"]}&ins=${obj["ins"]}&subst=${obj["subst"]}&notes=${obj["notes"]}&facs=${obj["facs"]}&pb=${obj["pb"]}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}

export function getInterCompare(interID, obj) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/inter/compare`, 
        `inters[]=${interID}&line=${obj["line"]}&spaces=${obj["spaces"]}`, 
        {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        } 
    )
}
//////////////////// VIRTUAL ////////////////////////
export function getPublicAllEditions() {
    return axios.get(API_BASE_URL + `/api/microfrontend/public/getAllEditions`)
}

export function getAllEditions() {
    return axios.get(API_BASE_URL + `/api/microfrontend/getAllEditions`, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function submitParticipation(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants/submit`, null, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function cancelParticipation(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants/cancel`, null, {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function createEdition(acronym, title, pub, use) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/create/${acronym}/${title}/${pub}/${use}`, null, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function getManagePage(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/manage/${externalId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function getEditInfo(externalId, obj) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/edit/${externalId}`, 
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
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function addNewMember(externalId, username) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants/add`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants/remove`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants/role`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/participants/role`, 
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
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/recommendation/${externalId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function setCriteriaChange(data) {
    return axios.post(API_BASE_URL + `/api/microfrontend/linear`, 
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

export function saveNewInters(acronym, inters) {
    return axios.post(API_BASE_URL + `/api/microfrontend/linear/save`, 
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
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/recommendation/${externalId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function reorderCurrentList(externalId, inters) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/reorder/${externalId}`, 
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
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/taxonomy`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function createVirtualCategory(externalId, name) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/category/create`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/category/mulop`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/taxonomy/generateTopics`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/${externalId}/taxonomy/createTopics`, 
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
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/category/${categoryId}`, 
    {
        headers: {
            'Content-type': 'application/json',
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

export function deleteVirtualCategory(categoryId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/category/delete`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/category/update`, 
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
    return axios.post(API_BASE_URL + `/api/microfrontend/restricted/category/extract`, 
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
    return axios.get(API_BASE_URL + `/api/microfrontend/restricted/fraginter/${fragInterId}`, 
    {
        headers: {
            Accept: 'application/json',
            Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
        }
    })
}

///////////////////// ADMIN /////////////////////
export function loadCorpus(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/load/corpus`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function loadFragment(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/load/fragmentsAtOnce`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function loadSeveralFragment(files) {
    return axios.post(API_BASE_URL + `/api/microfrontend/load/fragmentsStepByStep`, 
        files, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function loadUsers(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/load/users`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}
export function loadVirtualCorpus(file) {
    return axios.post(API_BASE_URL + `/api/microfrontend/load/virtual-corpus`, 
        file, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}
export function loadVirtualFragments(files) {
    return axios.post(API_BASE_URL + `/api/microfrontend/load/virtual-fragments`, 
        files, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        }
    )
}

export function exportRandom() {
    return axios.get(API_BASE_URL + `/api/microfrontend/exportRandom`, 
    {
        responseType:"blob"
    })
}
export function exportUsers() {
    return axios.get(API_BASE_URL + `/api/microfrontend/export/users`, 
    {
        responseType:"blob"
    })
}
export function exportAll() {
    return axios.get(API_BASE_URL + `/api/microfrontend/exportAll`, 
    {
        responseType:"blob"
    })
}
export function exportVirtualEditions() {
    return axios.get(API_BASE_URL + `/api/microfrontend/export/virtualeditions`, 
    {
        responseType:"blob"
    })
}
export function getFragmentsByQuery(query) {
    return axios.post(API_BASE_URL + `/api/microfrontend/exportSearch`, 
        `query=${query}`, 
    )
}
export function exportFragmentsByQuery(query) {
    return axios.post(API_BASE_URL + `/api/microfrontend/exportSearchResult`, 
        `query=${query}`, 
    )
}

export function getFragmentDeleteList() {
    return axios.get(API_BASE_URL + `/api/microfrontend/fragment/list`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteSingleFragment(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/delete`, 
        `externalId=${externalId}`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}
export function deleteAllFragment() {
    return axios.post(API_BASE_URL + `/api/microfrontend/fragment/deleteAll`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getUserList() {
    return axios.get(API_BASE_URL + `/api/microfrontend/user/list`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function changeActiveUser(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/user/active`,
        `externalId=${externalId}`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteUser(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/user/delete`,
        `externalId=${externalId}`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteUserSessions() {
    return axios.post(API_BASE_URL + `/api/microfrontend/sessions/delete`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function switchAdmin() {
    return axios.post(API_BASE_URL + `/api/microfrontend/switch`,
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getUserForm(externalId) {
    return axios.get(API_BASE_URL + `/api/microfrontend/user/edit`,
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
    return axios.post(API_BASE_URL + `/api/microfrontend/user/edit`,
        form,
        {   
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getAdminVirtualList() {
    return axios.get(API_BASE_URL + `/api/microfrontend/virtual/list`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function deleteAdminVirtualEdition(externalId) {
    return axios.post(API_BASE_URL + `/api/microfrontend/virtual/delete`, 
        {
            params : {
                externalId : externalId
            },
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}

export function getTweets() {
    return axios.get(API_BASE_URL + `/api/microfrontend/tweets`, 
        {
            headers: {
                Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`
            }
        } 
    )
}