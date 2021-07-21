import React, {useEffect, useState} from 'react'
import { connect } from 'react-redux';
import { useHistory, useLocation } from 'react-router-dom'
import '../../resources/css/fragment/Fragment.css'
import {getFragmentWithXml, getFragmentWithXmlAndUrl, getNextFragmentWithXmlAndUrl, 
    getPrevFragmentWithXmlAndUrl, getIntersByArrayExternalId, getInterWithDiff, 
    getAuthorialInterWithDiffs, getInterCompare, addToEdition, 
    getFragmentWithXmlAndUrlNoUser} from '../../util/API/FragmentAPI'
import Body_expert from './pages/expert/Body_expert'
import Navigation_expert from './pages/expert/Navigation_expert'
import InterEmpty from './pages/InterEmpty';
import Body_virtual from './pages/virtual/Body_virtual';
import Navigation_virtual from './pages/virtual/Navigation_virtual'


const Fragment_DISPATCHER = (props) => {

    const [data, setData] = useState(null)
    const location = useLocation()
    const history = useHistory()
    
    useEffect(() => {
        var path = location.pathname.split('/')
        console.log(path);
        if(path[5])
            if(props.isAuthenticated){
                getFragmentWithXmlAndUrl(path[3], path[5], props.selectedVEAcr)
                .then(res => {
                    console.log(res);
                    dataHandler(res.data)
                })
            }   
            else{
                getFragmentWithXmlAndUrlNoUser(path[3], path[5], props.selectedVEAcr)
                .then(res => {
                    console.log(res);
                    dataHandler(res.data)
                    
                })
            }
        else if(path[3])
            getFragmentWithXml(path[3])
                .then(res => {
                    console.log(res);
                    dataHandler(res.data)
                })
    }, [])

    const dataHandler = (obj) => {
        if(data === null) setData(obj)
        else{
            let aux = {...data}
            if(obj["fragment"]!==null) aux["fragment"] = obj["fragment"]
            if(obj["ldoD"]!==null) aux["ldoD"] = obj["ldoD"]
            if(obj["ldoDuser"]!==null) aux["ldoDuser"] = obj["ldoDuser"]
            if(obj["inters"]!==null) aux["inters"] = obj["inters"]
            if(obj["hasAccess"]!==null) aux["hasAccess"] = obj["hasAccess"]
            if(obj["transcript"]!==null) aux["transcript"] = obj["transcript"]
            if(obj["virtualEditionsDto"]!==null) aux["virtualEditionsDto"] = obj["virtualEditionsDto"]
            if(obj["setTranscriptionSideBySide"]!==null) aux["setTranscriptionSideBySide"] = obj["setTranscriptionSideBySide"]
            if(obj["variations"]!==null) aux["variations"] = obj["variations"]
            if(obj["writerLineByLine"]!==null) aux["writerLineByLine"] = obj["writerLineByLine"]
            if(obj["surface"]!==null) aux["surface"] = obj["surface"]
            aux["nextPb"] = obj["nextPb"]
            aux["nextSurface"] = obj["nextSurface"]
            aux["prevPb"] = obj["prevPb"]
            aux["prevSurface"] = obj["prevSurface"]
            setData(aux)
        }
    }

    const navigationSelectionHandler = (xmlId, urlId, nextOrPrev) => {
        if(nextOrPrev === "next"){
            getNextFragmentWithXmlAndUrl(xmlId, urlId, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
            })
        }
        else if(nextOrPrev === "prev"){
            getPrevFragmentWithXmlAndUrl(xmlId, urlId, props.selectedVEAcr)
            .then(res => {
                history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                dataHandler(res.data)
            }) 
        }
        else{
            getFragmentWithXmlAndUrl(xmlId, urlId, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)
                })
        }      
    }

    const getIntersByExternalId = (fragmentExternalId, arrayInterId) => {
        getIntersByArrayExternalId(fragmentExternalId, arrayInterId)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const callbackDiffHandler = (bool) => {
        let array = []
        array.push(data.inters[0].externalId)
        getInterWithDiff(array, bool)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const callbackSelectedHandler = (obj) => {
        let array = []
        array.push(data.inters[0].externalId)
        getAuthorialInterWithDiffs(array, obj)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const callbackCompareSelectedHandler = (obj) => {
        let array = []
        for(let el of data.inters){
            array.push(el.externalId)
        }
        getInterCompare(array, obj)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const addToEditionHandler = (externalId) => {
        addToEdition(externalId, data.inters[0].externalId)
            .then(res => {
                console.log(res.data)
                if(res.data === "success") {
                    var path = location.pathname.split('/')
                    getFragmentWithXmlAndUrl(path[3], path[5], props.selectedVEAcr)
                        .then(res => {
                            console.log(res);
                            dataHandler(res.data)
                        })
                }
            })
    }

    return (
        <div className="fragment">
            <div className="fragment-body">
                <InterEmpty data={data}/>
                <Body_expert data={data} messages={props.messages} 
                    callbackDiffHandler={(bool) => callbackDiffHandler(bool)}
                    callbackSelectedHandler={(obj) => callbackSelectedHandler(obj)}
                    callbackCompareSelectedHandler={(obj) => callbackCompareSelectedHandler(obj)}/>
                <Body_virtual data={data} messages={props.messages}/>
            </div>

            <div className="fragment-navigation">
                <Navigation_expert 
                    data={data} 
                    callbackNavigationHandler={(xmlId, urlId, nextOrPrev) => {navigationSelectionHandler(xmlId, urlId, nextOrPrev)}} 
                    callbackCheckboxHandler={(fragmentExternalId, arrayInterId) => {getIntersByExternalId(fragmentExternalId, arrayInterId)}}
                    messages={props.messages}/>
                <Navigation_virtual 
                    data={data} 
                    callbackNavigationHandler={(xmlId, urlId, nextOrPrev) => {navigationSelectionHandler(xmlId, urlId, nextOrPrev)}}
                    callbackCheckboxHandler={(fragmentExternalId, arrayInterId) => {getIntersByExternalId(fragmentExternalId, arrayInterId)}}
                    callbackAddToEdition={externalId => addToEditionHandler(externalId)}
                    messages={props.messages}/>
            </div>
        </div>
    )
}

const mapStateToProps = (state) => {
    return {
        selectedVEAcr: state.selectedVEAcr   
    }
}


export default connect(mapStateToProps,null)(Fragment_DISPATCHER)