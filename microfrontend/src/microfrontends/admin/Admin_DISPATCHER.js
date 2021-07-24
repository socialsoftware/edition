import React, { useEffect } from 'react';
import { Route, Switch, useHistory} from "react-router-dom";
import Load from './pages/Load';
import Export from './pages/Export';
import Delete from './pages/Delete';
import Tweets from './pages/Tweets';
import Virtual from './pages/Virtual';
import User from './pages/User';
import UserEdit from './pages/UserEdit';

import '../../resources/css/admin/Admin.css'

const Admin_DISPATCHER = (props) => {

    const history = useHistory()

    useEffect(() => {
        if(!props.isAuthenticated){
            alert("Acesso Negado")
            history.push("/")
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.isAuthenticated])

    return (
        <div style={{marginTop:"150px"}}>
            <Switch>
                <Route path="/admin/loadForm" 
                    component={() => <Load 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/admin/exportForm" 
                    component={() => <Export 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/admin/fragment" 
                    component={() => <Delete 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/admin/user/list" 
                    component={() => <User 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/admin/user/edit" 
                    component={() => <UserEdit 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/admin/virtual" 
                    component={() => <Virtual 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/admin/tweets" 
                    component={() => <Tweets 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
            </Switch>
        </div>
    )
}

export default Admin_DISPATCHER