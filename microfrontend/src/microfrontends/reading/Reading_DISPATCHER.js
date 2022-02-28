import React from 'react';
import Reading from './pages/Reading';
import Citations from './pages/Citations';
import '../../resources/css/reading/Reading.css'
import '../../resources/css/common/Table.css'
import '../../resources/css/common/SearchInput.css'

import { Route, Switch } from 'react-router';


const Reading_DISPATCHER = (props) => {
    return (
        <div style={{marginTop:"150px"}}>
            <Switch> 
                <Route path={"/reading/ldod-visual"} />
                <Route path={"/reading/citations"}>
                    <Citations language={props.language} messages={props.messages}/>
                </Route>
                <Route path={"/reading"}>
                    <Reading language={props.language} messages={props.messages}/>
                </Route>
            </Switch>
        </div>
        
        
        
    )
}

export default Reading_DISPATCHER
