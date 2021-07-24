import React from 'react';
// @ts-ignore
import tag from '../../../../resources/assets/tag_fill.svg'
// @ts-ignore
import user from '../../../../resources/assets/person-fill.svg'
import { Link } from 'react-router-dom';

const Taxonomy = (props) => {

    const mapDataToTable = () => {
        // @ts-ignore
        return props.data.inters[0].categoryUserDtoList.map((val, i) => {
            return (
                <tr key={i}>
                    <td><Link className="fragment-virtual-link" to={`/edition/acronym/${val.category.acronym}/category/${val.category.urlId}`}>{val.category.name}</Link></td>
                    <td><Link className="fragment-virtual-link" to={`/edition/user/${val.user.userName}`}>{val.user.firstName} {val.user.lastName} ({val.user.userName})</Link></td>
                </tr>
            )
        })
    }
    return (
        <div className="fragment-taxonomy">
            <table style={{paddingBottom:"30px"}}>
                <thead>
                    <tr>
                        <th><img alt="tagImg" src={tag} style={{height:"20px", width:"20px"}}></img></th>
                        <th><img alt="userImg" src={user} style={{height:"20px", width:"20px"}}></img></th>
                    </tr>
                </thead>
                <tbody>
                    {
                        props.data?
                        mapDataToTable()
                        :null
                    }
                </tbody>
            </table>
        </div>
    )
}

export default Taxonomy