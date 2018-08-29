import React, { Component } from 'react';
import { Table } from 'react-bootstrap';
import { getLeaderboard } from '../utils/APIUtils';

class GameLeaderboard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            users: [],
            points: [],
        }
    }


    async componentDidMount() {
        let request = await getLeaderboard();
       
        this.setState({
            users: request[0],
            points: request[1],
        })
    }


    render() {
        const tableView = [];   
        let users = this.state.users;
        let points = this.state.points;
        users.forEach(function(item, index){
            tableView.push(
                <tr key={index}>
                    <td>{index+1}</td>
                    <td>{item}</td>
                    <td>{Math.round(points[index])}</td>
                </tr>
                )
          });

        return (
            <div>
                <Table responsive>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Username</th>
                            <th>Points</th>
                        </tr>
                    </thead>
                    <tbody>
                        {tableView}
                    </tbody>
                </Table>
            </div>
        );
    }

}
export default GameLeaderboard;
