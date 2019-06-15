import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';

export class Inter2Compare extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            ids: props.ids,
            isLoaded: false,
        };
    }

    getCompareForIds() {
        axios.get('http://localhost:8080/api/services/frontend/multiple-writer', {
            params: {
                interIds: encodeURIComponent(this.state.ids),
            },
        }).then((result) => {
            console.log(result.data);

            this.setState({
                compareData: result.data,
                isLoaded: true,
            });
        });
    }


    componentDidMount() {
        this.getCompareForIds();
    }


    render() {
        if (!this.state.isLoaded) { return <div>Loading inter compare info</div>; }

        return (
            <div id="fragmentInter" className="row">
                <div>
                    <form className="form-horizontal">
                        <div className="control-group">
                            <div className="controls form-inline">
                                <div
                                    id="visualisation-properties-comparison"
                                    data-toggle="checkbox">
                                    <label htmlFor="lineCheck" className="checkbox" style={{ paddingTop: 0, minHeight: 0, fontWeight: 'normal' }}>
                                        <FormattedMessage id={'fragment.linebyline'} />
                                    </label>
                                    <input
                                        id="lineCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="line"
                                        value="Yes" />

                                    <label htmlFor="alignCheck" className="checkbox" style={{ paddingTop: 0, minHeight: 0, fontWeight: 'normal' }}>
                                        <FormattedMessage id={'fragment.alignspace'} />
                                    </label>
                                    <input
                                        id="alignCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="spaces"
                                        value="Yes" />
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <br />
                    Transcription here
                <div>
                    <h4>
                        <FormattedMessage id={'fragment.variationstable'} />(Apps Size goes here)
                    </h4>
                    <table className="table table-condensed">
                        <thead>
                            <tr>
                                <th> Short Name <br /> Title</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Variations</td>
                            </tr>
                        </tbody>

                    </table>
                </div>
            </div>
        );
    }
}
