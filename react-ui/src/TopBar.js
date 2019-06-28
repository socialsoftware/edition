import React from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { NavDropdown, MenuItem, NavItem } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { FormattedMessage } from 'react-intl';
import { connect } from 'react-redux';
import LanguageToggle from './languageToggle';
import { setAccessToken, setModuleConfig } from './actions/actions';
import { SERVER_URL } from './utils/Constants';

const mapStateToProps = state => ({ token: state.token, info: state.info });

function TopBarStatic(props) {
    let loginToggle = null;

    if (props.userExists && props.token === '') {
        loginToggle = (
            <li>
                <a href="/signin"><FormattedMessage id={'login'} /></a>
            </li>
        );
    } else if (props.userExists) {
        loginToggle = (
            <li className=" dropdown login logged-in ">
                <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                    {props.name}
                    <span className="caret" />
                </a>

                <ul className="dropdown-menu">
                    <li><a href="#">
                        <FormattedMessage id={'user.password'} />
                    </a></li>
                    <li><a href="#" onClick={props.logout}>
                        <FormattedMessage id={'header.logout'} />
                    </a></li>
                </ul>
            </li>
        );
    }


    return (
        <div className={'container-fluid'}>
            <div className={'container'}>
                <div className={'navbar-header'}>
                    <Link to={'/'} className={'navbar-brand'}><FormattedMessage id={'appName'} /></Link>
                    <ul className={'nav navbar-nav navbar-right hidden-xs'}>
                        {loginToggle}
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
                <Link key={props.baseLink} to={`/${props.baseLink}`}>{props.title}</Link>
            </li>
        );
    }


    const subsecs = props.subsections.map(subsection => (<LinkContainer key={subsection.link} to={`${subsection.link}`}>
        <MenuItem key={subsection.link}>{subsection.title}</MenuItem>
    </LinkContainer>),
    );

    if (props.division) {
        subsecs.splice(props.division, 0, <MenuItem key={0} divider />);
    }

    return (
        <NavDropdown key={props.baseLink} title={props.title} id={'Navigation Menu'}>
            <NavItem className={'dropdown-menu-bg'} />
            {subsecs}
        </NavDropdown>
    );
}

/* function TopBarList() {
    return (
        <div className={'container'}>
            <div className={'collapse navbar-collapse'}>
                <ul className={'nav navbar-nav navbar-nav-flex'}>
                    {/!* About *!/}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.about.title'} />}
                        baseLink={'about'}
                        subsections={[
                            { title: <FormattedMessage id={'topBar.about.archive'} />, link: 'archive' },
                            { title: <FormattedMessage id={'topBar.about.videos'} />, link: 'videos' },
                            { title: <FormattedMessage id={'topBar.about.faq'} />, link: 'faq' },
                            { title: <FormattedMessage id={'topBar.about.encoding'} />, link: 'encoding' },
                            { title: <FormattedMessage id={'topBar.about.articles'} />, link: 'articles' },
                            { title: <FormattedMessage id={'topBar.about.conduct'} />, link: 'conduct' },
                            { title: <FormattedMessage id={'topBar.about.privacy'} />, link: 'privacy' },
                            { title: <FormattedMessage id={'topBar.about.team'} />, link: 'team' },
                            { title: <FormattedMessage id={'topBar.about.acknowledgements'} />, link: 'acknowledgements' },
                            { title: <FormattedMessage id={'topBar.about.contact'} />, link: 'contact' },
                            { title: <FormattedMessage id={'topBar.about.copyright'} />, link: 'copyright' },
                        ]} />
                    {/!* Reading *!/}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.reading.title'} />}
                        subsections={[
                        { title: <FormattedMessage id={'topBar.reading.reading'} />, link: 'reading' },
                        { title: <FormattedMessage id={'topBar.reading.visual'} />, link: 'ldod-visual' },
                        { title: <FormattedMessage id={'topBar.reading.citations'} />, link: 'citations' },
                        ]} />
                    {/!* Documents *!/}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.documents.title'} />}
                        subsections={[
                            { title: <FormattedMessage id={'topBar.documents.witnesses'} />, link: 'source/list' },
                            { title: <FormattedMessage id={'topBar.documents.fragments'} />, link: 'fragments' },
                        ]} />
                    {/!* Editions *!/}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.editions.title'} />}
                        baseLink={'edition'}
                        subsections={[
                            { title: 'Jacinto do Prado Coelho', link: 'acronym/JPC' },
                            { title: 'Teresa Sobral Cunha', link: 'acronym/TSC' },
                            { title: 'Richard Zenith', link: 'acronym/RZ' },
                            { title: 'Jerónimo Pizarro', link: 'acronym/JP' },
                            { title: 'Arquivo LdoD', link: 'acronym/LdoD-Arquivo' },
                            { title: 'LdoD-JPC-ANOT', link: 'acronym/JPC-anot' },
                            { title: 'LdoD-JOGO-CLASS', link: 'acronym/LdoD-Jogo-Class' },
                            { title: 'LdoD-MALLET', link: 'acronym/LdoD-Mallet' },
                            { title: 'LdoD-TWITTER', link: 'acronym/LdoD-Twitter' },
                        ]}
                        division={5} />
                    {/!* Search *!/}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.search.title'} />}
                        baseLink={'search'}
                        subsections={[
                            { title: <FormattedMessage id={'topBar.search.simple'} />, link: 'simple' },
                            { title: <FormattedMessage id={'topBar.search.advanced'} />, link: 'advanced' },
                        ]} />
                    {/!* Virtual *!/}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.virtual.title'} />}
                        subsections={[
                        { title: <FormattedMessage id={'topBar.virtual.editions'} />, link: 'virtualeditions' },
                        { title: <FormattedMessage id={'topBar.virtual.game'} />, link: 'classificationGames' },
                        ]} />
                    {/!* Lang *!/}
                    <LanguageToggle />
                </ul>
            </div>
        </div>
    );
} */

