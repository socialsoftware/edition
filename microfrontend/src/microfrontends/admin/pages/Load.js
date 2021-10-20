import React, { useState } from 'react'
import { loadCorpus, loadFragment, loadSeveralFragment,
        loadUsers, loadVirtualCorpus, loadVirtualFragments } from '../../../util/API/AdminAPI';

const Load = (props) => {

    const [corpus, setCorpus] = useState(null)
    const [fragment, setFragment] = useState(null)
    const [severalFragment, setSeveralFragment] = useState(null)
    const [users, setUsers] = useState(null)
    const [virtualCorpus, setVirtualCorpus] = useState(null)
    const [virtualFragments, setVirtualFragments] = useState(null)
    const [serverResponse, setServerResponse] = useState("")

    const handleUpload = (type) => {
        setServerResponse("")
        var data = new FormData();
        if(type === "corpus" && corpus!=null){
            data.append('file', corpus);
            loadCorpus(data)
                .then(res => {
                    setServerResponse(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        } 
        else if(type === "fragment" && fragment!=null){
            data.append('file', fragment);
            loadFragment(data)
                .then(res => {
                    setServerResponse(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        } 
        else if(type === "severalFragment" && severalFragment){
            for (let file of severalFragment) {
                data.append("files", file);                
            }
            loadSeveralFragment(data)
                .then(res => {
                    setServerResponse(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        } 
        else if(type === "users" && users){
            data.append('file', users);
            loadUsers(data)
                .then(res => {
                    setServerResponse(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        } 
        else if(type === "virtualCorpus" && virtualCorpus){
            data.append('file', virtualCorpus);
            loadVirtualCorpus(virtualCorpus)
                .then(res => {
                    setServerResponse(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        } 
        else if(type === "virtualFragments" && virtualFragments){
            for (let file of virtualFragments) {
                data.append("files", file);
            }
            loadVirtualFragments(virtualFragments)
                .then(res => {
                    setServerResponse(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        } 
    }

    
    return (
        <div className="admin">
            <div style={{display:"flex", justifyContent:"space-between", marginTop:"50px"}}>
                <div className="admin-file-div">
                    <p>{props.messages.corpus_load_title}</p>
                    <input style={{marginTop:"15px"}} type="file" name="file" onChange={(e) => {
                        setCorpus(e.target.files[0])}}>
                    </input>
                    <span className="admin-submit-button" onClick={() => handleUpload("corpus")}>
                        <span>{props.messages.general_submit}</span>
                    </span>
                </div>
                <div className="admin-file-div">
                    <p>{props.messages.fragment_load_title}</p>
                    <input style={{marginTop:"15px"}} type="file" name="file" onChange={(e) => {
                        setFragment(e.target.files[0])}}>
                    </input>
                    <span className="admin-submit-button" onClick={() => handleUpload("fragment")}>
                        <span>{props.messages.general_submit}</span>
                    </span>
                </div>
                <div className="admin-file-div">
                    <p>{props.messages.fragment_load_titleAll}</p>
                    <input style={{marginTop:"15px"}} type="file" name="file" multiple={true} onChange={(e) => {
                        setSeveralFragment(e.target.files)}}>
                    </input>
                    <span className="admin-submit-button" onClick={() => handleUpload("severalFragment")}>
                        <span>{props.messages.general_submit}</span>
                    </span>
                </div>
            </div>
            <div style={{display:"flex", justifyContent:"space-between", marginTop:"50px"}}>
                <div className="admin-file-div">
                    <p>Load Users</p>
                    <input style={{marginTop:"15px"}} type="file" name="files" onChange={(e) => {
                        setUsers(e.target.files)}}>
                    </input>
                    <span className="admin-submit-button" onClick={() => handleUpload("users")}>
                        <span>{props.messages.general_submit}</span>
                    </span>
                </div>
                <div className="admin-file-div">
                    <p>Load Virtual Editions Corpus</p>
                    <input style={{marginTop:"15px"}} type="file" name="file" onChange={(e) => {
                        setVirtualCorpus(e.target.files[0])}}>
                    </input>
                    <span className="admin-submit-button" onClick={() => handleUpload("virtualCorpus")}>
                        <span>{props.messages.general_submit}</span>
                    </span>
                </div>
                <div className="admin-file-div">
                    <p>Load Virtual Editions Fragments</p>
                    <input style={{marginTop:"15px"}} type="file" name="file" multiple={true} onChange={(e) => {
                        setVirtualFragments(e.target.files)}}>
                    </input>
                    <span className="admin-submit-button" onClick={() => handleUpload("virtualFragments")}>
                        <span>{props.messages.general_submit}</span>
                    </span>
                </div>
            </div>
            {
                serverResponse.length>0?
                    <div className="admin-response-div" dangerouslySetInnerHTML={{ __html: serverResponse }}>

                    </div>
                    :null
            }
            
        </div>
    )
}

export default Load