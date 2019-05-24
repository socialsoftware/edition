/* eslint-env jquery */

import React from 'react';
import { FormattedMessage } from 'react-intl';

export class InterEditorial extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
        };
    }

    render() {
        return (
            <div>
                <script type="text/javascript" src="../scripts/EditorialScript.js" />
                <div id="fragmentInter" className="row">
                    <form className="form-inline">
                        <div className="form-group">
                            <div
                                id="visualisation-properties-editorial"
                                className="btn-group"
                                data-toggle="checkbox">
                                <div className="checkbox">
                                    <label htmlFor="diffBox">
                                        <FormattedMessage id={'fragment.highlightdifferences'} />
                                    </label>
                                    <input id="diffBox" type="checkbox" name="diff" value="Yes" />
                                </div>
                            </div>
                        </div>
                    </form>
                    <br />
                   TRANSCRIPTION GOES HERE

                    <br />
                    <div className="well">
                             META INFO GOES HERE
                    </div>
                </div>
            </div>
        );
    }
}
