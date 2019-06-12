import React from 'react';

export class Taxonomy extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
        };
    }


    render() {
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
                    <tbody />
                </table>
            </div>
        );
    }
}
