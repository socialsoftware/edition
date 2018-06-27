import React, { Component } from 'react';
import { createPoll } from '../../utils/APIUtils';
import { FRAGMENT_LIST_SIZE } from '../../utils/Constants';
import { Glyphicon } from 'react-bootstrap';
import './GameConfig.css';
import { Form, Input, Button, Icon, Select, Col, notification } from 'antd';
const Option = Select.Option;
const FormItem = Form.Item;
const { TextArea } = Input

class GameConfig extends Component {
    constructor(props) {
        super(props);
        this.state = {
            virtualEdition: "LdoD-experts",
            numberFragments: 5,
            time: 0,
            vocabulary: "closed",
            description: ""
        };

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        const data = {
            description: "does this work?",
            numberFragments: 5
        };

    }


    render() {
        return (
            <div className="game-config-container">
                <h1 className="page-title">Game Configuration</h1>
                <div className="game-config-content">
                <Form onSubmit={this.handleSubmit} className="game-config-form">
                    <FormItem>
                    <Input
                        prefix={<Glyphicon glyph="book" />}
                        size="small"
                        name="virtual edition"
                        placeholder="Enter Virtual Edition Acronym" />
                    </FormItem>
                    <FormItem className="game-config-form-row">
                        Number of fragments:
                        <Select
                            name="numberFragments"
                            defaultValue="1"
                            style={{ width: 60 }} >
                            {
                                Array.from(Array(FRAGMENT_LIST_SIZE).keys()).map(i =>
                                <Option key={i}>{i}</Option>
                                )
                            }
                        </Select>
                    </FormItem>

                    <FormItem className="game-config-form-row">
                            <Button type="primary"
                                htmlType="submit"
                                size="small"
                                className="game-config-form-button">Create Game Configuration</Button>
                    </FormItem>
                </Form>
                </div>
            </div>
        );
    }
}


export default GameConfig;