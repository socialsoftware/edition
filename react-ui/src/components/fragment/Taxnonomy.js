import React from 'react';
import axios from 'axios';

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
            console.log(result.data);

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

            const ref = `http://localhost:8080/edition/acronym/${taxInfo.acronym}/category/${taxInfo.urlId}`;

            const removeRef = `http://localhost:8080/virtualeditions/restricted/fraginter/${taxInfo.interExternal}/tag/dissociate/${taxInfo.categoryExternal}`;

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

        console.log(taxRows);

        return (
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
            </div>
        );
    }
}
