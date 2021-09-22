import React, { useEffect, useState } from 'react'
import FacDisplay from './FacDisplay'

const FragmentInterMetaInfo = (props) => {

    const [expertEditionInfo, setExpertEditionInfo] = useState(null)
    const [sourceInfo, setSourceInfo] = useState(null)
    const [isEditiorial, setIsEditorial] = useState(false)
    const [isManuscript, setIsManuscript] = useState(false)
    const [isPublication, setIsPublication] = useState(false)
    const [fac, setFac] = useState(null)

    useEffect(() => {
        
        if(props.sourceList!==undefined){
            setSourceInfo(props.sourceList)
            if(props.sourceList.sourceType === "MANUSCRIPT") {
                setIsManuscript(true)
            }
            if(props.sourceList.sourceType === "PRINTED") {
                setIsPublication(true)
            }
        } 
        else if(props.expertEditionMap){
            if(props.author!==undefined){
                var editionInfoAux = props.expertEditionMap[props.author]
            }
            else editionInfoAux = props.expertEditionMap
            if(editionInfoAux){
                if(editionInfoAux.sourceType === "EDITORIAL" || editionInfoAux.type === "EDITORIAL"){
                    setIsEditorial(true)
                }
                setExpertEditionInfo(editionInfoAux)
            }
        }
    }, [props.author, props.expertEditionMap, props.sourceList])

    const getDimensionsMap = (val) => {
        let total = "("
        // eslint-disable-next-line array-callback-return
        val.map((elem,key) => {
            total += `${elem.height} X ${elem.width}`
            if(key < val.length-1) total += ", "
        })
        return total +")"
    }

    const getHandNoteMap = (val) => {
        return val.map((nota, i) => {
            return (
                <p key={i}>
                    <strong>{props.messages.general_manuscript} </strong>
                    {nota.desc?`(${nota.desc})`:null}
                    <strong> : </strong>{nota.note}
                </p>
            )
        })
    }

    const getTypeNoteMap = (val) => {
        return val.map((nota, i) => {
            return (
                <p key={i}>
                    <strong>{props.messages.general_typescript} </strong>
                    {nota.desc?`(${nota.desc})`:null}
                    <strong> : </strong>{nota.note}
                </p>
            )
        })
    }

    const handleExtraNotes = (val) => {
        return val.map((note,i) => {
            return(
                <p key={i}><strong>{props.messages.general_note}:</strong> {note.presentationText}</p>
            )
        })
    }

    const getSurfacesMap = (val) => {
        if(val!==null){
            return val.map((elem,key) => {
                return <p key={key} className="fragment-virtual-link" onClick={() => {
                    setFac(elem.graphic)
                    window.scrollTo({top: 0, behavior: 'smooth'});
                        }}> {elem.graphic},</p>
            })
        }
    }



    return(
        <div>
            {
                fac?
                    <FacDisplay url={fac} removeFac={() => setFac(null)}/>
                    :null
            }
            
            {/* titulo ident */}
            {isEditiorial?
                <p>
                    <strong>{props.messages.general_title}: </strong>{expertEditionInfo.title}
                </p>:isPublication?
                <p>
                    <strong>{props.messages.general_title}: </strong>{sourceInfo.title}
                </p>:isManuscript?
                <p>
                    <strong>{props.messages.general_identification} </strong>{sourceInfo.altIdentifier}
                </p>:null
            }

            {/* heteronimo */}
            {
                isManuscript||isPublication||isEditiorial?<strong>{props.messages.general_heteronym}: </strong>:null
            }
            {isEditiorial?!expertEditionInfo.heteronym?
                expertEditionInfo.heteronym:props.messages.general_heteronym_notassigned:isManuscript||isPublication?
                    props.messages.general_heteronym_notassigned:null}

            {/* testemunho */}
            {isManuscript?
                sourceInfo.form === 'LEAF'?
                    <div>
                        <p><strong>{props.messages.general_format}: </strong>{props.messages.general_leaf}</p>
                        <small>{sourceInfo.dimensionSetSize!==0?getDimensionsMap(sourceInfo.dimensionDtoList):null}</small>
                    </div>:null:null
            }
            {isManuscript?
                sourceInfo.material === 'PAPER'?
                    <div>
                        <p><strong>{props.messages.general_material}: </strong>{props.messages.general_paper}</p>
                    </div>:null:null
            }
            {isManuscript?
                sourceInfo.columns !== 0?
                    <div>
                        <p><strong>{props.messages.general_columns}: </strong>{sourceInfo.columns}</p>
                    </div>:null:null
            }
            {isManuscript?
                <div>
                    {sourceInfo.hadLdoDLabel?
                        <p><strong>LdoD Mark: </strong>{props.messages.search_ldod_with}</p>:<p><strong>LdoD Mark: </strong>{props.messages.search_ldod_without}</p>}
                </div>:null
            }
            {isManuscript?
                getHandNoteMap(sourceInfo.handNoteDtoSet):null
            }
            {isManuscript?
                getTypeNoteMap(sourceInfo.typeNoteSet):null
            }

            {/* VOLUME */}
            {isEditiorial?
                expertEditionInfo.volume!==""?
                    <p><strong>{props.messages.tableofcontents_volume}</strong>: {expertEditionInfo.volume}</p>:null:null
            }

            {/* JOURNAL */}
            {isPublication?
                <p><strong>{props.messages.general_journal}</strong>: {sourceInfo.journal}</p>:null}
                
            {/* COMPLETE NUMBER */}
            {isEditiorial?
                expertEditionInfo.number!==""?
                    <p><strong>{props.messages.tableofcontents_number}</strong>: {expertEditionInfo.number}</p>:null:
            isManuscript?
                null:   
            isPublication?
                sourceInfo.issue!==null?
                    <p><strong>{props.messages.tableofcontents_number}</strong>: {sourceInfo.issue}</p>:null:null
            }

            {/* START END PAGES */}
            {isEditiorial?
                expertEditionInfo.startPage!==0?
                <p>
                    <strong>{props.messages.tableofcontents_page}: </strong>{expertEditionInfo.startPage}
                    {expertEditionInfo.endPage!==expertEditionInfo.startPage?` - ${expertEditionInfo.endPage}`:null}
                </p>:
            isManuscript?
                null:   
            isPublication?
                sourceInfo.startPage!==0?
                <p>
                    <strong>{props.messages.tableofcontents_page}: </strong>{sourceInfo.startPage}
                    {sourceInfo.endPage!==sourceInfo.startPage?` - ${sourceInfo.endPage}`:null}
                </p>:null:null:null
            }

            {/* PUBLISHED */}
            {isPublication?
                <p><strong>{props.messages.general_published_place}</strong>: {sourceInfo.pubPlace}
                    {sourceInfo.desc}</p>:null
            }
            {/* DATE */}
            {isEditiorial?
                expertEditionInfo.date!==null?
                    <p><strong>{props.messages.general_date}:</strong> {expertEditionInfo.date}{expertEditionInfo.desc}</p>:null:
            isManuscript?
                sourceInfo.date!==null?
                    <p><strong>{props.messages.general_date}:</strong> {sourceInfo.date} ({sourceInfo.desc})</p>:null:   
            isPublication?
                sourceInfo.date!==null?
                    <p><strong>{props.messages.general_date}:</strong> {sourceInfo.date}{sourceInfo.desc}</p>:null:null
            }

            {/* NOTES */}
            {isEditiorial?
                expertEditionInfo.notes!==""?
                    <p><strong>{props.messages.general_note}:</strong> {expertEditionInfo.notes}</p>:null:
            isManuscript?
                sourceInfo.notes!==""?
                    <p><strong>{props.messages.general_note}:</strong> {sourceInfo.notes}</p>:null:   
            isPublication?
                null:null
            }

            {/* NOTES 2 */}
            {isEditiorial?expertEditionInfo.annexNoteDtoList?handleExtraNotes(expertEditionInfo.annexNoteDtoList):null:null}

            {isManuscript || isPublication?
                <p><strong>{props.messages.general_facsimiles}: </strong> {getSurfacesMap(sourceInfo.surfaceDto)}</p>:null
                }
        </div>
    )
}

export default FragmentInterMetaInfo

