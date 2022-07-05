import React from 'react';
import { Switch } from 'react-router';
import { Route } from 'react-router-dom';
import Editions from './pages/Editions'
import Manage from './pages/Manage'
import Participants from './pages/Participants'
import Recommendation from './pages/Recommendation'
import Manual from './pages/Manual'
import Taxonomy from './pages/Taxonomy'
import Category from './pages/Category'
import FragInter from './pages/FragInter'
import JogoClassificacao from './pages/JogoClassificacao';

import '../../resources/css/virtual/Virtual.css'

const Virtual_DISPATCHER = (props) => {
    return (
        <div style={{marginTop:"150px"}}>
            <Switch>
                <Route exact path="/virtual/virtualeditions" 
                    component={() => <Editions 
                    isAuthenticated={props.isAuthenticated}
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/manage" 
                    component={() => <Manage 
                    isAuthenticated={props.isAuthenticated}
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/participants" 
                    component={() => <Participants 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/recommendation" 
                    component={() => <Recommendation 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/manual" 
                    component={() => <Manual 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/taxonomy" 
                    component={() => <Taxonomy 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/category" 
                    component={() => <Category 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/virtualeditions/restricted/fraginter" 
                    component={() => <FragInter 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/virtual/classificationGames" 
                    component={() => <JogoClassificacao
                    messages={props.messages}
                    />}>
                </Route>
            </Switch>
        </div>
    )
}

export default Virtual_DISPATCHER