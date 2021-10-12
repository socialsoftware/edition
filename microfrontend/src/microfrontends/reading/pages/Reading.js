import React, { useEffect, useState } from 'react';
import { getReadingExperts, getStartReadingFragment,
            getNextReadingFragment, getPrevReadingFragment, getPrevRecom, getCurrentReadingFragmentJson } from '../../../util/API/ReadingAPI';
import rightArrow from '../../../resources/assets/right_arrow.svg'
import leftArrow from '../../../resources/assets/left_arrow.svg'
import info from '../../../resources/assets/information.svg'
import save from '../../../resources/assets/floppy-disk.svg'
import ReactTooltip from 'react-tooltip';
import { useHistory, useLocation } from 'react-router';
import { connect } from 'react-redux';
import CircleLoader from "react-spinners/RotateLoader";


const Reading = (props) => {

    const [loading, setLoading] = useState(true)
    const [experts, setExperts] = useState(null)
    const [fragmentData, setFragmentData] = useState(null)
    const [selectedExpert, setSelectedExpert] = useState(null)
    const [heteronymWeight, setHeteronymWeight] = useState(0)
    const [dateWeight, setDateWeight] = useState(0)
    const [textWeight, setTextWeight] = useState(1)
    const [taxonomyWeight, setTaxonomyWeight] = useState(0)
    const [showModal, setShowModal] = useState(false) 
    const history = useHistory()
    const location = useLocation()

    useEffect(() => {
        var mounted = true
        getReadingExperts()
            .then(res => {
                if(mounted){
                    setExperts(res.data)
                    setLoading(false)
                }
            })
        var path = location.pathname.split('/')
        if(path[3] && path[5]){
            getCurrentReadingFragmentJson(path[3], path[5], props.recommendation)
            .then(res => {
                if(mounted){
                setFragmentData(res.data)
                setSelectedExpert(res.data.expertEditionInterDto.acronym)
                var val = res.data.readingRecommendation
                val.dateWeight = 0
                val.heteronymWeight = 0
                val.taxonomyWeight = 0
                val.textWeight = 1
                props.setUpdateRecommendation(val)
                }
            })
            .catch(error => {
                console.log(error);
            })
        }
        return function cleanup() {
            mounted = false
            var val = props.recommendation
            val.read = []
            val.dateWeight = 0
            val.heteronymWeight = 0
            val.taxonomyWeight = 0
            val.textWeight = 1
            props.setUpdateRecommendation(val)
            }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    useEffect(() => {
        if(showModal){
            document.body.style.overflow = "hidden"
        }
        else document.body.style.overflow = "scroll"
    }, [showModal])

    const getFragmentHandler = (xmlId, urlId) => {
        if(xmlId !== null && urlId !== null){
            getCurrentReadingFragmentJson(xmlId, urlId, props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${xmlId}/inter/${urlId}`)
            })
        }
    }
    const getFragmentWithAcronymHandler = (xmlId, urlId, acronym) => {
        getCurrentReadingFragmentJson(xmlId, urlId, props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                setSelectedExpert(acronym)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${xmlId}/inter/${urlId}`)
            })
    }

    const selectExpertHandler = (acronym) => {
        getStartReadingFragment(acronym, props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                setSelectedExpert(acronym)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.expertEditionInterDto.urlId}`)
            })
    }

    const getPrevFragmentHandler = (acronym, xmlId, urlId) => {
        getPrevReadingFragment(xmlId, urlId, props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                setSelectedExpert(acronym)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${xmlId}/inter/${urlId}`)
            })
    }

    const getNextFragmentHandler = (acronym, xmlId, urlId) => {
        getNextReadingFragment(xmlId, urlId, props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                setSelectedExpert(acronym)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${xmlId}/inter/${urlId}`)
            })
    }

    const setNewRecommendationsWithWeight = (type, value) => {
        let aux = props.recommendation
        aux[type] = value
        props.setUpdateRecommendation(aux)
    }

    const resetRecommendations = (xmlId, urlId) => {
        var val = props.recommendation
        val.read = []
        props.setUpdateRecommendation(val)
        if(xmlId !== null && urlId !== null){
            getCurrentReadingFragmentJson(xmlId, urlId, props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${xmlId}/inter/${urlId}`)
            })
        }
    }

    const getPrevRecommendedFragment = () => {
        getPrevRecom(props.recommendation)
            .then(res => {
                setFragmentData(res.data)
                props.setUpdateRecommendation(res.data.readingRecommendation)
                history.replace(`/reading/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.expertEditionInterDto.urlId}`)
            })
    }
    const mapRecommendationsToColumn = () => {
        return fragmentData.recommendations.map((val, i) => {
            return (
                <div key={i} onClick={() => getFragmentWithAcronymHandler(val.fragmentXmlId, val.urlId, val.acronym)} className="recommendation-div-hover">
                    <p className="reading-recommendation-acronym">{val.acronym}</p>
                    <p className="reading-number" >{val.number}</p>
                    <div className="reading-arrows">
                        <img alt="arrow" src={rightArrow} style={{margin:0}} className="reading-arrow-right"></img>
                    </div>
                </div>
            )
        })
    }
    const mapAuthorsInterpsToColumn = (index,flag) => {
        return fragmentData.expertEditionDtoList[index].map((inter, i) => {
            return (
                <div key={i}>
                    <p onClick={() => getFragmentWithAcronymHandler(fragmentData.fragment.fragmentXmlId, inter.urlId, inter.acronym)} 
                    className={flag?"reading-number-selected":"reading-number"}>{inter.number}</p>
                    <div className="reading-arrows">
                        <img alt="arrow" onClick={() => getPrevFragmentHandler(inter.acronym, fragmentData.fragment.fragmentXmlId, inter.urlId)} src={leftArrow} className="reading-arrow"></img>
                        <img alt="arrow" onClick={() => getNextFragmentHandler(inter.acronym, fragmentData.fragment.fragmentXmlId, inter.urlId)} src={rightArrow} className="reading-arrow-right"></img>
                    </div>
                </div>
            )
        })
    }

    const mapAuthorsToPage = () => {
        return experts.map((author, i) => {
            return (
                <div key={i} className={selectedExpert===author.acronym?"reading-column-fragment-selected":"reading-column-fragment"}>
                    <div className={selectedExpert===author.acronym?"reading-column-selected":"reading-column"}>
                        <div className="reading-author-div" key={author.acronym}>
                            <p onClick={() => selectExpertHandler(author.acronym)} className={selectedExpert===author.acronym?"reading-column-author-selected":"reading-column-author"}>{author.editor}</p>
                            {
                                !fragmentData?
                                <img alt="arrow" onClick={() => selectExpertHandler(author.acronym)} src={rightArrow} className="reading-arrow"></img>
                                :null
                            }
                            {
                                fragmentData?
                                    selectedExpert===author.acronym?
                                        mapAuthorsInterpsToColumn(i,true)
                                        :mapAuthorsInterpsToColumn(i,false)
                                :null
                            }
                        </div>
                        
                    </div>
                    
                    {
                        selectedExpert===author.acronym&&fragmentData?
                            <div className="reading-fragment">
                                <p className="reading-fragment-title" onClick={() => history.push(`/fragments/fragment/${fragmentData.fragment.fragmentXmlId}/inter/${fragmentData.expertEditionInterDto.urlId}`)}>{fragmentData.expertEditionInterDto.title}</p>
                                <div className="reading-fragment-text" dangerouslySetInnerHTML={{ __html: fragmentData.transcript }} />
                            </div>
                        :null
                    }
                </div>

                
            )
        })
    }

    return (
        <div>
            {
                loading?
                <CircleLoader loading={true}></CircleLoader>
                :
                <div>
                    {showModal?
                        <div className="reading-backdrop" onClick={() => setShowModal(false)}></div>
                    :null}
                    {
                        showModal?
                            <div className="reading-modal">
                                <div className="reading-modal-header">
                                    <p className="reading-modal-header-close" onClick={() => {
                                        getFragmentHandler(fragmentData?fragmentData.fragment.fragmentXmlId:null, 
                                                            fragmentData?fragmentData.expertEditionInterDto.urlId:null)
                                        setShowModal(false)}}>
                                            x</p>
                                    <p className="reading-modal-header-title">{props.messages.general_recommendation_config}</p>
                                </div>
                                <div className="reading-modal-body">
                                    <p className="reading-modal-body-title">{props.messages.recommendation_criteria} :</p>
                                    <div className="reading-modal-controls-div">
                                        <div className="reading-modal-body-input">
                                            <p className="reading-modal-body-input-title">{props.messages.general_heteronym}</p>
                                            <input max="1" min="0" step="0.2" type="range" className="reading-range" onChange={e => {
                                                setHeteronymWeight(parseFloat(e.target.value))
                                                setNewRecommendationsWithWeight("heteronymWeight", e.target.value)
                                                }} value={heteronymWeight}></input>
                                        </div>
                                        <div className="reading-modal-body-input">
                                            <p className="reading-modal-body-input-title">{props.messages.general_date}</p>
                                            <input max="1" min="0" step="0.2" type="range" className="reading-range" onChange={e => {
                                                setDateWeight(parseFloat(e.target.value))
                                                setNewRecommendationsWithWeight("dateWeight", e.target.value)
                                                }} value={dateWeight}></input>
                                        </div>
                                        <div className="reading-modal-body-input">
                                            <p className="reading-modal-body-input-title">{props.messages.general_text}</p>
                                            <input max="1" min="0" step="0.2" type="range" className="reading-range" onChange={e => {
                                                setTextWeight(parseFloat(e.target.value))
                                                setNewRecommendationsWithWeight("textWeight", e.target.value)
                                                }} value={textWeight}></input>
                                        </div>
                                        <div className="reading-modal-body-input">
                                            <p className="reading-modal-body-input-title">{props.messages.general_taxonomy}</p>
                                            <input max="1" min="0" step="0.2" type="range" className="reading-range" onChange={e => {
                                                setTaxonomyWeight(parseFloat(e.target.value))
                                                setNewRecommendationsWithWeight("taxonomyWeight", e.target.value)
                                                }} value={taxonomyWeight}></input>
                                        </div>
                                    </div>                            
                                </div>
                                <div className="reading-modal-footer">
                                    <div className="reading-modal-footer-buttons">
                                        <p className="reading-modal-footer-restart" onClick={() => {
                                            if(fragmentData){
                                                resetRecommendations(fragmentData?fragmentData.fragment.fragmentXmlId:null, 
                                                    fragmentData?fragmentData.expertEditionInterDto.urlId:null)
                                                setShowModal(false)
                                            }
                                            
                                        }}><span><img alt="arrow" src={save} style={{height:"15px", width:"15px"}}></img></span> {props.messages.general_reset}</p>
                                        <p className="reading-modal-footer-close" onClick={() => {
                                            getFragmentHandler(fragmentData?fragmentData.fragment.fragmentXmlId:null, 
                                                fragmentData?fragmentData.expertEditionInterDto.urlId:null)
                                            setShowModal(false)}}>{props.messages.recommendation_recommend}</p>
                                    </div>
                                </div>
                            </div>
                        :null
                    }
                    <div className="reading">
                    {!selectedExpert?
                    <div className="reading-book-title">
                        {props.language==="pt"?
                            <p className="reading-h1">Livro do Desassossego de Fernando Pessoa</p>
                        :props.language==="en"?
                            <p className="reading-h1">Book of Disquiet by Fernando Pessoa</p>
                        :
                            <p className="reading-h1">Libro del Desasosiego de Fernando Pessoa</p>
                        }
                    </div>
                    :null
                
                    }
                    {experts?
                        mapAuthorsToPage()
                    :null
                    }
                    
                    <div className="reading-column-recomendation">
                        <p className="reading-column-author-rec" onClick={() => {
                            window.scrollTo({ top: 0, behavior: 'smooth' })

                            setShowModal(true)}}>{props.messages.general_recommendation}</p>
                        <img alt="info" src={info} data-tip={props.messages.reading_tt_recom}
                            className="reading-info"></img>
                        <ReactTooltip backgroundColor="#fff" textColor="#333" border={true} borderColor="#000" className="reading-tooltip" place="bottom" effect="solid"/>
                        {fragmentData?
                            <div>
                                <div onClick={() => {
                                    setSelectedExpert(fragmentData.expertEditionInterDto.acronym)
                                    getFragmentHandler(fragmentData?fragmentData.fragment.fragmentXmlId:null, 
                                        fragmentData?fragmentData.expertEditionInterDto.urlId:null)
                                }} >
                                    <p style={{color:"#FC1B27"}} className="reading-recommendation-acronym">{fragmentData.expertEditionInterDto.acronym}</p>
                                    <p className="reading-number-selected-no-hover">{fragmentData.expertEditionInterDto.number}</p>
                                </div>
                                
                                <div>
                                    {
                                    fragmentData.prevCom?
                                        <div style={{marginTop:"20px"}} onClick={() => getPrevRecommendedFragment()}>
                                            <p className="reading-recommendation-acronym">{fragmentData.prevCom.acronym}</p>
                                            <p className="reading-number" >{fragmentData.prevCom.number}</p>
                                            <img alt="arrow" src={leftArrow} className="reading-arrow"></img>
                                        </div>
                                    :null
                                    }
                                </div>

                                <div >
                                    {mapRecommendationsToColumn()}
                                </div>
                            </div>
                        :null
                        }
                    </div>
                    
                    
                </div>
            </div>
            }
            
        </div>
        
    )
}

const mapStateToProps = (state) => {
    return {
        recommendation:state.recommendation   
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        setUpdateRecommendation: (rec) => dispatch({ type: 'SET_REC', payload: rec }),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Reading) 
