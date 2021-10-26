import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import { ReactComponent as Edit } from '../../../resources/assets/edit.svg'
import { getTweets } from '../../../util/API/AdminAPI';
import InfiniteScroll from 'react-infinite-scroll-component';
import CircleLoader from "react-spinners/RotateLoader";

const Tweets = (props) => {

    const [list, setList] = useState([])
    const [size, setSize] = useState(null)
    const [currentList, setCurrentList] = useState([])
    const [currentPosition, setCurrentPosition] = useState(1)

    useEffect(() => {
        var mounted = true
        getTweets()
            .then(res => {
                if(mounted){
                    setList(res.data.citations)
                    setSize(res.data.tweetsSize)
                    setCurrentList(res.data.citations.slice(0,19))
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
        return currentList.map((citation, i) => {
            return (
                <tr key={i}>
                    <td>{citation.data}</td>
                    <td><Link className="admin-link" to={`/fragments/fragment/${citation.xmlId}`}>{citation.title}</Link></td>
                    <td><a rel="noreferrer" target="_blank" className="admin-link" 
                        href={citation.sourceLink}>Tweet</a>
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
                    <td><a rel="noreferrer" target="_blank" className="admin-link" 
                        href={citation.userProfileURL}>{citation.userProfileURL}</a></td>
                    <td style={{maxWidth:"650px"}}><a rel="noreferrer" target="_blank" className="admin-link" 
                        href={citation.userImageURL}>{citation.userImageURL}</a></td>
                </tr>
            )
        })
    }

    const fetchMoreData = () => {
        let aux = [...currentList]
        aux = aux.concat(list.slice(currentPosition*20, currentPosition*20+19))
        let val = currentPosition + 1
        setCurrentPosition(val)
        setCurrentList(aux)
    }
    const deleteTweetsHandler = () =>{
        //to do
        //dont want to risk it, didnt have access to a local version of tweets - lucas perry
    }

    const generateCitationsHandler = () =>{
        //to do
    }

    return (
        <div className="admin">
            <p className="admin-title">Manage Tweets</p>

            {
                list.length===0?
                    <div style={{marginTop:"50px"}}>
                        <CircleLoader loading={list.length===0}></CircleLoader>
                    </div>
                    
                :
                <div>
                    <span className="admin-delete-button" style={{marginTop:"20px", marginBottom:"20px", float:"unset"}} onClick={() => deleteTweetsHandler()}>
                        <p style={{marginRight:"5px"}}>X</p>
                        <p style={{fontWeight:400}}>Delete Tweets ({size})</p>
                    </span>
                    <span className="admin-delete-button" style={{marginTop:"20px", marginBottom:"20px", float:"unset"}} onClick={() => generateCitationsHandler()}>
                        <Edit style={{marginRight:"5px", fill:"#fff"}}></Edit>
                        <p style={{fontWeight:400}}>Generate Citations</p>
                    </span>
                    
                    <div>
                    <InfiniteScroll
                        style={{width:"2650px"}}
                        dataLength={currentList.length}
                        next={fetchMoreData}
                        hasMore={currentList.length <= list.length}
                        loader={<h4>Loading...</h4>}
                        endMessage={
                            <p style={{ textAlign: "center" }}>
                            <b>Fim</b>
                            </p>
                        }
                        >
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
                        </InfiniteScroll>
                        
                    </div>
                </div>
            }
        </div>
    )
}

export default Tweets