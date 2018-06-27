import React, { Component } from 'react';
import { social } from '../utils/APIUtils'
import { notification } from 'antd';
import { ACCESS_TOKEN } from '../utils/Constants'


class Test extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: null,
            response: "",
        };
    }

    componentDidMount() {
        /*social()
        .then(session => this.setState(
            {name: session.username})
        );*/
        social()
        .then(response => {
            localStorage.setItem(ACCESS_TOKEN, response.accessToken);
            this.props.onLogin();
        }).catch(error => {
            notification.error({
                message: 'LdoD Game',
                description: error.message
            });
        });
        /*fetch('http://localhost:8080/api/session', {credentials: 'include'})
            .then(res => res.json())
            .then(session => this.setState(
                {name: session.username})
            );*/

        
    }

    render() {
        return (
            <div>
                <form action="http://localhost:8080/signin/facebook" method="post" target="_blank">
                    <h1>Please login</h1>
                    <button type="submit">Login</button>
                </form>
            </div>
        )
    }
}

export default Test;