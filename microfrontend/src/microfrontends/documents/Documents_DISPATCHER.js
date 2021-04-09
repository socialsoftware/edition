import React from 'react'
import { Route, Switch} from "react-router-dom";
import FragmentList from './pages/FragmentList';
import SourceList from './pages/SourceList';


const Documents_DISPATCHER = (props) => {
    return (
        <div>
            <Switch>
                <Route path="/documents/fragments" 
                    component={() => <FragmentList 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/documents/source/list" 
                    component={() => <SourceList 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                
            </Switch>
        </div>
    )
}

export default Documents_DISPATCHER