import React, { Component } from 'react';
import {
    Link,
    withRouter
} from 'react-router-dom';
import './AppHeader.css';
import { Layout, Menu, Dropdown } from 'antd';
import { Glyphicon } from 'react-bootstrap';
const Header = Layout.Header;

class AppHeader extends Component {
    constructor(props) {
        super(props);
        this.handleMenuClick = this.handleMenuClick.bind(this);
    }

    handleMenuClick({ key }) {
      if(key === "logout") {
        this.props.onLogout();
      }
    }

    render() {
        let menuItems;
        if(this.props.currentUser) {
          menuItems = [
            <Menu.Item key="/">
              <Link to="/">
                <Glyphicon glyph="home" className="nav-icon" />
              </Link>
            </Menu.Item>,
            <Menu.Item key="about">
              <Link to="/about">
                About
              </Link>
            </Menu.Item>,
            <Menu.Item key="leaderboard">
              <Link to="/leaderboard">
                Leaderboard
              </Link>
            </Menu.Item>,
          <Menu.Item key="/profile" className="profile-menu">
                <ProfileDropdownMenu
                  currentUser={this.props.currentUser}
                  handleMenuClick={this.handleMenuClick}/>
            </Menu.Item>
          ];
        } else {
          menuItems = [
            <Menu.Item key="/login">
              <Link to="/login">Login</Link>
            </Menu.Item>,
            <Menu.Item key="/about">
              <Link to="/about">About</Link>
            </Menu.Item>,
            <Menu.Item key="/virtualedition">
              <Link to="/virtualedition">Virtual Edition</Link>
            </Menu.Item>
          ];
        }

        return (
            <Header className="app-header">
            <div className="container">
              <div className="app-title" >
                <Link to="/">LdoD Game</Link>
              </div>
              <Menu
                className="app-menu"
                mode="horizontal"
                selectedKeys={[this.props.location.pathname]}
                style={{ lineHeight: '64px' }} >
                  {menuItems}
              </Menu>
            </div>
          </Header>
        );
    }
}

function ProfileDropdownMenu(props) {
  const dropdownMenu = (
  <Menu onClick={props.handleMenuClick} className="profile-dropdown-menu">
      <Menu.Item key="profile" className="dropdown-item">
          <Link to={`/user/${props.currentUser.username}`}>Profile</Link>
        </Menu.Item>
        <Menu.Item key="logout" className="dropdown-item">
            Logout
        </Menu.Item>
    </Menu>
  );

  return (
  <Dropdown
    overlay={dropdownMenu}
    trigger={['click']}
    getPopupContainer = { () => document.getElementsByClassName('profile-menu')[0]}>
    <a className="ant-dropdown-link">
       {props.currentUser.firstName} {props.currentUser.lastName}
       <Glyphicon glyph="menu-down"/>
    </a>
    </Dropdown>
  );
}


export default withRouter(AppHeader);