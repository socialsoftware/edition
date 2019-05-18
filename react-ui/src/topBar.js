import React from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { NavDropdown, MenuItem, NavItem } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { FormattedMessage } from 'react-intl';
import LanguageToggle from './languageToggle';

function TopBarStatic() {
    return (
        <div className={'container-fluid'}>
            <div className={'container'}>
                <div className={'navbar-header'}>
                    <Link to={'/'} className={'navbar-brand'}><FormattedMessage id={'appName'} /></Link>
                    <ul className={'nav navbar-nav navbar-right hidden-xs'}>
                        <li>
                            <a><FormattedMessage id={'login'} /></a>
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
                <Link key={props.baseLink} to={`/${props.baseLink}`}>{props.title}</Link>
            </li>
        );
    }

    let baseLink = '';
    if (props.baseLink) {
        baseLink = `/${props.baseLink}`;
    }
    const subsecs = props.subsections.map(subsection =>
        (<LinkContainer key={subsection.link} to={`${baseLink}/${subsection.link}`}>
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

function TopBarList() {
    return (
        <div className={'container'}>
            <div className={'collapse navbar-collapse'}>
                <ul className={'nav navbar-nav navbar-nav-flex'}>
                    {/* About */}
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
                    {/* Reading */}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.reading.title'} />}
                        subsections={[
                        { title: <FormattedMessage id={'topBar.reading.reading'} />, link: 'reading' },
                        { title: <FormattedMessage id={'topBar.reading.visual'} />, link: 'ldod-visual' },
                        { title: <FormattedMessage id={'topBar.reading.citations'} />, link: 'citations' },
                        ]} />
                    {/* Documents */}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.documents.title'} />}
                        subsections={[
                            { title: <FormattedMessage id={'topBar.documents.witnesses'} />, link: 'source/list' },
                            { title: <FormattedMessage id={'topBar.documents.fragments'} />, link: 'fragments' },
                        ]} />
                    {/* Editions */}
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
                    {/* Search */}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.search.title'} />}
                        baseLink={'search'}
                        subsections={[
                            { title: <FormattedMessage id={'topBar.search.simple'} />, link: 'simple' },
                            { title: <FormattedMessage id={'topBar.search.advanced'} />, link: 'advanced' },
                        ]} />
                    {/* Virtual */}
                    <TopBarElement
                        title={<FormattedMessage id={'topBar.virtual.title'} />}
                        subsections={[
                        { title: <FormattedMessage id={'topBar.virtual.editions'} />, link: 'virtualeditions' },
                        { title: <FormattedMessage id={'topBar.virtual.game'} />, link: 'classificationGames' },
                        ]} />
                    {/* Lang */}
                    <LanguageToggle />
                </ul>
            </div>
        </div>
    );
}

export default class TopBar extends React.Component {

    static baseURL = 'http://localhost:8080';

    constructor(props) {
        super(props);
        this.state = {
            moduleInfo: null,
            isLoaded: false,
        };
    }

    retrieveModuleInfoData() {
        axios.get('http://localhost:8080/api/services/frontend/module-info')
            .then((result) => {
                this.setState({
                    moduleInfo: result.data,
                    isLoaded: true,
                });
            },
                  (result) => {
                      console.log(`Failed to load moduleInfo. Status code ${result.status}`);
                  });
    }

    componentDidMount() {
        this.retrieveModuleInfoData();
    }

    render() {
        if (this.state.isLoaded) {
            console.log('Got module info from server');
            console.log(this.state.moduleInfo);

            const moduleNames = Object.keys(this.state.moduleInfo);

            const topBarComponents = [];

            for (let i = 0; i < moduleNames.length; i++) {
                console.log(moduleNames[i]);

                const menus = Object.keys(this.state.moduleInfo[moduleNames[i]]);

                for (let j = 0; j < menus.length; j++) {
                    const options = this.state.moduleInfo[moduleNames[i]][menus[j]];

                    const menuJson = [];

                    for (let k = 0; k < options.length; k++) {
                        menuJson.push({ title: <FormattedMessage id={options[k].key} />,
                            link: TopBar.baseURL + options[k].value });
                    }

                    const element = (<TopBarElement
                        title={<FormattedMessage id={menus[j]} />}
                        subsections={menuJson} />);
                    topBarComponents.push(element);
                }
            }

            console.log(topBarComponents);

            return (
                <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
                    <TopBarStatic />

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
        } console.log('Info not loaded');

        return (
            <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
                <TopBarStatic />
                <TopBarList />
            </nav>
        );
    }

}


/* export default function TopBar() {

    return (
        <nav className={'ldod-navbar navbar navbar-default navbar-fixed-top'}>
            <TopBarStatic />
            <TopBarList />
        </nav>
    );
} */
