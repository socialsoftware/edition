import React from 'react';
import { Switch } from 'react-router';
import { Route } from 'react-router-dom';
import Advanced from './pages/Advanced';
import Simple from './pages/Simple';
import '../../resources/css/search/Search.css'
import '../../resources/css/common/Table.css'
import '../../resources/css/common/SearchInput.css'

const Search_DISPATCHER = (props) => {
    return (
        <div style={{marginTop:"150px"}}>
            <Switch>
                <Route path="/search/simple" 
                    component={() => <Simple 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/search/advanced" 
                    component={() => <Advanced
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
            </Switch>
        </div>
    )
}

export default Search_DISPATCHER