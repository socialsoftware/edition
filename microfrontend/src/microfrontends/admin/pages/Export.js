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
                else setErro("Erro a exportar aleatoriamente, tente de novo por favor (fragmentos em falta)")
            })
    }

    const exportUsersHandler = () => {
        setErro("")
        exportUsers()
            .then(response => {
                if(response.data.type === "application/xml")
                    fileDownload(response.data, "users.xml")
                else setErro("Erro a exportar utilizadores")
            })
    }

    const exportAllHandler = () => {
        setErro("")
        exportAll()
            .then(response => {
                if(response.data.type === "application/tei+xml")
                    fileDownload(response.data, "tei.xml")
                else setErro("Erro a exportar o arquivo completo (fragmentos em falta)")
            })
    }

    const exportVirtualEditionsHandler = () => {
        setErro("")
        exportVirtualEditions()
            .then(response => {
                if(response.data.type === "application/zip")
                    fileDownload(response.data, "virtualeditions.zip")
                else setErro("Erro a exportar o edicoes virtuais")
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
                    
                else setErro("Não foram encontrados fragmentos com base nessa pesquisa")
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
                else setErro("Erro a exportar os fragmentos (fragmentos em falta)")
            })
    }
    return (
        <div className="admin">
            {
                erro?
                    <p style={{color:"red", fontWeight:700}}>{erro}</p>
                :null
            }
            
            <p className="admin-exp-title">EXPORTAR</p>
            <p className="admin-exp-subtitle">Exportar Utilizadores</p>
            <div className="admin-exp-subtitle-div">
                <span className="admin-exp-subtitle-button" onClick={() => exportUsersHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Exportar utilizadores</p>
                </span>
            </div>
            <p className="admin-exp-subtitle">Exportar Edições Virtuais</p>
            <div className="admin-exp-subtitle-div">
                <span className="admin-exp-subtitle-button" onClick={() => exportVirtualEditionsHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Exportar edições virtuais</p>
                </span>
            </div>
            <p className="admin-exp-subtitle">Exportar Fragmentos</p>
            <div className="admin-exp-subtitle-div">
                <span className="admin-exp-subtitle-button" onClick={() => exportAllHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Exportar arquivo completo</p>
                </span>
                <p className="admin-exp-subtitle-sub">(poderá demorar alguns instantes)</p>
            </div>
            <div className="admin-exp-subtitle-div" style={{marginTop:"20px"}}>
                <span className="admin-exp-subtitle-button" onClick={() => exportRandomHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Exportar 3 fragmentos aleatórios</p>
                </span>
            </div>
            <div className="admin-exp-subtitle-div" style={{marginTop:"20px"}}>
                <input placeholder="pesquisar titulo" className="form-control input-search" onChange={e => setQuery(e.target.value)}></input>
                <span className="admin-exp-subtitle-button" style={{marginTop:"20px"}} onClick={() => getFragmentsByQueryHandler()}>
                    <Download style={{marginRight:"5px"}}/>
                    <p>Pesquisar</p>
                </span>
                {
                    queryResult.length > 0?
                        <span className="admin-exp-subtitle-button" style={{marginTop:"20px"}} onClick={() => exportFragmentsByQueryHandler()}>
                            <Download style={{marginRight:"5px"}}/>
                            <p>Exportar {queryResult.length} fragmentos</p>
                        </span>
                    :null
                }
            </div>

            {
                    queryResult.length>0?
                        <div>
                            <p style={{textAlign:"left", marginTop:"20px"}}>Nº de fragmentos encontrados: {queryResult.length}</p>
                            <table className="admin-simple-table">
                                <tbody>
                                    {mapFragmentsToTable()}
                                </tbody>
                            </table>
                        </div>
                        
                    :erro?
                        <div className="admin-exp-subtitle-div" style={{marginTop:"20px"}}>
                            <p>Erro a encontrar fragmentos com base no critério de pesquisa</p>
                        </div>
                    :null
                }


        </div>
    )
}

export default Export