import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './Header.css';
import { Glyphicon, Nav, Navbar, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';

class HeaderNavigation extends Component {
    constructor(props) {
        super(props);
    }
    
    render() {
        let menuItems;
        if(this.props.currentUser) {
            let name = this.props.currentUser.firstName + "  " + this.props.currentUser.lastName;
            menuItems =
                <Navbar.Collapse>
                    <Nav pullRight>
                        <NavItem eventKey={1} componentClass={Link} href="/" to="/">
                            <Glyphicon glyph="home" className="nav-icon" />
                        </NavItem>
                        <NavItem eventKey={2} componentClass={Link} href="/about" to="/about">
                            About
                        </NavItem>
                        <NavItem eventKey={5} componentClass={Link} href="/leaderboard" to="/leaderboard">
                            Leaderboard
                        </NavItem>
                        <NavItem eventKey={6} componentClass={Link} href="/feedback" to="/feedback">
                            Feedback
                        </NavItem>
                        <NavDropdown eventKey={5} title={name} id="nav-dropdown">                       
                            <MenuItem eventKey={5.1} componentClass={Link} href={`/user/${this.props.currentUser.username}`} to={`/user/${this.props.currentUser.username}`}>
                                Profile
                            </MenuItem>
                            <MenuItem eventKey={5.2} onClick={this.props.onLogout}>
                                Logout
                            </MenuItem>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>;
        }
        else{
            menuItems = 
                <Navbar.Collapse>
                <Nav pullRight>
                    <NavItem eventKey={1} componentClass={Link} href="/login" to="/login">
                        Login
                    </NavItem>
                    <NavItem eventKey={2} componentClass={Link} href="/about" to="/about">
                        About
                    </NavItem>
                    <NavItem eventKey={3} componentClass={Link} href="/feedback" to="/feedback">
                       Feedback
                    </NavItem>
                </Nav>
            </Navbar.Collapse>;
        }
        return (
            <Navbar default collapseOnSelect>
                <Navbar.Header>
                    <Navbar.Brand>
                        <Link to="/">LdoD Classification Game</Link>
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                {menuItems}
            </Navbar>
    );
  }
}

export default withRouter(HeaderNavigation);