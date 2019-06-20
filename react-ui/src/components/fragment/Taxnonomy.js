import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';

export class Taxonomy extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            taxonomy: null,
            isLoaded: false,
        };
    }

    getTaxonomyInfo() {
        axios.get('http://localhost:8080/api/services/frontend/taxonomy', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            this.setState({
                taxonomy: result.data,
                isLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getTaxonomyInfo();
    }

    render() {
        if (!this.state.isLoaded) {
            return (
                <div>Loading taxonomy info</div>
            );
        }

        const taxRows = [];

        for (let i = 0; i < this.state.taxonomy.length; i++) {
            const taxInfo = this.state.taxonomy[i];

            const userRow = [];

            for (let j = 0; j < taxInfo.users.length; j++) {
                const userInfo = taxInfo.users[j];

                const ref = `http://localhost:8080/edition/user/${userInfo.username}`;

                userRow.push(<a href={ref}>{userInfo.firstName} {userInfo.lastName} ({userInfo.username})</a>);
            }

            const ref = `http://localhost:9000/edition/acronym/${taxInfo.acronym}/category/${taxInfo.urlId}`;

            const removeRef = `http://localhost:9000/virtualeditions/restricted/fraginter/${taxInfo.interExternal}/tag/dissociate/${taxInfo.categoryExternal}`;

            taxRows.push(
                <tr>
                    <td><a
                        href={ref}>{taxInfo.name}</a>
                        <a
                            href={removeRef}>
                            <span className="glyphicon glyphicon-remove" /></a>
                    </td>
                    <td>
                        {userRow}
                    </td>
                </tr>,

            );
        }

        return ([
            <div className="row" id="taxonomy">
                <link
                    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
                    rel="stylesheet" />
                <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js" />
                <button
                    className="btn btn-primary pull-right"
                    data-toggle="modal"
                    data-target="#myModal">
                    <span className="glyphicon glyphicon-plus" />
                </button>
                <table className="table table-hover">
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag" /></th>
                            <th><span className="glyphicon glyphicon-user" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        {taxRows}
                    </tbody>
                </table>
            </div>,

            <div className="modal fade" id="myModal" tabIndex="-1" role="dialog">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button
                                type="button"
                                className="close"
                                data-dismiss="modal"
                                aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 className="modal-title">THIS IS A TITLE</h4>
                        </div>
                        <div className="modal-body">
                            <div className="row text-center">
                                <form
                                    className="form"
                                    method="POST"
                                    action="/virtualeditions/restricted/tag/associate">
                                    <input
                                        type="hidden"
                                        name="THIS SHOULD BE A VALID NAME"
                                        value="THIS SHOULD BE A VAILD TOKEN" />
                                    <div className="form-group">
                                        <div className="hidden">
                                            <input
                                                type="hidden"
                                                name="fragInterId"
                                                className="form-control"
                                                value="external id" />
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <select
                                            name="categories[]"
                                            id="category-select"
                                            className="form-control"
                                            style={{ width: '75%' }}
                                            multiple="true">
                                            <option
                                                value="TEMP">NON ASSIGNED CATEGORIES</option>
                                            <option
                                                value="temp"
                                                selected="selected">ASSIGNED CATEGORIES</option>
                                        </select>
                                    </div>
                                    <div className="form-group">
                                        <button type="submit" className="btn btn-sm btn-primary">
                                            <FormattedMessage id={'general.associate'} />
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-default" data-dismiss="modal">
                                <FormattedMessage id={'general.close'} />
                            </button>
                        </div>
                    </div>
                </div>
            </div>,
        ]
        );
    }
}
