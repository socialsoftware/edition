import React, { Component } from 'react';
import { login } from '../../utils/APIUtils';
import './Login.css';
import { ACCESS_TOKEN, LDOD_MESSAGE } from '../../utils/Constants';

import { Form, Input, notification } from 'antd';
import {Glyphicon} from 'react-bootstrap';
const FormItem = Form.Item;

class Login extends Component {
    render() {
        const AntWrappedLoginForm = Form.create()(LoginForm)
        return (
            <div className="login-container">
                <h1 className="page-title">Login</h1>
                <div className="login-content">
                    <AntWrappedLoginForm onLogin={this.props.onLogin} />
                </div>
            </div>
        );
    }
}

class LoginForm extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const loginRequest = Object.assign({}, values);
                login(loginRequest)
                .then(response => {
                    localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                    this.props.onLogin();
                }).catch(error => {
                    if(error.status === 401) {
                        notification.error({
                            message: LDOD_MESSAGE,
                            description: 'Your Username or Password is incorrect. Please try again!'
                        });                    
                    } else {
                        notification.error({
                            message: LDOD_MESSAGE,
                            description: 'Sorry! Something went wrong. Please try again later!'
                        });                                            
}
                });
            }
        });
    }

    render() {
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSubmit} className="login-form">
                <FormItem>
                    {getFieldDecorator('username', {
                        rules: [{ required: true, message: 'Please input your LdoD Username!' }],
                    })(
                    <Input
                        prefix={<Glyphicon glyph="user" />}
                        size="large"
                        name="username"
                        placeholder="LdoD Username" />
                    )}
                </FormItem>
                <FormItem>
                {getFieldDecorator('password', {
                    rules: [{ required: true, message: 'Please input your LdoD Password!' }],
                })(
                    <Input
                        prefix={<Glyphicon glyph="lock" />}
                        size="large"
                        name="password"
                        type="password"
                        placeholder="LdoD Password"  />
                )}
                </FormItem>
                <FormItem>
                    <div className="login-form">
                        <button className="btn btn-primary form-control" type="submit">Login</button>	
                    </div>
                </FormItem>
            </Form>
        );
    }
}


export default Login;