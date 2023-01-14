export default /*css*/ `
* {
  box-sizing: border-box;
}

a:hover {
  color: #0c4ef6;
}

a {
  text-decoration: none;
  color: black;
  cursor: pointer;
}

.navbar-default {
  background-color: rgba(255, 255, 255, 0.8);
  border-bottom: none;
  border-color: #e7e7e7;
}

.ldod-navbar.navbar {
  border-top: 10px solid #fc1b27;
  font-family: 'Space-Mono';
  font-weight: bold;
  font-size: 14px;
}

.navbar {
  position: relative;
  min-height: 50px;
  margin-bottom: 20px;
  border: 1px solid transparent;
}

.navbar-fixed-bottom,
.navbar-fixed-top {
  position: fixed;
  right: 0;
  left: 0;
  z-index: 1030;
}

.navbar-fixed-top {
  top: 0;
  border-width: 0 0 1px;
}

.container-fluid {
  border-bottom: 1px solid #fc1b27;
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}

.container-fluid::after,
.container-fluid::before,
.container::after,
.container::before,
.nav::after,
.nav::before,
.navbar-collapse::after,
.navbar-collapse::before {
  display: table;
  content: ' ';
}

.container-fluid::after,
.container::after {
  clear: both;
}

.container {
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}

.navbar-header {
  float: none;
}

.navbar-brand {
  float: left;
  padding: 15px 15px;
  line-height: 20px;
  font-family: 'Work-Sans';
  font-weight: 800;
  font-size: 30px;
  padding-top: 20px;
  padding-bottom: 22px;
}

.navbar-toggle {
  position: relative;
  float: right;
  padding: 9px 10px;
  background-color: transparent;
  background-image: none;
  border-radius: 4px;
  border-color: #ddd;
  border: none;
  margin-top: 13px;
  margin-bottom: 12px;
  margin-right: -10px;
}

.navbar-toggle:hover .icon-bar {
  background-color: #0c4ef6;
}

button {
  cursor: pointer;
  text-transform: none;
}

.icon-bar {
  display: block;
  width: 22px;
  height: 3px;
  border-radius: 1px;
  background-color: black;
}

.icon-bar + .icon-bar {
  margin-top: 5px;
}

.nav {
  padding-left: 0;
  margin-bottom: 0;
  list-style: none;
}

.navbar-nav {
  text-transform: uppercase;
  margin: 7.5px -15px;
}

.navbar-nav-flex {
  display: flex;
  width: 100%;
  justify-content: space-between;
}

.nav > li {
  position: relative;
  display: block;
}

.nav > li > a {
  position: relative;
  display: block;
  padding: 10px 15px;
  padding-left: 0;
  padding-right: 0;
}

.navbar-nav > li > a {
  padding-top: 10px;
  padding-bottom: 10px;
  line-height: 20px;
}

.navbar-header .navbar-nav > li > a {
  line-height: 32px;
  padding-right: 15px;
}

li.nav-lang > a {
  display: inline-block;
}

.navbar-nav > li > a:focus {
  color: #fc1b27;
}

.navbar-nav > li > a:hover {
  color: #0c4ef6;
}

a.active {
  color: #fc1b27;
}

.open > a:hover,
.open > a:focus {
  background-color: #e7e7e7;
}

.caret {
  display: inline-block;
  width: 0;
  height: 0;
  margin-left: 2px;
  vertical-align: middle;
  border-top: 4px dashed;
  border-top: 4px solid;
  border-right: 4px solid transparent;
  border-left: 4px solid transparent;
}

.dropdown-menu {
  border-radius: 0;
  border: none;
  box-shadow: none;
  background: none;
  padding: 5px 0;
  padding-top: 15px;
  padding-bottom: 15px;
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  display: none;
  float: left;
  min-width: 160px;
  margin: 2px 0 0;
  margin-top: 0;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
  font-size: 14px;
  text-align: left;
  list-style: none;
}

.open > .dropdown-menu {
  display: block;
}

.dropdown-menu-bg {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 200vw;
  -webkit-transform: translateX(-50%);
  -moz-transform: translateX(-50%);
  -ms-transform: translateX(-50%);
  -o-transform: translateX(-50%);
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.8);
  z-index: -1;
  border-top: 1px dashed #fc1b27;
  border-bottom: 1px solid #fc1b27;
}

.dropdown-menu > li > a {
  display: block;
  padding: 3px 20px;
  clear: both;
  line-height: 1.42857143;
  white-space: nowrap;
  padding-left: 0;
  padding-right: 0;
  font-weight: inherit;
}

.dropdown-menu .divider {
  height: 1px;
  margin: 9px 0;
  overflow: hidden;
  background-color: #e5e5e5;
}

li[is='user-component'].dropdown > a.dropdown-toggle {
  color: #fc1b27;
}

.user-component .dropdown-menu {
  background-color: white;
  border: 1px solid #e71924;
  margin-right: 15px;
  padding-left: 15px;
  padding-right: 15px;
}

@media (max-width: 767px) {
  .navbar-collapse {
    background-color: white;
    border-bottom: 1px solid #fc1b27;
    display: none;
  }
  .navbar-nav {
    margin-left: 0;
    margin-right: 0;
  }
  .navbar-nav-flex {
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -webkit-flex-direction: column;
    -moz-box-orient: vertical;
    -moz-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
  }
  .navbar-collapse {
    border-top: 1px solid transparent;
    box-shadow: inset 0 1px 0 rgb(255 255 255 / 10%);
    padding-right: 15px;
    padding-left: 15px;
    overflow-x: visible;
    border-color: #e7e7e7;
    margin-right: -15px;
    margin-left: -15px;
  }
  .navbar-collapse[aria-expanded='true'] {
    overflow: hidden;
    display: block;
  }
  .dropdown-menu {
    padding-top: 0;
    padding-bottom: 0;
  }
  .open .dropdown-menu {
    position: static;
    float: none;
    width: auto;
    margin-top: 0;
    background-color: transparent;
    border: 0;
    -webkit-box-shadow: none;
    box-shadow: none;
  }
  .dropdown-menu .dropdown-menu-bg {
    display: none;
  }
  .open .dropdown-menu > li > a {
    line-height: 20px;
    padding: 5px 15px 5px 15px;
  }
  .hidden-xs {
    display: none;
  }
}

@media (min-width: 768px) {
  .navbar-fixed-top,
  .navbar-fixed-bottom {
    border-radius: 0;
  }
  .navbar > .container .navbar-brand,
  .navbar > .container-fluid .navbar-brand {
    margin-left: -15px;
  }
  .container-fluid > .navbar-header,
  .container > .navbar-header {
    margin-right: 0;
    margin-left: 0;
  }
  .container {
    width: 750px;
  }
  .navbar-toggle {
    display: none;
  }
  .navbar-nav {
    float: left;
    margin: 0;
  }
  .navbar-nav > li {
    float: left;
  }
  .navbar-nav > li > a {
    padding-top: 15px;
    padding-bottom: 15px;
  }
  .user-component {
    float: right;
    margin-right: -15px;
  }
  .user-component .dropdown-menu {
    right: 0;
    left: auto;
  }
  li[is='user-component'].visible-xs {
    display: none;
  }
}

@media (min-width: 992px) {
  .container {
    width: 970px;
  }
}

@media (min-width: 1200px) {
  .container {
    width: 1170px;
  }
}

#admin[aria-hidden='true'] {
  display: none;
}

li[is='user-component'] > a:hover {
  color: #0c4ef6;
}

span.caret {
  margin-left: 8px;
}
`;