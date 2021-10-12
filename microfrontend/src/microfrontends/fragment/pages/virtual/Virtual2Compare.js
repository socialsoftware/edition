import React from 'react'
import { Link } from 'react-router-dom'
// @ts-ignore
import tag from '../../../../resources/assets/tag_fill.svg'
// @ts-ignore
import user from '../../../../resources/assets/person-fill.svg'
import he from 'he'

const Virtual2Compare = (props) => {

    const mapTagsToTable = (inter) => {
        return inter.categoryUserDtoList.map((val, i) => {
            return (
                <tr key={i}>
                    <td>---</td>
                    <td>---</td>
                    <td>
                        <span style={{display:"flex", alignItems:"center"}}><img alt="userImg" src={user} style={{height:"20px", width:"20px", marginRight:"5px"}}></img> <Link className="fragment-virtual-link" to={`/edition/user/${val.user.userName}`}>{val.user.userName}</Link></span>
                    </td>
                    <td>
                        <span style={{display:"flex", alignItems:"center"}}><img alt="tagImg" src={tag} style={{height:"20px", width:"20px", marginRight:"5px"}}></img> <Link className="fragment-virtual-link" to={`/edition/acronym/${val.category.acronym}/category/${val.category.urlId}`}>{val.category.name}</Link></span>
                    </td>
                </tr>
            )
        })
    }

    const mapTablesToView = () => {
        return props.data.inters.map((inter, i) => {
            return(
                <div key={i}>
                    <p style={{textAlign:"left", margin:"0", marginBottom:"5px"}}><strong>{props.messages.general_edition}:</strong> <span>{he.decode(inter.shortName)}</span></p>
                    <table className="fragment-vcompare-table">
                        <thead>
                            <tr>
                                <th>{props.messages.virtualcompare_quote}</th>
                                <th>{props.messages.virtualcompare_comment}</th>
                                <th>{props.messages.virtualcompare_user}</th>
                                <th>{props.messages.general_tags}</th>
                            </tr>
                        </thead>
                        <tbody>
                            {mapTagsToTable(inter)}
                        </tbody>
                    </table>
                </div>
            )
            
            
        })
    }
    return (
        <div>
            <p className="body-title" style={{marginBottom:"30px"}}>{props.messages.virtualcompare_title}</p>
            {   
                props.data?
                mapTablesToView()
                :null
                }


            
        </div>
    )
}

export default Virtual2Compare