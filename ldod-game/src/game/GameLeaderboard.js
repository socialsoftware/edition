import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Table, Icon, Divider } from 'antd';
import {Glyphicon} from 'react-bootstrap';
const data = [{
  key: '1',
  username: 'John Brown',
  age: 32,
  address: 'New York No. 1 Lake Park',
}, {
  key: '2',
  username: 'Jim Green',
  age: 42,
  address: 'London No. 1 Lake Park',
}, {
  key: '3',
  username: 'Joe Black',
  age: 32,
  address: 'Sidney No. 1 Lake Park',
}];
const { Column, ColumnGroup } = Table;

class GameLeaderboard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            allTimesData: [],
            recentData: [],
            isToggled: false
        }
    }

    // called when pressing the toggle button
    handleToggle(e,isToggled){
        this.setState({isToggled: isToggled});
    }

    componentWillMount() {
        // get recent data
        /*fetch(``)
        .then(data => data.json())
        .then(data => {this.setState({recentData:data})})
        .catch(error => {console.log(error)});*/

        // get all times data
        /*fetch(``)
        .then(data => data.json())
        .then(data => {this.setState({allTimesData:data})})
        .catch(error => {console.log(error)});*/

    }


    render() {
        return (
            <div>
                <Table dataSource={data}>
                    <Column
                        title="Username"
                        dataIndex="username"
                        key="username"
                    />
                    <Column
                      title="Age"
                      dataIndex="age"
                      key="age"
                    />
                    <Column
                      title="Action"
                      key="action"
                      render={(text, record) => (
                        <span>
                          <a href="javascript:;">Action 一 {record.name}</a>
                          <Divider type="vertical" />
                          <a href="javascript:;">Delete</a>
                          <Divider type="vertical" />
                          <a href="javascript:;" className="ant-dropdown-link">
                            More actions <Icon type="down" />
                          </a>
                        </span>
                      )}
                    />
                </Table>
            </div>
        );
    }

}
export default GameLeaderboard;
