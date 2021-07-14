import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import { ReactComponent as Edit } from '../../../resources/assets/edit.svg'
import { getTweets } from '../../../util/utilsAPI';

const Tweets = (props) => {

    const [list, setList] = useState([])

    useEffect(() => {
        var mounted = true
        getTweets()
            .then(res => {
                if(mounted){
                    console.log(res);
                    setList(res.data.citations)
                }
            })
        return function cleanup() {
                mounted = false
            }
    }, [])

    const mapRangesToTable = (ranges) => {
        return ranges.map((range, i) => {
            return (
                <tr key={i}>
                    <td>{range.xmlId}</td>
                    <td>{range.text}</td>
                    <td>{range.quote}</td>
                    <td>{range.start}</td>
                    <td>{range.end}</td>
                    <td>{range.startOffset}</td>
                    <td>{range.endOffset}</td>
                </tr>
            )
        })
    }

    const mapTweetsToTable = () => {
        return list.map((citation, i) => {
            return (
                <tr key={i}>
                    <td>{citation.data}</td>
                    <td><Link className="admin-link" to={`/fragments/fragment/${citation.xmlId}`}></Link></td>
                    <td><span className="admin-link" onClick={() => {
                        window.location.href = citation.sourceLink; 
                        }}></span>
                    </td>
                    <td>
                    <table className="admin-simple-table">
                        <thead>
                            <tr>
                                <td>Inter</td>
                                <td>Text</td>
                                <td>Quote</td>
                                <td>Start</td>
                                <td>End</td>
                                <td>StartOffset</td>
                                <td>EndOffset</td>
                            </tr>
                        </thead>
                        <tbody>
                        {
                            mapRangesToTable(citation.rangeList)
                        }
                        </tbody>
                    </table>
                </td>
                    <td>{citation.awareSetSize}</td>
                    <td>{citation.numberOfRetweets}</td>
                    <td>{citation.location}</td>
                    <td>{citation.country}</td>
                    <td>{citation.username}</td>
                    <td>{citation.userProfileURL}</td>
                    <td>{citation.userImageURL}</td>
                </tr>
            )
        })
    }

    const deleteTweetsHandler = () =>{
        
    }
    const generateCitationsHandler = () =>{
        
    }
    return (
        <div className="admin">
            <p className="admin-title">Manage Tweets</p>

            <span className="admin-delete-button" style={{marginTop:"20px", marginBottom:"20px", float:"unset"}} onClick={() => deleteTweetsHandler()}>
                <p style={{marginRight:"5px"}}>X</p>
                <p style={{fontWeight:400}}>Delete Tweets ({list.length})</p>
            </span>
            <span className="admin-delete-button" style={{marginTop:"20px", marginBottom:"20px", float:"unset"}} onClick={() => generateCitationsHandler()}>
                <Edit style={{marginRight:"5px", fill:"#fff"}}></Edit>
                <p style={{fontWeight:400}}>Generate Citations</p>
            </span>
            {
            !(list.length>0)?
            <div>
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Fragment</th>
                            <th>Source Link</th>
                            <th>Info Ranges</th>
                            <th>Number of Annotations</th>
                            <th>Number of Retweets</th>
                            <th>Location</th>
                            <th>Country</th>
                            <th>Username</th>
                            <th>User Profile</th>
                            <th>User Image</th>
                        </tr>
                    </thead>
                    <tbody>
                        {mapTweetsToTable()}
                    </tbody>
                </table>
            </div>
            
            :null
            }
        </div>
    )
}

export default Tweets