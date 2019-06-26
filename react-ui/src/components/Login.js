import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import { Form } from 'react-bootstrap';
import { SERVER_URL } from '../utils/Constants';

class LoginForm extends React.Component {
    constructor(props) {
        super(props);

        this.handleLogin = this.handleLogin.bind(this);

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);

        this.state = {
            username: '',
            password: '',
        };
    }

    handleLogin(event) {
        event.preventDefault(); // disable default handling of form.
        console.log('I am the login event handler');
        console.log(this.state);

        const loginInfo = Object.assign({}, this.state);

        console.log('==========');
        console.log(loginInfo);

        axios.post(`${SERVER_URL}/api/auth/signin`, loginInfo)
            .then((result) => {
                console.log('Login successful');
                console.log(result);
            })
            .catch(((reason) => {
                console.log('Login failed');
                console.log(reason);
            }));
    }

    handleUsernameChange(event) {
        this.setState({
            username: event.target.value,
        });
    }

    handlePasswordChange(event) {
        this.setState({
            password: event.target.value,
        });
    }

    render() {
        return (
            <Form onSubmit={this.handleLogin} className="form-horizontal" >
                <div className="form-group">
                    <div className="col-md-offset-4 col-md-4">
                        <input type="hidden" />
                        <input
                            type="text"
                            className="form-control"
                            id="username_or_email"
                            name="username"
                            placeholder="Username"
                            value={this.state.username}
                            onChange={this.handleUsernameChange} />
                    </div>
                    <br /> <br />
                    <div className="col-md-offset-4 col-md-4">
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            name="password"
                            placeholder="Password"
                            value={this.state.password}
                            onChange={this.handlePasswordChange} />
                    </div>
                </div>
                <div className="form-group">
                    <div className="login-form col-md-offset-5 col-md-2">
                        <button className="btn btn-primary form-control" type="submit">
                            <FormattedMessage id={'general.signin'} />
                        </button>
                    </div>
                </div>
            </Form>
        );
    }
}

export default class Login extends React.Component {
    render() {
        return (
            <div className="row">
                <div className="login-form">
                    <h2 style={{ textAlign: 'center' }}>
                        <FormattedMessage id={'header.title'} />
                    </h2>
                    <LoginForm />
                </div>
            </div>
        );
    }
}

