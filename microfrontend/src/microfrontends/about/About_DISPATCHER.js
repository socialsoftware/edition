import React from 'react'
import { Route, Switch} from "react-router-dom";
import Info from '../../util/Info';
import Acknowledgements_main from './pages/Acknowledgements_main';
import Archive_main from './pages/Archive_main';
import Articles_main from './pages/Articles_main';
import Conduct_main from './pages/Conduct_main'
import Contact_main from './pages/Contact_main';
import Copyright_main from './pages/Copyright_main';
import Encoding_main from './pages/Encoding_main';
import Faq_main from './pages/Faq_main';
import Privacy_main from './pages/Privacy_main';
import Team_main from './pages/Team_main';
import Tutorials_main from './pages/Tutorials_main';
import Videos_main from './pages/Videos_main';

const About_DISPATCHER = (props) => {
    return (
        <div>
            <Switch>
                <Route path="/about/conduct" 
                    component={() => <Conduct_main 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/acknowledgements" 
                    component={() => <Acknowledgements_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/archive" 
                    component={() => <Archive_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/articles" 
                    component={() => <Articles_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/contact" 
                    component={() => <Contact_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/encoding" 
                    component={() => <Encoding_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/faq" 
                    component={() => <Faq_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/privacy" 
                    component={() => <Privacy_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/team" 
                    component={() => <Team_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/tutorials" 
                    component={() => <Tutorials_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/videos" 
                    component={() => <Videos_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/copyright" 
                    component={() => <Copyright_main
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                
            </Switch>
            <Info language={props.language}/> {/* bottom of page info */}
        </div>
    )
}

export default About_DISPATCHER