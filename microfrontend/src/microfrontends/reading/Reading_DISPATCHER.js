import React from 'react';
import Reading from './pages/Reading';
import '../../resources/css/reading/Reading.css'
import '../../resources/css/common/Table.css'
import '../../resources/css/common/SearchInput.css'

import { Route, Switch } from 'react-router';
import Citations from './pages/Citations';

const Reading_DISPATCHER = (props) => {
    return (
        <Switch>
            <Route path={"/reading/citations"}>
                <Citations language={props.language} messages={props.messages}/>
            </Route>
            <Route path={"/reading"}>
                <Reading language={props.language} messages={props.messages}/>
            </Route>
        </Switch>
        
        
    )
}

export default Reading_DISPATCHER
