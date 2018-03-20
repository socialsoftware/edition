import React from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from 'react-router-dom';
import { NavDropdown, MenuItem } from 'react-bootstrap';
import ReactDOM from 'react-dom';
import HomePage from './home';
import About from './about';
import './resources/css/style.css';
import './resources/css/ldod.css';
import './resources/css/font-awesome.min.css';
import './resources/css/bootstrap.min.css';

function TopBarStatic() {
    return (
        <div className={'container-fluid'}>
            <div className={'container'}>
                <div className={'navbar-header'}>
                    <a className={'navbar-brand'}>Arquivo LdoD</a>
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
        <MenuItem href={`${baseLink}/${subsection.link}`}>{subsection.title}</MenuItem>);
    console.log(subsecs);

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
                    <Route path={'/about/archive'} render={() => <About url={'archive'} />} />
                    <Route path={'/'} component={HomePage} />
                </Switch>
            </div>
        </Router>
    );
}


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);
