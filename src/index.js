import React from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from 'react-router-dom';
import { NavDropdown, MenuItem } from 'react-bootstrap';
import ReactDOM from 'react-dom';
import StaticPage from './staticPage';
import './resources/css/style.css';
import './resources/css/ldod.css';
import './resources/css/font-awesome.min.css';
import './resources/css/bootstrap.min.css';

function TopBarStatic() {
    return (
        <div className={'container-fluid'}>
            <div className={'container'}>
                <div className={'navbar-header'}>
                    <Link to={'/'} className={'navbar-brand'}>Arquivo LdoD</Link>
                    <ul className={'nav navbar-nav navbar-right hidden-xs'}>
                        <li>
                            <a>Iniciar Sessão</a>
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
                        title={'Acerca'}
                        baseLink={'about'}
                        subsections={[
                            { title: 'Arquivo LdoD', link: 'archive' },
                            { title: 'Vídeos', link: 'videos' },
                            { title: 'Preguntas Frequentes', link: 'faq' },
                            { title: 'Codificação de Texto', link: 'encoding' },
                            { title: 'Bibliografia', link: 'articles' },
                            { title: 'Código de Conduta', link: 'conduct' },
                            { title: 'Política de Privacidade', link: 'privacy' },
                            { title: 'Equipa Editorial', link: 'team' },
                            { title: 'Agradecimentos', link: 'acknowledgements' },
                            { title: 'Contacto', link: 'contact' },
                            { title: 'Copyright', link: 'copyright' },
                        ]} />
                    {/* Reading */}
                    <TopBarElement title={'Leitura'} baseLink={'reading'} />
                    {/* Documents */}
                    <TopBarElement
                        title={'Documentos'}
                        subsections={[
                            { title: 'Testemunhos', link: 'source/list' },
                            { title: 'Fragmentos Codificados', link: 'fragments' },
                        ]} />
                    {/* Editions */}
                    <TopBarElement
                        title={'Edições'}
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
                        title={'Pesquisa'}
                        baseLink={'search'}
                        subsections={[
                            { title: 'Pesquisa Simples', link: 'simple' },
                            { title: 'Pesquisa Avançada', link: 'advanced' },
                        ]} />
                    {/* Virtual */}
                    <TopBarElement title={'Virtual'} baseLink={'virtualeditions'} />
                    {/* Lang */}
                    <li className={'nav-lang'} >
                        <a className={'active'}>PT</a>
                        <a>EN</a>
                        <a>ES</a>
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
