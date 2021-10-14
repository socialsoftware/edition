import React, { useState } from 'react'
import {ReactComponent as Download} from '../../../resources/assets/file-arrow-up.svg'
import { exportRandom, exportUsers, exportAll, exportVirtualEditions, getFragmentsByQuery, exportFragmentsByQuery} from '../../../util/API/AdminAPI';
import fileDownload from 'js-file-download';
import { useHistory } from 'react-router-dom';

const Export = (props) => {

    const history = useHistory()
    const [erro, setErro] = useState("")
    const [query, setQuery] = useState("")
    const [queryResult, setQueryResult] = useState([])
    const [lastQuery, setLastQuery] = useState("")

    const exportRandomHandler = () => {
        setErro("")
        exportRandom()
            .then(response => {
                if(response.data.type === "application/tei+xml")
                    fileDownload(response.data, "tei.xml")
                else setErro("Error exporting randomly, please try again (missing fragments)")
            })
    }

    const exportUsersHandler = () => {
        setErro("")
        exportUsers()
            .then(response => {
                if(response.data.type === "application/xml")
                    fileDownload(response.data, "users.xml")
                else setErro("Error exporting users")
            })
    }

    const exportAllHandler = () => {
        setErro("")
        exportAll()
            .then(response => {
                if(response.data.type === "application/tei+xml")
                    fileDownload(response.data, "tei.xml")
                else setErro("Error exporting the full archive (missing fragments)")
            })
    }

    const exportVirtualEditionsHandler = () => {
        setErro("")
        exportVirtualEditions()
            .then(response => {
                if(response.data.type === "application/zip")
                    fileDownload(response.data, "virtualeditions.zip")
                else setErro("Error exporting virtual editions")
            })
    }

    const getFragmentsByQueryHandler = () => {
        setErro("")
        if(query.length>0){
            getFragmentsByQuery(query)
            .then(response => {
                if(response.data.length>0){
                    setQueryResult(response.data)
                    setLastQuery(query)
                }
                    
                else setErro("Fragments not found based on the search parameters")
            })
        }
        
    }

    const mapFragmentsToTable = () => {
        return queryResult.map((frag, i) => {
            return (
                <tr key={i}>
                    <td onClick={() => history.push(`/fragments/fragment/${frag.fragmentXmlId}`)} className="admin-link" dangerouslySetInnerHTML={{ __html: frag.exportString }}></td>
                </tr>
                
            )
        })
    }

    const exportFragmentsByQueryHandler = () => {
        exportFragmentsByQuery(lastQuery)
            .then(response => {
                if(response.headers["content-type"] === "application/tei+xml")
                    fileDownload(response.data, "tei.xml")
                else setErro("Error exporting fragments (missing fragments)")
            })
    }
    return (
        <div className="admin">
            {
                erro?
                    <p style={{color:"red", fontWeight:700}}>{erro}</p>
                :null
            }
            
            <p className="admin-exp-title">EXPORT</p>
            <p className="admin-exp-subtitle">Export Users</p>
            <div className="admin-exp-subtitle-div">
                <span className="admin-exp-subtitle-button" onClick={() => exportUsersHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Export user</p>
                </span>
            </div>
            <p className="admin-exp-subtitle">Exportar Virtual Editions</p>
            <div className="admin-exp-subtitle-div">
                <span className="admin-exp-subtitle-button" onClick={() => exportVirtualEditionsHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Export virtual editions</p>
                </span>
            </div>
            <p className="admin-exp-subtitle">Exportar Fragments</p>
            <div className="admin-exp-subtitle-div">
                <span className="admin-exp-subtitle-button" onClick={() => exportAllHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Export entire archive</p>
                </span>
                <p className="admin-exp-subtitle-sub">(Might take several minutes)</p>
            </div>
            <div className="admin-exp-subtitle-div" style={{marginTop:"20px"}}>
                <span className="admin-exp-subtitle-button" onClick={() => exportRandomHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Exporting 3 random fragments</p>
                </span>
            </div>
            <div className="admin-exp-subtitle-div" style={{marginTop:"20px"}}>
                <input placeholder="pesquisar titulo" className="form-control input-search" onChange={e => setQuery(e.target.value)}></input>
                <span className="admin-exp-subtitle-button" style={{marginTop:"20px"}} onClick={() => getFragmentsByQueryHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Search</p>
                </span>
                {
                    queryResult.length > 0?
                        <span className="admin-exp-subtitle-button" style={{marginTop:"20px"}} onClick={() => exportFragmentsByQueryHandler()}>
                            <Download style={{marginRight:"5px"}}/>
                            <p>Exporting {queryResult.length} fragments</p>
                        </span>
                    :null
                }
            </div>

            {
                    queryResult.length>0?
                        <div>
                            <p style={{textAlign:"left", marginTop:"20px"}}>Nº of found fragments: {queryResult.length}</p>
                            <table className="admin-simple-table">
                                <tbody>
                                    {mapFragmentsToTable()}
                                </tbody>
                            </table>
                        </div>
                        
                    :erro?
                        <div className="admin-exp-subtitle-div" style={{marginTop:"20px"}}>
                            <p>Error finding fragments based on the search parameters</p>
                        </div>
                    :null
                }


        </div>
    )
}

export default Export