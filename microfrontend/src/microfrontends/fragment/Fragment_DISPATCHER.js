import React, {useEffect, useState} from 'react'
import { connect } from 'react-redux';
import { useLocation } from 'react-router-dom'
import '../../resources/css/fragment/Fragment.css'
import BodyExpert from './pages/expert/Body_expert'
import NavigationExpert from './pages/expert/Navigation_expert'
import InterEmpty from './pages/InterEmpty';
import BodyVirtual from './pages/virtual/Body_virtual';
import NavigationVirtual from './pages/virtual/Navigation_virtual'


const Fragment_DISPATCHER = (props) => {

    const [xmlId, setXmlId] = useState(null)
    const [urlId, setUrlId] = useState(null)
    const [xmlIdExpertToVirtual, setXmlIdExpertToVirtual] = useState(null)
    const [urlIdExpertToVirtual, setUrlIdExpertToVirtual] = useState(null)
    const [xmlIdVirtualToExpert, setXmlIdVirtualToExpert] = useState(null)
    const [urlIdVirtualToExpert, setUrlIdVirtualToExpert] = useState(null)
    const [externalId, setExternalId] = useState(null)
    const [selectedInters, setSelectedInters] = useState([])
    const [currentType, setCurrentType] = useState(null)
    const location = useLocation()
    
    useEffect(() => {
        var path = location.pathname.split('/')
        if(path[3]) setXmlId(path[3])
        if(path[5]) setUrlId(path[5]) 
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const navigationSelectionHandlerExpertToVirtual = (xmlId, urlId) => {
        setXmlIdExpertToVirtual(xmlId)
        setUrlIdExpertToVirtual(urlId)
    }

    const navigationSelectionHandlerVirtualToExpert = (xmlId, urlId) => {
        setXmlIdVirtualToExpert(xmlId)
        setUrlIdVirtualToExpert(urlId)
    }


    return (
        <div className="fragment">
            <div className="fragment-body">
                <InterEmpty 
                    xmlId = {xmlId}
                    selectedInters = {selectedInters}
                    selectedVEAcr = {props.selectedVEAcr}
                    xmlIdVirtualToExpert = {xmlIdVirtualToExpert}
                    xmlIdExpertToVirtual = {xmlIdExpertToVirtual}
                    />
                <BodyExpert 
                    externalId = {externalId}
                    selectedInters = {selectedInters}
                    messages={props.messages} />
                <BodyVirtual 
                    externalId = {externalId}
                    selectedInters = {selectedInters}
                    selectedVEAcr = {props.selectedVEAcr}
                    isAuthenticated = {props.isAuthenticated}
                    messages={props.messages}/>
            </div>

            <div className="fragment-navigation">
                <NavigationExpert 
                    selectedVEAcr = {props.selectedVEAcr}
                    isAuthenticated = {props.isAuthenticated}
                    xmlId = {xmlId}
                    urlId = {urlId}
                    xmlIdVirtualToExpert = {xmlIdVirtualToExpert}
                    urlIdVirtualToExpert = {urlIdVirtualToExpert}
                    selectedInters = {selectedInters}
                    callbackSetExternalId = {id => setExternalId(id)}
                    callbackNavigationHandler={(xmlId, urlId) => {navigationSelectionHandlerExpertToVirtual(xmlId, urlId)}} 
                    callbackSetSelected={(arrayInterId) => {setSelectedInters(arrayInterId)}}
                    currentType = {currentType}
                    callbackSetCurrentType = {() => setCurrentType("expert")}
                    messages={props.messages}/>
                <NavigationVirtual 
                    selectedVEAcr = {props.selectedVEAcr}
                    isAuthenticated = {props.isAuthenticated}
                    xmlId = {xmlId}
                    urlId = {urlId}
                    xmlIdExpertToVirtual = {xmlIdExpertToVirtual}
                    urlIdExpertToVirtual = {urlIdExpertToVirtual}
                    selectedInters = {selectedInters}
                    callbackSetExternalId = {id => setExternalId(id)}
                    callbackNavigationHandler={(xmlId, urlId) => {navigationSelectionHandlerVirtualToExpert(xmlId, urlId)}}
                    callbackSetSelected={(arrayInterId) => {setSelectedInters(arrayInterId)}}
                    currentType = {currentType}
                    callbackSetCurrentType = {() => setCurrentType("virtual")}
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