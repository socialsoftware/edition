import React from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from 'react-router-dom';
import { NavDropdown, MenuItem } from 'react-bootstrap';
import ReactDOM from 'react-dom';
import StaticPage from './staticPage';
import i18next from './i18next';
import './resources/css/style.css';
import './resources/css/ldod.css';
import './resources/css/font-awesome.min.css';
import './resources/css/bootstrap.min.css';

function TopBarStatic() {
    return (
        <div className={'container-fluid'}>
            <div className={'container'}>
                <div className={'navbar-header'}>
                    <Link to={'/'} className={'navbar-brand'}>{i18next.t('appName', 'en')}</Link>
                    <ul className={'nav navbar-nav navbar-right hidden-xs'}>
                        <li>
                            <a>{i18next.t('login')}</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

function TopBarElement(props) {
    if (!props.subsections) {
        return (
            <li>
                <Link to={props.baseLink}>{props.title}</Link>
            </li>
        );
    }
    // This happens because of Documents menu
    let baseLink = '';
    if (props.baseLink) {
        baseLink = `/${props.baseLink}`;
    }
    const subsecs = props.subsections.map(subsection =>
        (<Route render={({ history }) => (
            <MenuItem
                onClick={() => { history.push(`${baseLink}/${subsection.link}`); }}>
                {subsection.title}
            </MenuItem>
        )} />));


    if (props.division) {
        subsecs.splice(props.division, 0, <MenuItem divider />);
    }

    return (
        <NavDropdown title={props.title}>
            <div className={'dropdown-menu-bg'} />
            {subsecs}
        </NavDropdown>
    );
}

function TopBarList() {
    return (
        <div className={'container'}>
            <div className={'collapse navbar-collapse'}>
                <ul className={'nav navbar-nav navbar-nav-flex'}>
                    {/* About */}
                    <TopBarElement
                        title={i18next.t('topBar.about.title')}
                        baseLink={'about'}
                        subsections={[
                            { title: i18next.t('topBar.about.archive'), link: 'archive' },
                            { title: i18next.t('topBar.about.videos'), link: 'videos' },
                            { title: i18next.t('topBar.about.faq'), link: 'faq' },
                            { title: i18next.t('topBar.about.encoding'), link: 'encoding' },
                            { title: i18next.t('topBar.about.articles'), link: 'articles' },
                            { title: i18next.t('topBar.about.conduct'), link: 'conduct' },
                            { title: i18next.t('topBar.about.privacy'), link: 'privacy' },
                            { title: i18next.t('topBar.about.team'), link: 'team' },
                            { title: i18next.t('topBar.about.acknowledgements'), link: 'acknowledgements' },
                            { title: i18next.t('topBar.about.contact'), link: 'contact' },
                            { title: i18next.t('topBar.about.copyright'), link: 'copyright' },
                        ]} />
                    {/* Reading */}
                    <TopBarElement title={i18next.t('topBar.reading')} baseLink={'reading'} />
                    {/* Documents */}
                    <TopBarElement
                        title={i18next.t('topBar.documents.title')}
                        subsections={[
                            { title: i18next.t('topBar.documents.witnesses'), link: 'source/list' },
                            { title: i18next.t('topBar.documents.fragments'), link: 'fragments' },
                        ]} />
                    {/* Editions */}
                    <TopBarElement
                        title={i18next.t('topBar.editions.title')}
                        baseLink={'edition'}
                        subsections={[
                            { title: 'Jacinto do Prado Coelho', link: 'acronym/JPC' },
                            { title: 'Teresa Sobral Cunha', link: 'acronym/TSC' },
                            { title: 'Richard Zenith', link: 'acronym/RZ' },
                            { title: 'Jerónimo Pizarro', link: 'acronym/JP' },
                            { title: 'Arquivo LdoD', link: 'acronym/LdoD-Arquivo' },
                            { title: 'LdoD-JPC-ANOT', link: 'acronym/JPC-anot' },
                            { title: 'LdoD-MALLET', link: 'acronym/LdoD-Mallet' },
                        ]}
                        division={5} />
                    {/* Search */}
                    <TopBarElement
                        title={i18next.t('topBar.search.title')}
                        baseLink={'search'}
                        subsections={[
                            { title: i18next.t('topBar.search.simple'), link: 'simple' },
                            { title: i18next.t('topBar.search.advanced'), link: 'advanced' },
                        ]} />
                    {/* Virtual */}
                    <TopBarElement title={i18next.t('topBar.virtual')} baseLink={'virtualeditions'} />
                    {/* Lang */}
                    <li className={'nav-lang'} >
                        <a onClick={() => i18next.changeLanguage('pt')}>PT</a>
                        <a onClick={() => i18next.changeLanguage('en')}>EN</a>
                        <a onClick={() => i18next.changeLanguage('es')}>ES</a>
                    </li>
                </ul>
            </div>
        </div>
    );
}


function TopBar() {
    return (
        <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
            <TopBarStatic />
            <TopBarList />
        </nav>
    );
}

function App() {
    return (
        <Router>
            <div>
                <TopBar />
                <Switch>
                    <Route path={'/about/archive'} render={() => <StaticPage url={'/about/archive'} />} />
                    <Route path={'/about/videos'} render={() => <StaticPage url={'/about/videos'} />} />
                    <Route path={'/about/encoding'} render={() => <StaticPage url={'/about/encoding'} />} />
                    <Route path={'/about/articles'} render={() => <StaticPage url={'/about/articles'} />} />
                    <Route path={'/about/conduct'} render={() => <StaticPage url={'/about/conduct'} />} />
                    <Route path={'/about/privacy'} render={() => <StaticPage url={'/about/privacy'} />} />
                    <Route path={'/about/team'} render={() => <StaticPage url={'/about/team'} />} />
                    <Route path={'/about/acknowledgements'} render={() => <StaticPage url={'/about/acknowledgements'} />} />
                    <Route path={'/about/contact'} render={() => <StaticPage url={'/about/contact'} />} />
                    <Route path={'/about/copyright'} render={() => <StaticPage url={'/about/copyright'} />} />
                    <Route exact path={'/'} render={() => <StaticPage url={'/'} />} />
                </Switch>
            </div>
        </Router>
    );
}


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);
