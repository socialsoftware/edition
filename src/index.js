import React from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
import { NavDropdown, MenuItem } from 'react-bootstrap';
import ReactDOM from 'react-dom';
import HomePage from './home';
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
                <a>{props.title}</a>
            </li>
        );
    }

    const subsecs = props.subsections.map(subsection => <MenuItem>{subsection}</MenuItem>);

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
                    <li><Link to={'/about'}>about</Link></li>
                    <TopBarElement
                        title={'Acerca'}
                        subsections={['Arquivo LdoD',
                            'Vídeos',
                            'Perguntas Frequentes',
                            'Codificação de Texto',
                            'Bibliografia',
                            'Código de Conduta',
                            'Política de Privacidade',
                            'Equipa Editorial',
                            'Agradecimentos',
                            'Contacto',
                            'Copyright']} />
                    {/* Reading */}
                    <TopBarElement title={'Leitura'} />
                    {/* Documents */}
                    <TopBarElement title={'Documentos'} subsections={['Testemunhos', 'Fragmentos Codificados']} />
                    {/* Editions */}
                    <TopBarElement
                        title={'Edições'}
                        subsections={['Jacinto do Prado Coelho',
                            'Teresa Sobral Cunha',
                            'Richard Zenith',
                            'Jerónimo Pizarro',
                            'Arquivo LdoD',
                            'LdoD-JPC-ANOT',
                            'LdoD-MALLET']}
                        division={5} />
                    {/* Search */}
                    <TopBarElement title={'Pesquisa'} subsections={['Pesquisa Simples', 'Pesquisa Avançada']} />
                    {/* Virtual */}
                    <TopBarElement title={'Virtual'} />
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
                <Route path={'/'} component={HomePage} />
            </div>
            {/* <Route path={'/about'} component={About} /> */}
        </Router>
    );
}


ReactDOM.render(
    <App />,
    document.getElementById('root'),
);