class TopBar extends React.Component {

    static baseURL = SERVER_URL;

    constructor(props) {
        super(props);

        this.logoutUser = this.logoutUser.bind(this);

        this.state = {
            moduleInfo: null,
            isLoaded: false,
        };
    }

    retrieveModuleInfoData() {
        axios.get(`${SERVER_URL}/api/services/frontend/module-info`)
            .then((result) => {
                this.props.setModuleConfig(result.data);

                this.setState({
                    moduleInfo: result.data,
                    isEditionLoaded: true,
                });
            },
                  (result) => {
                      console.log(`Failed to load moduleInfo. Status code ${result.status}`);
                  });
    }

    logoutUser(event) {
        event.preventDefault();
        this.props.setAccessToken('');
        console.log(this.props);
    }

    componentDidMount() {
        this.retrieveModuleInfoData();
    }

    render() {
        if (this.state.isEditionLoaded) {
            const moduleNames = Object.keys(this.state.moduleInfo);

            const topBarComponents = [];

            let userExists = false;

            for (let i = 0; i < moduleNames.length; i++) {
                if (moduleNames[i] === 'edition-user') { userExists = true; }

                const menus = Object.keys(this.state.moduleInfo[moduleNames[i]]);

                for (let j = 0; j < menus.length; j++) {
                    // if (menus[j] === 'topBar.virtual.title') continue;

                    const options = this.state.moduleInfo[moduleNames[i]][menus[j]];

                    let menuJson = [];

                    for (let k = 0; k < options.length; k++) {
                        const name = Object.keys(options[k])[0];
                        menuJson.push({ title: <FormattedMessage id={name} />,
                            link: options[k][name] });
                    }

                    let foundDuplicate = false;

                    for (let k = 0; k < topBarComponents.length; k++) {
                        if (topBarComponents[k].props.title.props.id === menus[j]) {
                            menuJson = topBarComponents[k].props.subsections.concat(menuJson);
                            topBarComponents.splice(k, 1);
                            topBarComponents.splice(k, 0, (<TopBarElement
                                title={<FormattedMessage id={menus[j]} />}
                                subsections={menuJson} />));
                            foundDuplicate = true;
                            break;
                        }
                    }

                    if (foundDuplicate) continue;

                    const element = (<TopBarElement
                        title={<FormattedMessage id={menus[j]} />}
                        subsections={menuJson} />);
                    topBarComponents.push(element);
                }
            }

            const name = this.props.info !== null ? `${this.props.info.firstName} ${this.props.info.lastName} ` : <FormattedMessage id={'login'} />;

            return (
                <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
                    <TopBarStatic
                        userExists={userExists}
                        token={this.props.token}
                        logout={this.logoutUser}
                        name={name} />

                    <div className={'container'}>
                        <div className={'collapse navbar-collapse'}>
                            <ul className={'nav navbar-nav navbar-nav-flex'}>
                                {topBarComponents}
                                <LanguageToggle />
                            </ul>
                        </div>
                    </div>
                </nav>
            );
        }

        return (
            <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
                <TopBarStatic />
            </nav>
        );
    }

}

export default connect(mapStateToProps, { setModuleConfig, setAccessToken })(TopBar);

/* export default function TopBar() {

    return (
        <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
            <TopBarStatic />
            <TopBarList />
        </nav>
    );
} */
