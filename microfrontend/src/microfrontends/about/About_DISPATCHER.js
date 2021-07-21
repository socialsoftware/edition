import React from 'react'
import {Route, Switch} from "react-router-dom";
import Info from '../common/Info';
import AcknowledgementsMain from './pages/Acknowledgements_main';
import ArchiveMain from './pages/Archive_main';
import ArticlesMain from './pages/Articles_main';
import ConductMain from './pages/Conduct_main'
import ContactMain from './pages/Contact_main';
import CopyrightMain from './pages/Copyright_main';
import EncodingMain from './pages/Encoding_main';
import FaqMain from './pages/Faq_main';
import PrivacyMain from './pages/Privacy_main';
import TeamMain from './pages/Team_main';
import TutorialsMain from './pages/Tutorials_main';
import VideosMain from './pages/Videos_main';

import '../../resources/css/about/About.css'

const About_DISPATCHER = (props) => {
    return (
        <div className="about">
            <Switch>
                <Route path="/about/conduct" 
                    component={() => <ConductMain 
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/acknowledgements" 
                    component={() => <AcknowledgementsMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/archive" 
                    component={() => <ArchiveMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/articles" 
                    component={() => <ArticlesMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/contact" 
                    component={() => <ContactMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/encoding" 
                    component={() => <EncodingMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/faq" 
                    component={() => <FaqMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/privacy" 
                    component={() => <PrivacyMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/team" 
                    component={() => <TeamMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/tutorials" 
                    component={() => <TutorialsMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/videos" 
                    component={() => <VideosMain
                    language={props.language}
                    messages={props.messages}
                    />}>
                </Route>
                <Route path="/about/copyright" 
                    component={() => <CopyrightMain
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