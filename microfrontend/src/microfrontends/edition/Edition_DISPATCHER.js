import React, {useEffect, useState} from 'react'
import { Route, useLocation} from "react-router-dom";
import ExpertEditionList from './pages/ExpertEditionList';
import { getEdition } from '../../util/utilsAPI';
import VirtualEditionList from './pages/VirtualEditionList';
import UserContributions from './pages/UserContributions';
import TaxonomyList from './pages/TaxonomyList';
import CategoryList from './pages/CategoryList';



const Edition_DISPATCHER = (props) => {

    const [page, setPage] = useState("")
    const [acronym, setAcronym] = useState(null)
    const [user, setUser] = useState("")
    const [category, setCategory] = useState("")

    const location = useLocation()

    useEffect(() => {
        let aux = location.pathname.split("/")
        if(aux[2] === "acronym"){
            setAcronym(aux[3])
            getEdition(aux[3])
                .then(res => {
                    if(res.data === "EDITORIAL") setPage("EDITORIAL")
                    else if(res.data === "VIRTUAL") setPage("VIRTUAL")
                })
                .catch(err => {
                    console.log(err);
                })
        }
        if(aux[2] === "user"){
            setUser(aux[3])
        }
        if(aux[5]) setCategory(aux[5])
        
    }, [location])

    return (
        <div>
            <Route exact path={`/edition/acronym/${acronym}`} >
                {page==="EDITORIAL"?
                    <ExpertEditionList
                        acronym={acronym}
                        language={props.language}
                        messages={props.messages}
                    />:null
                }
                {page==="VIRTUAL"?
                    <VirtualEditionList
                        acronym={acronym}
                        language={props.language}
                        messages={props.messages}
                    />:null
                }
            </Route>
            <Route exact path={`/edition/user/${user}`}>
                <UserContributions
                    user={user}
                    language={props.language}
                    messages={props.messages}
                />
            </Route>
            <Route exact path={`/edition/acronym/${acronym}/taxonomy`}>
                <TaxonomyList
                    acronym={acronym}
                    language={props.language}
                    messages={props.messages}
                />
            </Route>
            <Route exact path={`/edition/acronym/${acronym}/category/${category}`}>
                <CategoryList
                    acronym={acronym}
                    category={category}
                    language={props.language}
                    messages={props.messages}
                />
            </Route>
        </div>
    )
}

export default Edition_DISPATCHER