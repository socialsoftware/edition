import React, {useEffect, useState} from 'react'
import { useHistory, useLocation } from 'react-router'
import { getClassificationContent } from '../../../util/API/EditionAPI'
import CircleLoader from "react-spinners/RotateLoader";

const ClassificationGameUsers = (props) => {

    const [data, setData] = useState(null)
    const location = useLocation()
    const history = useHistory()

    useEffect(() => {
        let aux = location.pathname.split("/")
        console.log(aux);
        if(aux[3] && aux[5]){
            getClassificationContent(aux[3], aux[5])
                .then(res => {
                    setData(res.data)
                    console.log(res.data);
                })
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const mapParticipantsToTable = () => {
        return data.participantList.map((participant, i) => {
            return (
                <tr key={i}>
                    <td>{i+1}</td>
                    <td><p className="edition-participant" onClick={() => history.push(`/edition/user/${participant.userName}`)}>{participant.userName}</p></td>
                    <td>{parseFloat(participant.score)}</td>
                </tr>
            )
        })
    }
    return (
        <div>
            {   
                !data?
                <CircleLoader loading={true}></CircleLoader>:
                <div className="edition-table-div">
                    <span className="edition-list-title">
                        {props.messages.general_classificationGame}: {data.gameTitle}
                    </span>
                    <div className="edition-list-title">
                        <p style={{fontSize:"18px"}}>{props.messages.general_winner}: {data.userName}</p>
                        <p style={{fontSize:"18px"}}>{props.messages.general_category}: {data.name}</p>
                    </div>

                    <table className="edition-participant-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>{props.messages.login_username}</th>
                                <th>{props.messages.general_points}</th>
                            </tr>
                        </thead>
                        <tbody>
                            {mapParticipantsToTable()}
                        </tbody>
                    </table>
                </div>

                
            }
        </div>
            
        
    )
}

export default ClassificationGameUsers