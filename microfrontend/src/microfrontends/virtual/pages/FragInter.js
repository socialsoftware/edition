import React, { useEffect, useState } from 'react'
import { Link, useHistory, useLocation } from 'react-router-dom'
import { getFragInterData } from '../../../util/API/VirtualAPI';
import {ReactComponent as LeftArrow} from '../../../resources/assets/caret-left-fill.svg'
import {ReactComponent as List} from '../../../resources/assets/card-list.svg'
import {ReactComponent as Nest} from '../../../resources/assets/list-nested.svg'
import he from 'he'

const FragInter = (props) => {

    const [data, setData] = useState(null)
    const history = useHistory()
    const location = useLocation()

    useEffect(() => {
        var path = location.pathname.split('/')
        getFragInterData(path[5])
            .then(res => {
                setData(res.data)
            })
            .catch((err) => {
                console.log(err);
                alert("Acesso Negado")
                history.push("/")
            })
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const mapCategoriesToTable = () => {
        return data.categoryList.map((category, i) => {
            return (
                <tr key={i}>
                    <td>
                        <Link className="virtual-link" to={`/virtual/virtualeditions/restricted/category/${category.externalId}`}>{category.name}</Link>
                    </td>
                </tr>
            )
        })
    }

    return (
        <div className="virtual-editions">
            <button className="virtual-back-button" onClick={() => history.goBack()}>
                <LeftArrow></LeftArrow>
                <p>{props.messages.general_back}</p>
            </button>
            <span className="virtual-body-title">
                <p>{data?he.decode(data.editionTitle):null} - <span className="virtual-link" onClick={() => history.push(`/virtual/virtualeditions/restricted/taxonomy/${data?data.virtualExternalId:null}`)}>{props.messages.general_taxonomy} </span>
                - {data?data.title:null}</p>
            </span>
            <div style={{display:"flex", width:"100%", justifyContent:"flex-end", marginTop:"20px"}}>
                <div className="virtual-taxonomy-public">
                    <p style={{color:"#333"}}>{props.messages.general_public_pages}:</p>
                    <span className="virtual-taxonomy-span-flex" onClick={() => history.push(`/edition/acronym/${data?data.acronym:null}`)}>
                        <List className="virtual-list-icon"></List>
                        <p className="virtual-taxonomy-edition-text">{props.messages.general_edition} <span style={{color:"#000"}}>-</span></p>
                    </span>
                      <span className="virtual-taxonomy-span-flex" onClick={() => history.push(`/fragments/fragment/${data?data.xmlId:null}/category/${data?data.urlId:null}`)}>
                        <Nest className="virtual-list-icon"></Nest>
                        <p className="virtual-taxonomy-edition-text">{props.messages.fragment}</p>
                    </span>
                </div>
            </div>
            <table className="virtual-restricted-table">
                <thead>
                    <tr>
                        <th>{props.messages.general_category}</th>
                    </tr>
                </thead>
                <tbody>
                    {data?
                        mapCategoriesToTable()
                    :null}
                </tbody>
            </table>
        </div>
    )
}

export default FragInter