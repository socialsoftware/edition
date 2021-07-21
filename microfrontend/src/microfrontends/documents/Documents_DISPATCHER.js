import React from 'react'
import { Route, Switch} from "react-router-dom";
import FragmentList from './pages/FragmentList';
import SourceList from './pages/SourceList';
import '../../resources/css/documents/Documents.css'
import '../../resources/css/common/Table.css'
import '../../resources/css/common/SearchInput.css'

const Documents_DISPATCHER = (props) => {
    return (
        <div style={{marginTop:"150px"}}>
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